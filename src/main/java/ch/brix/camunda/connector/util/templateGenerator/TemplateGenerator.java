package ch.brix.camunda.connector.util.templateGenerator;

import ch.brix.camunda.connector.util.templateGenerator.schema.*;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
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
		if (templateDefinition.groupIds().length > 0) {
			Set<Group> groups = new LinkedHashSet<>();
			for (int i = 0; i < templateDefinition.groupIds().length; i++)
				groups.add(Group.builder()
						.id(templateDefinition.groupIds()[i])
						.label(templateDefinition.groupLabels()[i])
						.build());
			template.setGroups(groups);
		}
		Set<Property> properties = new LinkedHashSet<>();
		Property p = new Property();
		p.setType(TYPE.HIDDEN);
		p.setValue(templateDefinition.id());
		p.setBinding(Binding.builder().type(BINDING_TYPE.ZEEBE_TASK_DEFINITION_TYPE).build());
		properties.add(p);
		properties.addAll(getProperties(clazz, template, null, null, false));
		template.setProperties(properties);
		addSpecialProperties(templateDefinition, properties);
		if (templateDefinition.groupIds().length > 0) { // moved that down to add standard groups last
			if (templateDefinition.addDefaultOutputMapping())
				template.getGroups().add(Group.builder()
						.id("output")
						.label("Output Mapping")
						.build());
			if (templateDefinition.addDefaultErrorHandling())
				template.getGroups().add(Group.builder()
						.id("errors")
						.label("Error Handling")
						.build());
		}
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
					.description(DefaultTexts.RESULT_VARIABLE)
					.groupId(templateDefinition.groupIds().length > 0 ? "output" : null)
					.type(TYPE.STRING)
					.binding(Binding.builder()
							.type(BINDING_TYPE.ZEEBE_TASK_HEADER)
							.key("resultVariable")
							.build())
					.build());
			properties.add(Property.builder()
					.label("Result Expression")
					.description(DefaultTexts.RESULT_EXPRESSION)
					.groupId(templateDefinition.groupIds().length > 0 ? "output" : null)
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
					.description(DefaultTexts.ERROR_EXPRESSION)
					.groupId(templateDefinition.groupIds().length > 0 ? "errors" : null)
					.type(TYPE.TEXT)
					.feel(FEEL.REQUIRED)
					.binding(Binding.builder()
							.type(BINDING_TYPE.ZEEBE_TASK_HEADER)
							.key("errorExpression")
							.build())
					.build());
	}

	private static Set<Property> getProperties(Class<?> propertyClass, Template template, String propertyId, String propertyValue, boolean setGroupToValue) {
		Set<Property> deferredProperties = new LinkedHashSet<>();
		Set<Property> properties = new LinkedHashSet<>();
		for (Field field : propertyClass.getDeclaredFields()) {
			PropertyDefinition propertyDefinition = field.getDeclaredAnnotation(PropertyDefinition.class);
			if (propertyDefinition == null)
				continue;
			Property property = new Property();
			property.setLabel(propertyDefinition.label());
			if (!propertyDefinition.id().isBlank()) {
				if (propertyDefinition.id().equals("<field>"))
					property.setId(field.getName());
				else
					property.setId(propertyDefinition.id());
			}
			if (!propertyDefinition.description().isBlank())
				property.setDescription(propertyDefinition.description());
			if (!propertyDefinition.groupId().isBlank())
				property.setGroupId(propertyDefinition.groupId());
			else if (setGroupToValue)
				property.setGroupId(propertyValue);
			property.setType(propertyDefinition.type());
			if (propertyDefinition.feel() != FEEL.NO && propertyDefinition.type() != TYPE.DROPDOWN && propertyDefinition.type() != TYPE.BOOLEAN)
				property.setFeel(propertyDefinition.feel());
			Binding binding = new Binding();
			binding.setType(propertyDefinition.bindingType());
			if (!propertyDefinition.bindingName().isBlank()) {
				if (propertyDefinition.bindingName().equals("<field>"))
					binding.setName(field.getName());
				else
					binding.setName(propertyDefinition.bindingName());
			}
			if (!propertyDefinition.bindingKey().isBlank())
				binding.setKey(propertyDefinition.bindingKey());
			property.setBinding(binding);
			if (propertyDefinition.notEmpty() || propertyDefinition.minLength() > 0 || propertyDefinition.maxLength() > 0 || !propertyDefinition.pattern().isBlank()) {
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
				}
				property.setConstraints(constraints);
			}
			if (propertyDefinition.optional())
				property.setOptional(true);
			if (!propertyDefinition.editable())
				property.setEditable(false);
			if (!propertyDefinition.value().isBlank())
				property.setValue(propertyDefinition.value());
			if (propertyDefinition.choiceValues().length > 0) {
				Set<Choice> choices = new LinkedHashSet<>();
				for (int i = 0; i < propertyDefinition.choiceValues().length; i++) {
					choices.add(Choice.builder()
							.value(propertyDefinition.choiceValues()[i])
							.name(propertyDefinition.choiceNames()[i])
							.build());
					if (propertyDefinition.choiceClasses().length == propertyDefinition.choiceValues().length) {
						boolean setToValue = false;
						if (propertyDefinition.choiceGroupNames().length == propertyDefinition.choiceValues().length) {
							template.getGroups().add(Group.builder().id(propertyDefinition.choiceValues()[i]).label(propertyDefinition.choiceGroupNames()[i]).build());
							setToValue = true;
						}
						deferredProperties.addAll(getProperties(propertyDefinition.choiceClasses()[i], template, property.getId(), propertyDefinition.choiceValues()[i], setToValue));
					}
				}
				property.setChoices(choices);
				property.setType(TYPE.DROPDOWN);
				property.setFeel(null);
			}
			if (!propertyDefinition.conditionPropertyId().isBlank()) {
				Condition condition = new Condition();
				condition.setProperty(propertyDefinition.conditionPropertyId());
				if (!propertyDefinition.conditionEquals().isBlank())
					condition.setEquals(propertyDefinition.conditionEquals());
				if (propertyDefinition.conditionOneOf().length > 0)
					condition.setOneOf(new LinkedHashSet<>(Arrays.asList(propertyDefinition.conditionOneOf())));
				property.setCondition(condition);
			} else if (propertyId != null && propertyValue != null) {
				property.setCondition(Condition.builder().property(propertyId).equals(propertyValue).build());
			}
			properties.add(property);
		}
		properties.addAll(deferredProperties);
		return properties;
	}

}
