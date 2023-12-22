package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Property {
    private String id;
    private String label;
    private String description;
    private String tooltip;
    @SerializedName("group") private String groupId; // to distinguish from group name
    private TYPE type;
    private FEEL feel;
    private Binding binding;
    private Boolean optional;
    private String value;
    private Collection<Choice> choices;
    private Constraints constraints;
    private Condition condition;
    private Boolean editable;
}
