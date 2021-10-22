package io.mosip.partner.partnermanagement.model.device;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DeviceActivateRequestDto {
    private String approvalStatus;
    private String id;
    private String isItForRegistrationDevice;
}
