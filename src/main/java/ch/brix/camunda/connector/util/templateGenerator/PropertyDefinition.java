package ch.brix.camunda.connector.util.templateGenerator;

import ch.brix.camunda.connector.util.templateGenerator.schema.BINDING_TYPE;
import ch.brix.camunda.connector.util.templateGenerator.schema.FEEL;
import ch.brix.camunda.connector.util.templateGenerator.schema.TYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a property to be added to the template.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertyDefinition {

    /**
     * Constant to take the value from the field name
     */
    String FIELD = "<field>";

    /**
     * The id is required for conditions. The field name is taken as default (can be overwritten by @SerializedName),
     * so this should work automatically and doesn't have to be set manually.
     * @return an id, empty string to omit the id or PropertyDefinition.FIELD to take the field's name (default)
     */
    String id() default FIELD;
    /**
     * @return Human-readable label for the property, e.g. "Username"
     */
    String label();

    /**
     * @return An optional description for the property
     */
    String description() default "";
    /**
     * @return An optional tooltip for the property
     */
    String tooltip() default "";

    String groupId() default "";

    /**
     * @return The input type of the property, see @{@link TYPE}
     */
    TYPE type() default TYPE.STRING;

    /**
     * @return Whether FEEL is available or required, see @{@link FEEL}
     */
    FEEL feel() default FEEL.OPTIONAL;

    /**
     * @return The binding type regarding zeebe, see @{@link BINDING_TYPE}
     */
    BINDING_TYPE bindingType() default BINDING_TYPE.ZEEBE_INPUT;
    /**
     * @return A binding name, empty string to omit the value or PropertyDefinition.FIELD (default) to take the field's name (WARNING: always use this in combination with @SerializeName to change the binding name if the property is intended to be deserialized by the Deserializer)
     */
    String bindingName() default FIELD;
    String bindingKey() default "";
    boolean notEmpty() default false;
    int maxLength() default -1;
    int minLength() default -1;
    String pattern() default "";
    String patternMessage() default "";
    boolean optional() default false;
    boolean editable() default true;
    String value() default "";

    /**
     * @return the choice values in the same order as the choice names
     */
    String[] choiceValues() default {};

    /**
     * @return the choice names in the same order as the choice values
     */
    String[] choiceNames() default {};

    /**
     * @return a property class for each choice, if choiceClasses is specified then all the annotated properties
     * in the class are imported, but the condition is set so that the property is only visible when the corresponding
     * choice was selected. Requires a setter for the corresponding class in the property class to deserialize.
     * Its name has to start with "set*". This is useful for an "Action" or "Method" dropdown, so the different actions
     * can be separated. If this feature is used it is recommended to prefix the properties to avoid naming conflicts.
     */
    Class<?>[] choiceClasses() default {};

    /**
     * @return the names (labels) of the choice groups (the choice value is taken as group id unless choiceGroupIds is specified).
     * This only works in combination with choiceClasses. If choiceGroupNames is set
     * all the properties in the corresponding class are put in this group by default.
     */
    String[] choiceGroupNames() default {};
    /**
     * @return the default group id for all the properties in the corresponding choice class.
     * If choiceGroupNames is empty then the group has to exist in the TemplateDefinition.
     */
    String[] choiceGroupIds() default {};
    /**
     * @return the tooltips for the choice groups (only respected if choiceGroupNames is set)
     */
    String[] choiceGroupTooltips() default {};
    /**
     * @return whether the corresponding group is open by default (only respected if choiceGroupNames is set)
     */
    boolean[] choiceGroupOpenByDefaults() default {};

    /**
     * Instead of using the other choice* properties everything can be set using an enum (recommended).
     * @return the enum with the choices:<ul>
     *     <li>choiceValue: taken from the @SerializedName annotation and if not present the enum constant name is used as value (so deserialization should work without problems)
     *     <li>choiceName: if there is a function <em>String getChoiceName()</em> this will be taken otherwise the result of <em>String toString()</em></li>
     *     <li>choiceClass: if there is a function <em>Class getChoiceClass()</em> it will be used as choice class and all its properties loaded</li>
     *     <li>choiceGroupId: if there is a function <em>String getChoiceGroupId</em> it will be taken as default group for all properties in the corresponding choice class</li>
     * </ul>getChoiceGroupId() this will be the default group id for the
     * properties in the corresponding choice class.
     */
    Class<? extends Enum> choiceEnum() default Null.class;

    /**
     * @return the property id (not binding) on which the condition is based,
     * requires exactly one of conditionEquals or conditionOneOf
     */
    String conditionPropertyId() default "";
    String conditionEquals() default "";
    String[] conditionOneOf() default {};
    // This makes absolutely no sense, there is no multi-value property type like dropdown with multi-selection.
    // String[] conditionAllOf() default {};

    /**
     * @return true to display the property if the property given in the condition is active, if true conditionOneOf and conditionEquals are ignored
     */
    boolean conditionIsActive() default false;

    enum Null {}
}
