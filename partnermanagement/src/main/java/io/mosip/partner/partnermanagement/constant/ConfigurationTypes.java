package io.mosip.partner.partnermanagement.constant;

import lombok.Getter;

@Getter
public enum ConfigurationTypes {
    MOCK("MOCK"),
    DEVICE("DEVICE");

    private String configurationType;

    private ConfigurationTypes(String configurationType) {
        this.configurationType = configurationType;
    }

}
