package io.mosip.partner.partnermanagement.model.certificate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PartnerCertificateResponseData {
    private String signedCertificateData;
    private String certificateId;
    private String timestamp;
}
