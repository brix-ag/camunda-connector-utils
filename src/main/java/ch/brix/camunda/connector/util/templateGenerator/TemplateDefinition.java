package ch.brix.camunda.connector.util.templateGenerator;

import ch.brix.camunda.connector.util.templateGenerator.schema.BINDING_TYPE;
import ch.brix.camunda.connector.util.templateGenerator.schema.BPMN_TYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the class in which the properties are specified and global settings.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TemplateDefinition {
    String schema() default "https://unpkg.com/@camunda/zeebe-element-templates-json-schema@0.17.0/resources/schema.json";

    /**
     * @return Human-readable name of the connector, e.g. "ACME Connector"
     */
    String name();

    /**
     * @return The identifier of the connector, e.g. com.acme.camunda.connectors.acmeEventConnector:1
     */
    String id();

    /**
     * @return An optional description of the connector
     */
    String description() default "";
    /**
     * @return version &gt;= 0, or &lt; 0 to omit that field
     */
    int version() default -1;

    /**
     * @return An icon encoded as data:image/svg+xml,...
     */
    String icon() default "";

    /**
     * @return A hyperlink to the connector's documentation
     */
    String documentation() default "";
    String categoryId() default "connectors";
    String categoryName() default "Connectors";
    BPMN_TYPE[] appliesTo() default BPMN_TYPE.TASK;
    BPMN_TYPE elementType() default BPMN_TYPE.SERVICE_TASK;
    /**
     * @return the group ids in the same order as the group labels
     */
    String[] groupIds() default {};
    /**
     * @return the group labels in the same order as the group ids
     */
    String[] groupLabels() default {};
    /**
     * @return booleans to indicate whether the corresponding group is opened by default or not
     */
    boolean[] groupOpenByDefaults() default {};
    /**
     * @return tooltips for the groups in the same order as group ids
     */
    String[] groupTooltips() default {};
    /**
     * @return task definition type (is added automatically as hidden property)
     */
    BINDING_TYPE taskDefinitionType() default BINDING_TYPE.ZEEBE_TASK_DEFINITION_TYPE;
    /**
     * Adds standard "Result Variable" and "Result Expression" fields.
     * If there is at least one group defined the group "Output Mapping" (ID: "output") is created if it doesn't exist
     * and the fields are added to this group, otherwise they are added to no group.
     * @return true to add the default output mapping automatically
     */
    boolean addDefaultOutputMapping() default true;

    String defaultOutputMappingTooltip() default "";

    String defaultOutputMappingResultExpressionDescription() default "Expression to handle the result. Details in the <a href=\"https://docs.camunda.io/docs/components/connectors/use-connectors/\" target=\"_blank\">documentation</a>.";

    /**
     * Adds standard "Error Expression" field.
     * If there is at least one group defined the group "Error Handling" (ID: "errors") is created if it doesn't exist
     * and the field is added to this group, otherwise it is added to no group.
     * @return true to add the default error handling automatically
     */
    boolean addDefaultErrorHandling() default true;

    String defaultErrorHandlingTooltip() default "";

    String defaultErrorHandlingExpressionDescription() default "Expression to handle errors. Details in the <a href=\"https://docs.camunda.io/docs/components/connectors/use-connectors/\" target=\"_blank\">documentation</a>.";
}
