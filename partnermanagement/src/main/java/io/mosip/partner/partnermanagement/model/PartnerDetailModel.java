package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PartnerDetailModel {
    private String partnerId;
    private String partnerEmailId;
    private String partnerAddress;
    private String partnerContactNumber;
    private String partnerOrganizationName;
    private String partnerType;
    private String policyGroup;
    private PartnerTypes partnerDomain;
}
