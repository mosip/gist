package io.mosip.partner.partnermanagement.constant;

import lombok.Getter;

@Getter
public enum PartnerManagementConstants {
    PARTNER_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-001", "Partner Creation Successful"),
    PARTNER_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-001", "Partner Creation Un-Successful"),
    CA_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-003", "CA/SUBCA Certificate Upload Success"),
    CA_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-003", "CA/SUBCA Certificate Upload Failed"),
    PC_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-004", "Partner Certificate Upload Success"),
    PC_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-005", "API Key Generation"),
    API_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-005", "API Key Generation Failed"),
    API_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-004", "Partner Certificate Upload Failed"),
    API_APPROVE_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-006", "API Key Approval Process Successful"),
    API_APPROVE_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-006", "API Key Approval Process Failed"),
    BIO_EXTRACT_ADD_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-007", "Bio Extract Addition Successful"),
    BIO_EXTRACT_ADD_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-007", "Bio Extract Addition Failed"),
    CERTIFICATE_GENERATED(LoggerFileConstant.SUCCESS, "PMD-SUC-002", "Certificate Generation Successful"),
    CERTIFICATE_GENERATION_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-002", "Certificate Generation Un-Successful"),
    DEVICE_DETAIL_ADD_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-008", "Device Details Added Successful"),
    DEVICE_DETAIL_ADD_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-008", "Device Details Added Un-Successful"),
    DEVICE_ACTIVATION_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-009", "Device Activation Successful"),
    DEVICE_ACTIVATION_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-009", "Device Activation Un-Successful"),
    SECURE_BIOMETRICS_DETAIL_ADD_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-010", "Secure Biometrics Details Added Successful"),
    SECURE_BIOMETRICS_DETAIL_ADD_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-010", "Secure Biometrics Details Added Un-Successful"),
    SECURE_BIOMETRICS_ACTIVATION_SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-011", "Secure Biometrics Activation Successful"),
    SECURE_BIOMETRICS_ACTIVATION_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-011", "Secure Biometrics Activation Un-Successful");

    public final LoggerFileConstant errorStatus;
    public final String errorCode;
    public final String errorMessage;

    PartnerManagementConstants(final LoggerFileConstant errorStatus, final String errorCode, final String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }


}