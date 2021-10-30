package io.mosip.partner.partnermanagement.model.biometricextractors;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class ExtractorsRequestData {
    public List<Extractors> extractors = new ArrayList<>();

    public ExtractorsRequestData() {
        extractors.add(new Extractors("photo", "face"));
        extractors.add(new Extractors("iris", "iris"));
        extractors.add(new Extractors("fingerprint", "finger"));
    }
}
