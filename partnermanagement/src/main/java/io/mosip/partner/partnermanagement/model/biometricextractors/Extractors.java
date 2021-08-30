package io.mosip.partner.partnermanagement.model.biometricextractors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class Extractors {
    private String attributeName;
    private String biometric;
    private Extractor extractor;

    public Extractors(String attributeName, String biometric) {
        this.attributeName = attributeName;
        this.biometric = biometric;
        this.extractor = new Extractor();
    }
}
