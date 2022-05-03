package io.mosip.partner.partnermanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PartnerDetailResponseModel {
    private String partnerId;
    private String policyMappingKey;
    private String partnerApiKey;
    private String partnerMISPLicenseKey;
    private String signedCertificate;
    private List<Object> errors;
}
