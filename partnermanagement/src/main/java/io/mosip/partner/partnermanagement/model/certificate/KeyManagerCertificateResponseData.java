package io.mosip.partner.partnermanagement.model.certificate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class KeyManagerCertificateResponseData {
    String certificate;
    String certSignRequest;
    String issuedAt;
    String expiryAt;
    String timestamp;
}
