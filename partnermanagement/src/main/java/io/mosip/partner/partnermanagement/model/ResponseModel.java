package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.constant.LoggerFileConstant;
import io.mosip.partner.partnermanagement.constant.PartnerManagementConstants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseModel {
    private LoggerFileConstant status;
    private String statusCode;
    private String message;
    private Object responseData;

    public ResponseModel(PartnerManagementConstants constant) {
        this.status = constant.getErrorStatus();
        this.statusCode = constant.getErrorCode();
        this.message = constant.getErrorMessage();
    }

    public ResponseModel(LoggerFileConstant status, String statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }
}
