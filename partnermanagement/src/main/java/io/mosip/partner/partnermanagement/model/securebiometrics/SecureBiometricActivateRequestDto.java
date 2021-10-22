package io.mosip.partner.partnermanagement.model.securebiometrics;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SecureBiometricActivateRequestDto {
    private String approvalStatus;
    private String id;
    private String isItForRegistrationDevice;
}
