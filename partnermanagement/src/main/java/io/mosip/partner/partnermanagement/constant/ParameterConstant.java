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
    PARTNER_API_REQUEST_FOR_LTS("mosip.partner.api.request.lts.api"),
    PARTNER_API_APROVE_REQUEST("mosip.partner.api.approve.api"),
    PARTNER_ADD_BIOEXTRACT_REQUEST("mosip.partner.add.bioextract.api"),
    DEVICE_DETAIL_ADD_REQUEST("mosip.device.add.api"),
    DEVICE_ACTIVATE_REQUEST("mosip.device.activate.api"),
    SECURE_BIOMETRIC_ADD_REQUEST("mosip.secure.biometric.add.api"),
    SECURE_BIOMETRIC_ACTIVATE_REQUEST("mosip.secure.biometric.activate.api"),
    FETCH_KEY_MANAGER_CERTIFICATE("mosip.key.manager.certificate.api"),
    PARTNER_IDA_CA_CERTIFICATE_UPLOAD("mosip.ida.ca.certificate.upload.api"),
    GENERATE_MISP_LICENSE_KEY("mosip.partner.generate.misp.license"),
    PARTNER_POLICY_MAPPING_REQUEST("mosip.partner.policy.map.api"),
    PARTNER_POLICY_MAPPING_APPROVAL("mosip.partner.policy.map.approval.api"),
    AUTHENTICATION_CLIENT_ID("mosip.authenticate.client.id"),
    AUTHENTICATION_CLIENT_SECRET("mosip.authenticate.client.secretkey");


    private final String value;

    private ParameterConstant(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
