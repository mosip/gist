package io.mosip.partner.management.demo.partnermanagementdemo.dto;

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

}
