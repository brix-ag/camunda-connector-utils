package ch.brix.camunda.connector.util.choiceEnums;

import lombok.Getter;

public enum YesNo {

    YES("Yes"), NO("No");

    @Getter
    private final String choiceName;

    YesNo(String label) {
        this.choiceName = label;
    }

    public boolean getAsBoolean() {
        return this == YES;
    }

}
