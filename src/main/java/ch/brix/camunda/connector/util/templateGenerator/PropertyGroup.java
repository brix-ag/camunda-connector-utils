package ch.brix.camunda.connector.util.templateGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To mark fields on the template class which contain properties.
 * This allows to split the properties up however we want to,
 * although splitting by group is most convenient and the intended way for this annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertyGroup {
    /**
     * @return the default group id for the properties in the field's class, if left empty it is inherited from parent
     */
    String groupId() default "";

    /**
     * @return the default property for the condition, if left empty inherited from parent
     */
    String conditionPropertyId() default "";

    /**
     * @return the default condition value, if there is only one value it is converted to a conditionEquals, and if it is left empty it is inherited
     */
    String[] conditionOneOf() default {};
}
