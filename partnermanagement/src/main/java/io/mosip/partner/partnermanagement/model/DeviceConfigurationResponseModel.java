package io.mosip.partner.partnermanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DeviceConfigurationResponseModel {
    List<DeviceL1ResponseModel> response;
}
