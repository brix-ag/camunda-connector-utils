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
     * @return group name to create the group on the fly
     */
    String groupName() default "";

    /**
     * @return a tooltip for the group if they are created on the fly otherwise set tooltips for groups in @TemplateDefinition
     */
    String groupTooltip() default "";

    /**
     * @return the default property for the condition, if left empty inherited from parent
     */
    String conditionPropertyId() default "";

    /**
     * @return the default condition value, if there is only one value it is converted to a conditionEquals, and if it is left empty it is inherited
     */
    String[] conditionOneOf() default {};

    /**
     * @return the default condition value for isActive, if true conditionOneOf is ignored
     */
    boolean conditionIsActive() default false;

    /**
     * @return false for this group to be initially closed and true (default) for it to be initially opened
     */
    boolean openByDefault() default true;
}
