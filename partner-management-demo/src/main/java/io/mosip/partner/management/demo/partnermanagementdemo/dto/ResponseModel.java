package io.mosip.partner.management.demo.partnermanagementdemo.dto;

import io.mosip.partner.management.demo.partnermanagementdemo.consent.PartnerManagementConstants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseModel {
    private String status;
    private String statusCode;
    private String message;

    public ResponseModel(PartnerManagementConstants constant) {
        this.status = constant.getErrorStatus().toString();
        this.statusCode = constant.getErrorCode();
        this.message = constant.getErrorMessage();
    }

    public ResponseModel(String status, String statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }
}
