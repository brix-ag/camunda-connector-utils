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
     * The id is required for conditions. The field name is taken as default,
     * so this should work automatically and doesn't have to be set manually.
     * @return an id, empty string to omit the id or PropertyDefinition.FIELD to take the field's name (default)
     */
    String id() default FIELD;
    /**
     * @return Human-readable label for the property, e.g. "Username"
     */
    String label();

    /**
     * @return An optional description of the property
     */
    String description() default "";
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
     * @return A binding name, empty string to omit the value or PropertyDefinition.FIELD to take the field's name (default)
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
     * @return the property id (not binding) on which the condition is based,
     * requires exactly one of conditionEquals or conditionOneOf
     */
    String conditionPropertyId() default "";
    String conditionEquals() default "";
    String[] conditionOneOf() default {};
    // This makes absolutely no sense, there is no multi-value property type like dropdown with multi-selection.
    // String[] conditionAllOf() default {};
}
