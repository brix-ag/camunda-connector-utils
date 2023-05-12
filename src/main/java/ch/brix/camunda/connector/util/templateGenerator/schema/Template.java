package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Template {

    @SerializedName("$schema") private String schema;
    private String name;
    private String id;
    private Integer version;
    private String description;
    private String documentationRef;
    private Icon icon;
    private Category category;
    private Collection<BPMN_TYPE> appliesTo;
    private ElementType elementType;
    private Collection<Group> groups;
    private Collection<Property> properties;

}
