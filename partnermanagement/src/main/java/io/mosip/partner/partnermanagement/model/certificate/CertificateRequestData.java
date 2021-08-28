package io.mosip.partner.partnermanagement.model.certificate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CertificateRequestData {
    private String certificateData;
    private String partnerDomain;
}
