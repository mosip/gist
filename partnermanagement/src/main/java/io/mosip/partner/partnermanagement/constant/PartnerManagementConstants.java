package io.mosip.partner.partnermanagement.constant;

import lombok.Getter;

@Getter
public enum PartnerManagementConstants {
    SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-001", "Partner Creation Successful"),
    FAIL(LoggerFileConstant.FAIL, "PMD-FAL-001", "Partner Creation Un-Successful"),
    CERTIFICATE_GENERATED(LoggerFileConstant.SUCCESS, "PMD-SUC-002", "Certificate Generation Successful"),
    CERTIFICATE_GENERATION_FAIL(LoggerFileConstant.FAIL, "PMD-FAL-002", "Certificate Generation Un-Successful");

    public final LoggerFileConstant errorStatus;
    public final String errorCode;
    public final String errorMessage;

    PartnerManagementConstants(final LoggerFileConstant errorStatus, final String errorCode, final String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }


}
