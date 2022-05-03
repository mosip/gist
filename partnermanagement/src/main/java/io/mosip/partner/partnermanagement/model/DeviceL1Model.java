package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.constant.APITypes;
import io.mosip.partner.partnermanagement.constant.ConfigurationTypes;
import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import io.mosip.partner.partnermanagement.model.apikey.ApiApproveReponseData;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.device.DeviceModel;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricsModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DeviceL1Model {
    @ApiModelProperty(name = "configurationType", value = "MOCK, DEVICE")
    private ConfigurationTypes configurationType;
    private APITypes environmentVersion;
    private PartnerModel deviceProvider;
    private PartnerModel ftmProvider;
}
