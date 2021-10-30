package io.mosip.partner.partnermanagement.model.device;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DeviceRequestDto {
    String deviceProviderId;
    String deviceSubTypeCode;
    String deviceTypeCode;
    String id;
    String isItForRegistrationDevice;
    String make;
    String model;
    String partnerOrganizationName;
}
