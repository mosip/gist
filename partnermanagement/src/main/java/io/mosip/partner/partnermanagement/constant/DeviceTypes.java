package io.mosip.partner.partnermanagement.constant;

import lombok.Getter;

@Getter
public enum DeviceTypes {
    IRIS_SINGLE("Iris", "Single"),
    FINGERPRINT_SLAP("Finger", "Slab"),
    IRIS_DUAL("Iris", "Double"),
    FACE_FACE("Face", "Full face");

    private String deviceTypeCode;
    private String deviceSubTypeCode;

    private DeviceTypes(String deviceTypeCode, String deviceSubTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
        this.deviceSubTypeCode = deviceSubTypeCode;
    }

}
