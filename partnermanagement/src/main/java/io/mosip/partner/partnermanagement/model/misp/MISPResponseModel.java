package io.mosip.partner.partnermanagement.model.misp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MISPResponseModel {
    private String providerId;
    private String licenseKey;
    private String licenseKeyStatus;
    private String licenseKeyExpiry;
}
