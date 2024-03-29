package ch.brix.camunda.connector.util.templateGenerator;

import ch.brix.camunda.connector.util.templateGenerator.schema.*;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateGenerator {

	/**
	 * Generates the template and prints out the input variables to be copied in @OutboundConnector.
	 *
	 * @param args 0: the template class, 1: the template file location, 2: (optional) the template processor class
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2 || args.length > 3)
			throw new IllegalArgumentException("Too few or too many arguments, expected: 0: the template class, 1: the template file location, 2: (optional) the template processor class");
		Class<?> clazz = Class.forName(args[0]);
		Class<?> processorClass = args.length == 3 ? Class.forName(args[2]) : null;
		TemplateDefinition templateDefinition = clazz.getDeclaredAnnotation(TemplateDefinition.class);
		if (templateDefinition == null)
			throw new RuntimeException("TemplateDefinition annotation missing.");
		Template template = new Template();
		template.setSchema(templateDefinition.schema());
		template.setName(templateDefinition.name());
		template.setId(templateDefinition.id());
		if (templateDefinition.version() >= 0)
			template.setVersion(templateDefinition.version());
		if (!templateDefinition.description().isBlank())
			template.setDescription(templateDefinition.description());
		if (!templateDefinition.documentation().isBlank())
			template.setDocumentationRef(templateDefinition.documentation());
		if (!templateDefinition.icon().isBlank())
			template.setIcon(Icon.builder()
					.contents(templateDefinition.icon())
					.build());
		if (!templateDefinition.categoryId().isBlank())
			template.setCategory(Category.builder()
					.id(templateDefinition.categoryId())
					.name(templateDefinition.categoryName())
					.build());
		template.setAppliesTo(new LinkedHashSet<>(Arrays.asList(templateDefinition.appliesTo())));
		template.setElementType(ElementType.builder()
				.value(templateDefinition.elementType())
				.build());
		Set<Group> groups = new LinkedHashSet<>();
		for (int i = 0; i < templateDefinition.groupIds().length; i++) {
			Group.GroupBuilder builder = Group.builder().id(templateDefinition.groupIds()[i]);
			if (templateDefinition.groupLabels().length == templateDefinition.groupIds().length)
				builder.label(templateDefinition.groupLabels()[i]);
			if (templateDefinition.groupTooltips().length == templateDefinition.groupIds().length && !templateDefinition.groupTooltips()[i].isBlank())
				builder.tooltip(templateDefinition.groupTooltips()[i]);
			if (templateDefinition.groupOpenByDefaults().length == templateDefinition.groupIds().length && !templateDefinition.groupOpenByDefaults()[i])
				builder.openByDefault(false);
			groups.add(builder.build());
		}
		template.setGroups(groups);
		Set<Property> properties = new LinkedHashSet<>();
		Property p = new Property();
		p.setType(TYPE.HIDDEN);
		p.setValue(templateDefinition.id());
		p.setBinding(Binding.builder().type(BINDING_TYPE.ZEEBE_TASK_DEFINITION_TYPE).build());
		properties.add(p);
		properties.addAll(getProperties(clazz, template, null, null, null, false));
		template.setProperties(properties);
		addSpecialProperties(templateDefinition, properties);
		// moved that down to add standard groups last
		if (templateDefinition.addDefaultOutputMapping())
			template.getGroups().add(Group.builder()
					.id("output")
					.label("Output Mapping")
					.tooltip(templateDefinition.defaultOutputMappingTooltip().isBlank() ? null : templateDefinition.defaultOutputMappingTooltip())
					.build());
		if (templateDefinition.addDefaultErrorHandling())
			template.getGroups().add(Group.builder()
					.id("errors")
					.label("Error Handling")
					.tooltip(templateDefinition.defaultErrorHandlingTooltip().isBlank() ? null : templateDefinition.defaultErrorHandlingTooltip())
					.build());
		if (processorClass != null) {
			TemplateProcessor tp = (TemplateProcessor) processorClass.getConstructor().newInstance();
			tp.process(template);
		}
		File file = new File(args[1]);
		if (file.getParentFile() != null)
			file.getParentFile().mkdirs();
		try (FileWriter fileWriter = new FileWriter(args[1], StandardCharsets.UTF_8)) {
			fileWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(template));
			fileWriter.flush();
		}
		System.out.println("Input variables (to be copied in @OutboundConnector):");
		System.out.println("{" + properties.stream()
				.filter(s -> s != null && s.getBinding() != null && s.getBinding().getName() != null && !s.getBinding().getName().isBlank())
				.map(s -> "\"" + s.getBinding().getName() + "\"")
				.collect(Collectors.joining(", ")) + "}");
	}

	private static void addSpecialProperties(TemplateDefinition templateDefinition, Set<Property> properties) {
		if (templateDefinition.addDefaultOutputMapping()) {
			properties.add(Property.builder()
					.label("Result Variable")
					.description("Name of the variable to store the response in.")
					.groupId("output")
					.type(TYPE.STRING)
					.binding(Binding.builder()
							.type(BINDING_TYPE.ZEEBE_TASK_HEADER)
							.key("resultVariable")
							.build())
					.build());
			properties.add(Property.builder()
					.label("Result Expression")
					.description(templateDefinition.defaultOutputMappingResultExpressionDescription())
					.groupId("output")
					.type(TYPE.TEXT)
					.feel(FEEL.REQUIRED)
					.binding(Binding.builder()
							.type(BINDING_TYPE.ZEEBE_TASK_HEADER)
							.key("resultExpression")
							.build())
					.build());
		}
		if (templateDefinition.addDefaultErrorHandling())
			properties.add(Property.builder()
					.label("Error Expression")
					.description(templateDefinition.defaultErrorHandlingExpressionDescription())
					.groupId("errors")
					.type(TYPE.TEXT)
					.feel(FEEL.REQUIRED)
					.binding(Binding.builder()
							.type(BINDING_TYPE.ZEEBE_TASK_HEADER)
							.key("errorExpression")
							.build())
					.build());
	}

	private static Set<Property> getProperties(Class<?> propertyClass, Template template, String propertyId, Set<String> propertyValues, String groupId, boolean isActive) {
		Set<Property> deferredProperties = new LinkedHashSet<>();
		Set<Property> properties = new LinkedHashSet<>();
		Set<Class<?>> processedClasses = new HashSet<>();
		for (Field field : propertyClass.getDeclaredFields()) {
			PropertyGroup propertyGroup = field.getDeclaredAnnotation(PropertyGroup.class);
			if (propertyGroup != null && !processedClasses.contains(field.getType())) {
				String grpId = propertyGroup.groupId().isEmpty() ? (groupId != null ? groupId : field.getType().getSimpleName()) : propertyGroup.groupId();
				if (!propertyGroup.groupName().isEmpty())
					template.getGroups().add(Group.builder().id(grpId)
							.label(propertyGroup.groupName())
							.tooltip(propertyGroup.groupTooltip().isBlank() ? null : propertyGroup.groupTooltip())
							.openByDefault(propertyGroup.openByDefault() ? null : false).build());
				deferredProperties.addAll(getProperties(field.getType(), template,
						propertyGroup.conditionPropertyId().isEmpty() ? propertyId : propertyGroup.conditionPropertyId(),
						propertyGroup.conditionOneOf().length == 0 ? propertyValues : Arrays.stream(propertyGroup.conditionOneOf()).collect(Collectors.toSet()),
						grpId,
						propertyGroup.conditionIsActive()));
			}
			PropertyDefinition propertyDefinition = field.getDeclaredAnnotation(PropertyDefinition.class);
			SerializedName serializedName = field.getDeclaredAnnotation(SerializedName.class);
			if (propertyDefinition == null)
				continue;
			Property property = new Property();
			property.setLabel(propertyDefinition.label());
			if (!propertyDefinition.id().isBlank()) {
				if (propertyDefinition.id().equals("<field>"))
					property.setId(serializedName == null ? field.getName() : serializedName.value());
				else
					property.setId(propertyDefinition.id());
			}
			if (!propertyDefinition.description().isBlank())
				property.setDescription(propertyDefinition.description());
			if (!propertyDefinition.tooltip().isBlank())
				property.setTooltip(propertyDefinition.tooltip());
			if (!propertyDefinition.groupId().isBlank())
				property.setGroupId(propertyDefinition.groupId());
			else if (groupId != null)
				property.setGroupId(groupId);
			property.setType(propertyDefinition.type());
			if (propertyDefinition.feel() != FEEL.NO && propertyDefinition.type() != TYPE.DROPDOWN && propertyDefinition.type() != TYPE.BOOLEAN)
				property.setFeel(propertyDefinition.feel());
			Binding binding = new Binding();
			binding.setType(propertyDefinition.bindingType());
			if (!propertyDefinition.bindingName().isBlank()) {
				if (propertyDefinition.bindingName().equals("<field>"))
					binding.setName(serializedName == null ? field.getName() : serializedName.value());
				else
					binding.setName(propertyDefinition.bindingName());
			}
			if (!propertyDefinition.bindingKey().isBlank())
				binding.setKey(propertyDefinition.bindingKey());
			property.setBinding(binding);
			if (propertyDefinition.notEmpty() || propertyDefinition.minLength() > 0 || propertyDefinition.maxLength() > 0 || !propertyDefinition.pattern().isBlank()) {
				Constraints constraints = getConstraints(propertyDefinition);
				property.setConstraints(constraints);
			}
			if (propertyDefinition.optional())
				property.setOptional(true);
			if (!propertyDefinition.editable())
				property.setEditable(false);
			if (!propertyDefinition.value().isBlank())
				property.setValue(propertyDefinition.value());
			if (propertyDefinition.choiceValues().length > 0 || !propertyDefinition.choiceEnum().equals(PropertyDefinition.Null.class)) {
				Set<Choice> choices = new LinkedHashSet<>();
				if (propertyDefinition.choiceEnum().equals(PropertyDefinition.Null.class)) {
					for (int i = 0; i < propertyDefinition.choiceValues().length; i++) {
						choices.add(Choice.builder()
								.value(propertyDefinition.choiceValues()[i])
								.name(propertyDefinition.choiceNames()[i])
								.build());
						if (propertyDefinition.choiceClasses().length == propertyDefinition.choiceValues().length) {
							String grpId = null;
							if (propertyDefinition.choiceGroupNames().length == propertyDefinition.choiceValues().length && !propertyDefinition.choiceGroupNames()[i].isBlank()) {
								grpId = propertyDefinition.choiceGroupIds().length == propertyDefinition.choiceValues().length && !propertyDefinition.choiceGroupIds()[i].isBlank() ? propertyDefinition.choiceGroupIds()[i] : propertyDefinition.choiceValues()[i];
								template.getGroups().add(Group.builder()
										.id(grpId)
										.label(propertyDefinition.choiceGroupNames()[i])
										.tooltip(propertyDefinition.choiceGroupTooltips().length == propertyDefinition.choiceValues().length && !propertyDefinition.choiceGroupTooltips()[i].isBlank() ? propertyDefinition.choiceGroupTooltips()[i] : null)
										.openByDefault(propertyDefinition.choiceGroupOpenByDefaults().length == propertyDefinition.choiceValues().length && !propertyDefinition.choiceGroupOpenByDefaults()[i] ? false : null)
										.build());
							} else if (propertyDefinition.choiceGroupIds().length == propertyDefinition.choiceValues().length && !propertyDefinition.choiceGroupIds()[i].isBlank()) {
								grpId = propertyDefinition.choiceGroupIds()[i];
							}
							if (!processedClasses.contains(propertyDefinition.choiceClasses()[i])) {
								Set<String> pVals = new HashSet<>();
								for (int j = 0; j < propertyDefinition.choiceClasses().length; j++) {
									if (propertyDefinition.choiceClasses()[i].equals(propertyDefinition.choiceClasses()[j]))
										pVals.add(propertyDefinition.choiceValues()[j]);
								}
								deferredProperties.addAll(getProperties(propertyDefinition.choiceClasses()[i], template, property.getId(), pVals, grpId, false));
								processedClasses.add(propertyDefinition.choiceClasses()[i]);
							}
						}
					}
				} else if (propertyDefinition.choiceEnum().getEnumConstants().length > 0) { // load choices from enum
					Method getChoiceName = null;
					try {
						getChoiceName = propertyDefinition.choiceEnum().getDeclaredMethod("getChoiceName");
					} catch (NoSuchMethodException ignore) {}
					Method getChoiceClass = null;
					try {
						getChoiceClass = propertyDefinition.choiceEnum().getDeclaredMethod("getChoiceClass");
					} catch (NoSuchMethodException ignore) {}
					Method getChoiceGroupId = null;
					try {
						getChoiceGroupId = propertyDefinition.choiceEnum().getDeclaredMethod("getChoiceGroupId");
					} catch (NoSuchMethodException ignore) {}
					Map<Class<?>, Set<String>> optionsPerClass = new HashMap<>();
					List<String> choiceGroupIds = new ArrayList<>();
					List<Class<?>> choiceClasses = new ArrayList<>();
					for (Enum<?> enumConstant : propertyDefinition.choiceEnum().getEnumConstants()) {
						String value = getValue(enumConstant, propertyDefinition.choiceEnum());
						String name = enumConstant.toString();
						if (getChoiceName != null) {
							try {
								name = (String) getChoiceName.invoke(enumConstant);
							} catch (IllegalAccessException | InvocationTargetException ignore) {}
						}
						choices.add(Choice.builder()
								.value(value)
								.name(name)
								.build());
						if (getChoiceClass != null) {
							try {
								Class<?> clazz = (Class<?>) getChoiceClass.invoke(enumConstant);
								choiceClasses.add(clazz);
								optionsPerClass.computeIfAbsent(clazz, x -> new HashSet<>()).add(value);
							} catch (IllegalAccessException | InvocationTargetException e) {
								choiceClasses.add(null);
							}
						}
						if (getChoiceGroupId != null) {
							try {
								choiceGroupIds.add((String) getChoiceGroupId.invoke(enumConstant));
							} catch (IllegalAccessException | InvocationTargetException e) {
								choiceGroupIds.add(null);
							}
						}
					}
					Iterator<String> cgis = choiceGroupIds.iterator();
					for (Class<?> choiceClass : choiceClasses) {
						if (choiceClass == null)
							continue;
						if (!processedClasses.contains(choiceClass)) {
							deferredProperties.addAll(getProperties(choiceClass, template, property.getId(), optionsPerClass.get(choiceClass), getChoiceGroupId == null ? null : cgis.next(), false));
							processedClasses.add(choiceClass);
						}
					}
				}
				property.setChoices(choices);
				property.setType(TYPE.DROPDOWN);
				property.setFeel(null);
			}
			if (!propertyDefinition.conditionPropertyId().isBlank()) {
				Condition condition = new Condition();
				condition.setProperty(propertyDefinition.conditionPropertyId());
				if (propertyDefinition.conditionIsActive()) {
					condition.setIsActive(true);
				} else {
					if (!propertyDefinition.conditionEquals().isBlank())
						condition.setEquals(propertyDefinition.conditionEquals());
					if (propertyDefinition.conditionOneOf().length > 0)
						condition.setOneOf(new LinkedHashSet<>(Arrays.asList(propertyDefinition.conditionOneOf())));
					property.setCondition(condition);
				}
			} else if (propertyId != null && ((propertyValues != null && !propertyValues.isEmpty()) || isActive)) {
				Condition.ConditionBuilder condition = Condition.builder().property(propertyId);
				if (isActive) {
					property.setCondition(condition.isActive(true).build());
				} else {
					if (propertyValues.size() == 1)
						property.setCondition(condition.equals(propertyValues.iterator().next()).build());
					else
						property.setCondition(condition.oneOf(propertyValues).build());
				}
			}
			properties.add(property);
		}
		properties.addAll(deferredProperties);
		return properties;
	}

	private static Constraints getConstraints(PropertyDefinition propertyDefinition) {
		Constraints constraints = new Constraints();
		if (propertyDefinition.notEmpty())
			constraints.setNotEmpty(true);
		if (propertyDefinition.minLength() > 0)
			constraints.setMinLength(propertyDefinition.minLength());
		if (propertyDefinition.maxLength() > 0)
			constraints.setMaxLength(propertyDefinition.maxLength());
		if (!propertyDefinition.pattern().isBlank()) {
			Pattern pattern = new Pattern();
			pattern.setRegex(propertyDefinition.pattern());
			if (!propertyDefinition.patternMessage().isBlank())
				pattern.setMessage(propertyDefinition.patternMessage());
			constraints.setPattern(pattern);
			constraints.setNotEmpty(propertyDefinition.notEmpty());
		}
		return constraints;
	}

	private static String getValue(Enum<?> enumConstant, Class<? extends Enum> theEnum) {
		String value = enumConstant.name();
		try {
			SerializedName serializedName = theEnum.getDeclaredField(value).getAnnotation(SerializedName.class);
			if (serializedName != null)
				value = serializedName.value();
		} catch (NoSuchFieldException ignore) {}
		return value;
	}

}
