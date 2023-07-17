package com.acme.camunda.connector.acmeSessionConnector.stateOfTheArt;

import com.acme.camunda.connector.acmeSessionConnector.grouped.LoginGroup;
import com.acme.camunda.connector.acmeSessionConnector.grouped.LogoutGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Action {
    //@SerializedName("login") to change the value from LOG_IN to login
    LOG_IN("Log in", LoginGroup.class, "data"),
    LOG_OUT("Log out", LogoutGroup.class, "data");

    private final String choiceName;
    private final Class<?> choiceClass;
    private final String choiceGroupId;
    // optionally additional fields, e.g. could specify the groups
    // to be used for the validation of this action
}
