package io.mosip.partner.partnermanagement.model.certificate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PartnerCertificateRequestData {
    private String certificateData;
    private String partnerDomain;
    private String partnerId;
}
