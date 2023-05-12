package ch.brix.camunda.connector.util.templateGenerator.schema;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Binding {
    private BINDING_TYPE type;
    private String key;
    private String name;
}
