package ch.brix.camunda.connector.util.templateGenerator.schema;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ElementType {
    private BPMN_TYPE value;
}
