package com.acme.camunda.connector.acmeSessionConnector.grouped;

import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import ch.brix.camunda.connector.util.templateGenerator.schema.FEEL;
import lombok.Data;

@Data
public class LogoutGroup {

    @PropertyDefinition(
            label = "Token",
            feel = FEEL.REQUIRED,
            notEmpty = true
    )
    private String token;
}
