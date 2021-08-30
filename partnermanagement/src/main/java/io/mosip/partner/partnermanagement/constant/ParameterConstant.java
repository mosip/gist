package io.mosip.partner.partnermanagement.constant;

public enum ParameterConstant {
    AUTHENTICATION_APPID("mosip.authenticate.appid"),
    AUTHENTICATION_USERID("mosip.authenticate.user"),
    AUTHENTICATION_PASSWORD("mosip.authenticate.password"),
    AUTHENTICATION_API_URL("mosip.authenticate.api"),
    AUTHENTICATION_REQUEST_ID("mosip.authenticate.request.id"),
    AUTHENTICATION_VERSION_ID("mosip.authenticate.request.version"),
    PARTNER_APPID("mosip.partner.api"),
    PARTNER_CA_CERTIFICATE_UPLOAD("mosip.ca.certificate.upload.api"),
    PARTNER_CERTIFICATE_UPLOAD("mosip.partner.certificate.upload.api"),
    PARTNER_API_REQUEST("mosip.partner.api.request.api"),
    PARTNER_API_APROVE_REQUEST("mosip.partner.api.approve.api"),
    PARTNER_ADD_BIOEXTRACT_REQUEST("mosip.partner.add.bioextract.api");


    private final String value;

    private ParameterConstant(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
