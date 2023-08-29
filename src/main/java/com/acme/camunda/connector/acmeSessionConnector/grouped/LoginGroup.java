package com.acme.camunda.connector.acmeSessionConnector.grouped;

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
            notEmpty = true
    )
    private String password;
}
