package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;

public enum BINDING_TYPE {
    @SerializedName("zeebe:input") ZEEBE_INPUT,
    @SerializedName("zeebe:output") ZEEBE_OUTPUT,
    @SerializedName("zeebe:property") ZEEBE_PROPERTY,
    @SerializedName("zeebe:taskHeader") ZEEBE_TASK_HEADER,
    @SerializedName("zeebe:taskDefinition:type") ZEEBE_TASK_DEFINITION_TYPE
}
