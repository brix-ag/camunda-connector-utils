package ch.brix.camunda.connector.util.templateGenerator.schema;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Constraints {
    private Boolean notEmpty;
    private Integer minLength;
    private Integer maxLength;
    private Pattern pattern;
}
