package io.mosip.partner.management.demo.partnermanagementdemo.consent;

import lombok.Getter;

@Getter
public enum PartnerManagementConstants {
    SUCCESS(LoggerFileConstant.SUCCESS, "PMD-SUC-001", "Partner Creation Successful");

    public final LoggerFileConstant errorStatus;
    public final String errorCode;
    public final String errorMessage;

    PartnerManagementConstants(final LoggerFileConstant errorStatus, final String errorCode, final String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }


}
