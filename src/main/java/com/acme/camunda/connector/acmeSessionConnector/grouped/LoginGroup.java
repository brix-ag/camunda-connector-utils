package com.acme.camunda.connector.acmeSessionConnector.grouped;

import ch.brix.camunda.connector.util.templateGenerator.DefaultTexts;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import lombok.Data;

@Data
public class LoginGroup {

    @PropertyDefinition(
            label = "Username",
            notEmpty = true
    )
    private String username;

    @PropertyDefinition(
            label = "Password",
            description = DefaultTexts.SECRETS_SUPPORTED,
            notEmpty = true
    )
    //@Secret (dependency missing here, but all fields that support secrets have to be marked with this annotation)
    private String password;
}
