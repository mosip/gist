package io.mosip.partner.partnermanagement.model.apikey;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApiKeyRequestLTSData {
    private String policyName;
    private String label;
}
