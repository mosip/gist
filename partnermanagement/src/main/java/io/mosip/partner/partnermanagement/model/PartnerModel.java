package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.device.DeviceModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class PartnerModel {
    private String partnerId;
    private String partnerEmailId;
    private String partnerAddress;
    private String partnerContactNumber;
    private String partnerOrganizationName;
    @ApiModelProperty(name = "partnerType", value = "DEVICE, AUTH, CREDENTIAL")
    private PartnerTypes partnerType;
    private String policyGroup;
    private CertificateChainResponseDto certificateDetails;
    private List<DeviceModel> deviceDetails;

}
