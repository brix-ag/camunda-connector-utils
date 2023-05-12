package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;

public enum FEEL {
    @SerializedName("") NO, // this value is just required to override the default value and never directly serialized
    @SerializedName("optional") OPTIONAL,
    @SerializedName("required") REQUIRED
}
