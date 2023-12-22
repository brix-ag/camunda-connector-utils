package ch.brix.camunda.connector.util.templateGenerator.schema;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group {
    @EqualsAndHashCode.Include
    private String id;
    private String label;
    private String tooltip;
    @Builder.Default
    private boolean openByDefault = true;
}
