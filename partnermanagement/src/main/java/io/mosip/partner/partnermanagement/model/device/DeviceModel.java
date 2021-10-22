package io.mosip.partner.partnermanagement.model.device;

import io.mosip.partner.partnermanagement.constant.DeviceTypes;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricsModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DeviceModel {
    DeviceTypes deviceType;
    String make;
    String model;
    @ApiModelProperty(required = false, hidden = true)
    private SecureBiometricsModel secureBiometricsModel;
}
