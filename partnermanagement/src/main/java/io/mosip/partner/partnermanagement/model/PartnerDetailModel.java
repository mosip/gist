package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import io.mosip.partner.partnermanagement.model.biometricextractors.ExtractorsRequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;

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
    private PartnerTypes partnerDomain;
    private String policyGroup;
    private String policyName;
    public ExtractorsRequestData extractorList;
}
