package io.mosip.partner.partnermanagement.constant;

public enum ParameterConstant {
    AUTHENTICATION_APPID("mosip.authenticate.appid"),
    AUTHENTICATION_USERID("mosip.authenticate.user"),
    AUTHENTICATION_PASSWORD("mosip.authenticate.password"),
    AUTHENTICATION_API_URL("mosip.authenticate.api"),
    AUTHENTICATION_REQUEST_ID("mosip.authenticate.request.id"),
    AUTHENTICATION_VERSION_ID("mosip.authenticate.request.version");

    private final String value;

    private ParameterConstant(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
