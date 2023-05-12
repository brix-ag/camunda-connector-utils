package ch.brix.camunda.connector.util.templateGenerator.schema;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Pattern {
    @SerializedName("value") private String regex; // to show what "value" actually is
    private String message;
}
