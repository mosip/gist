package io.mosip.partner.partnermanagement.model.policy;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PolicyMappingResponseData {
    private String mappingkey;
    private String message;
}
