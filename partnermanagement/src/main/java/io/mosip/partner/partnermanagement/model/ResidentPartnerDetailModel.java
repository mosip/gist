package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import io.mosip.partner.partnermanagement.model.biometricextractors.ExtractorsRequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResidentPartnerDetailModel {
    private PartnerModel partnerModel;
    private String policyName;
    public ExtractorsRequestData extractorList;
}
