package ch.brix.camunda.connector.util.templateGenerator.schema;

import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Condition {
    @EqualsAndHashCode.Include
    private String property;
    private String equals;
    private Collection<String> oneOf;
    private Collection<String> allOf;
    private Boolean isActive;
}
