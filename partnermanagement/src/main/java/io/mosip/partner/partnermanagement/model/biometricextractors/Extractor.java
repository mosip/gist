package io.mosip.partner.partnermanagement.model.biometricextractors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Extractor {
    private String provider = "mock";
    private String version = "1.1";
}
