package io.mosip.partner.partnermanagement.model;

import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.device.DeviceModel;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricsModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DeviceL1Model {
    private PartnerModel deviceProvider;
    private PartnerModel ftmProvider;
}
