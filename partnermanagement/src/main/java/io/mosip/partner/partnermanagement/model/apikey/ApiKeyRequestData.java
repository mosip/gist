package io.mosip.partner.partnermanagement.model.apikey;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApiKeyRequestData {
    private String policyName;
    private String useCaseDescription;
}
