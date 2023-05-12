package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;

public enum BPMN_TYPE {
    @SerializedName("bpmn:Activity") ACTIVITY,
    @SerializedName("bpmn:Event") EVENT,
    @SerializedName("bpmn:Gateway") GATEWAY,
    @SerializedName("bpmn:Process") PROCESS,
    @SerializedName("bpmn:SequenceFlow") SEQUENCE_FLOW,
    @SerializedName("bpmn:ServiceTask") SERVICE_TASK,
    @SerializedName("bpmn:StartEvent") START_EVENT,
    @SerializedName("bpmn:Task") TASK
}
