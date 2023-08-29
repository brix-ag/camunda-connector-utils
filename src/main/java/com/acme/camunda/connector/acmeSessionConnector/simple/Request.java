package com.acme.camunda.connector.acmeSessionConnector.simple;

import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import ch.brix.camunda.connector.util.templateGenerator.TemplateDefinition;
import ch.brix.camunda.connector.util.templateGenerator.schema.FEEL;
import lombok.Data;

@Data
@TemplateDefinition(
        name = "ACME Session Connector",
        id = "com.acme.camunda.connectors.acmeEventConnector:1",
        version = 1,
        icon = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='250' height='250' viewBox='0 0 192.756 192.756'%3E%3Cpath fill-rule='evenodd' clip-rule='evenodd' fill='%23fff' d='M0 0h192.756v192.756H0V0z'/%3E%3Cpath fill-rule='evenodd' clip-rule='evenodd' fill='%23255398' d='M9.602 141.23h173.552V51.527H9.602v89.703z'/%3E%3Cpath fill='none' stroke='%23255398' stroke-width='1.784' stroke-miterlimit='2.613' d='M9.602 51.527h173.552v89.703H9.602V51.527z'/%3E%3Cpath fill-rule='evenodd' clip-rule='evenodd' fill='%23255398' d='M12.305 138.137h168.146V54.074H12.305v84.063z'/%3E%3Cpath fill='none' stroke='%23fff' stroke-width='2.673' stroke-miterlimit='2.613' d='M12.665 54.802h167.427v83.153H12.665V54.802z'/%3E%3Cpath d='M95.567 129.584c40.369 0 73.89-15.283 73.89-33.297 0-17.831-33.521-33.115-73.89-33.115-40.368 0-72.088 15.284-72.088 33.115 0 18.014 31.72 33.297 72.088 33.297z' fill-rule='evenodd' clip-rule='evenodd' fill='%23fff' stroke='%23fff' stroke-width='2.673' stroke-miterlimit='2.613'/%3E%3Cpath d='M95.567 129.584c40.369 0 73.89-15.283 73.89-33.297 0-17.831-33.521-33.116-73.89-33.116-40.368 0-72.088 15.285-72.088 33.116 0 18.014 31.72 33.297 72.088 33.297z' fill-rule='evenodd' clip-rule='evenodd' fill='%23cf4044' stroke='%23fff' stroke-width='1.425' stroke-miterlimit='2.613'/%3E%3Cpath d='M95.388 125.035c36.405 0 69.745-12.555 69.745-28.748 0-16.011-33.34-28.749-69.745-28.749-36.226 0-67.764 12.738-67.764 28.749 0 16.193 31.539 28.748 67.764 28.748z' fill='none' stroke='%23fff' stroke-width='1.784' stroke-miterlimit='2.613'/%3E%3Cpath d='M67.813 108.842l-.54-4.004h-6.309l-1.442 4.004h-6.127l9.731-27.657h7.75l3.064 27.657h-6.127zm-1.983-17.65l-.359.365-2.703 7.643h3.785l-.723-8.008zM90.16 108.842h-3.965c-5.767.182-12.254-4.184-11.353-13.647 1.261-12.372 10.812-14.01 14.237-14.01h4.505l-.9 6.55h-4.145c-3.786 0-6.128 4.185-6.489 7.277-.361 3.277.721 7.281 4.325 7.281h4.686l-.901 6.549zM109.625 99.199l-4.506 9.643h-2.523l-1.983-9.826-.361-.364-1.08 10.19h-5.767l3.244-27.657h6.126l2.705 12.737 6.127-12.737h6.127l-3.244 27.657h-5.767l1.261-10.19-.359.547zM131.43 87.735h4.506l.723-6.55h-4.146c-3.424 0-12.975 1.638-14.236 14.01-1.082 9.463 6.127 13.647 11.895 13.647 1.803 0 1.623.182 3.244 0l.721-6.549h-3.965c-2.344 0-4.867-2.184-5.047-4.369h9.553l.9-6.367h-9.73c.896-2.002 3.24-3.822 5.582-3.822zM141.244 103.373c.797 0 1.574.205 2.334.617a4.37 4.37 0 0 1 1.771 1.773c.424.768.637 1.568.637 2.402 0 .826-.209 1.619-.627 2.381a4.409 4.409 0 0 1-1.754 1.773c-.754.424-1.539.633-2.361.633s-1.609-.209-2.361-.633a4.43 4.43 0 0 1-1.758-1.773 4.864 4.864 0 0 1-.629-2.381c0-.834.213-1.635.639-2.402a4.398 4.398 0 0 1 1.775-1.773c.758-.412 1.537-.617 2.334-.617zm0 .793a4.02 4.02 0 0 0-1.947.518 3.715 3.715 0 0 0-1.48 1.479 4.084 4.084 0 0 0-.535 2.004c0 .689.176 1.352.527 1.982.35.633.838 1.127 1.469 1.48a3.946 3.946 0 0 0 1.967.531c.682 0 1.34-.178 1.969-.531s1.115-.848 1.463-1.48a4.05 4.05 0 0 0 .521-1.982c0-.695-.176-1.363-.529-2.004a3.696 3.696 0 0 0-1.48-1.479c-.636-.344-1.285-.518-1.945-.518zm-2.082 6.646v-5.152h1.754c.602 0 1.035.045 1.303.141s.48.262.641.498a1.374 1.374 0 0 1-.16 1.74c-.266.279-.621.436-1.061.471.182.074.326.166.434.271.207.205.459.545.756 1.023l.623 1.008h-1.006l-.456-.812c-.355-.637-.641-1.039-.859-1.199-.15-.119-.371-.178-.66-.178h-.484v2.189h-.825zm.824-2.9h1c.477 0 .803-.072.977-.215a.707.707 0 0 0 .262-.572.704.704 0 0 0-.127-.41.747.747 0 0 0-.348-.27c-.148-.061-.424-.088-.828-.088h-.936v1.555z' fill-rule='evenodd' clip-rule='evenodd' fill='%23fff'/%3E%3C/svg%3E",
        groupIds =    {"action", "data"},
        groupLabels = {"Action", "Data"}
)
public class Request {

    @PropertyDefinition(
            label = "Action",
            groupId = "action",
            notEmpty = true,
            choiceValues = {"login", "logout"},
            choiceNames = {"Log in", "Log out"},
            value = "login"
    )
    private String action;

    @PropertyDefinition(
            label = "Username",
            groupId = "data",
            notEmpty = true,
            conditionPropertyId = "action",
            conditionEquals = "login"
    )
    private String username;

    @PropertyDefinition(
            label = "Password",
            groupId = "data",
            notEmpty = true,
            conditionPropertyId = "action",
            conditionEquals = "login"
    )
    private String password;

    @PropertyDefinition(
            label = "Token",
            groupId = "data",
            feel = FEEL.REQUIRED,
            notEmpty = true,
            conditionPropertyId = "action",
            conditionEquals = "logout"
    )
    private String token;

}