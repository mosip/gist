package io.mosip.partner.partnermanagement.model.certificate;
import lombok.Data;

@Data
public class CertificateChainResponseDto {
    String caCertificate;
    String interCertificate;
    String partnerCertificate;
}
