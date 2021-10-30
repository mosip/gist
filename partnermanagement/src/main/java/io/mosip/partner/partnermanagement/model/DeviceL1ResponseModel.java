package io.mosip.partner.partnermanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Data
@Getter
@Setter
public class DeviceL1ResponseModel {
    private String partnerId;
    private String deviceId;
    private String secureBiometricId;
    private String signedCertificate;
    private List<Object> errors;
}
