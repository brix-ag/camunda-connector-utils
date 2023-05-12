package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;

public enum TYPE {
    @SerializedName("String") STRING,
    @SerializedName("Text") TEXT,
    @SerializedName("Dropdown") DROPDOWN,
    /**
     * BOOLEAN can only be used for property binding currently, use DROPDOWN with Yes/No or true/false.
     */
    @SerializedName("Boolean") BOOLEAN,
    @SerializedName("Hidden") HIDDEN
}
