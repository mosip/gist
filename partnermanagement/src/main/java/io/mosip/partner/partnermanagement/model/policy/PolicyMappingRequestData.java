package io.mosip.partner.partnermanagement.model.policy;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PolicyMappingRequestData {
    private String policyName;
    private String useCaseDescription;
}
