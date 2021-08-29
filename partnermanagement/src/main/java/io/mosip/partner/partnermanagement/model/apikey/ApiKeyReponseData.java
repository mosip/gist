package io.mosip.partner.partnermanagement.model.apikey;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApiKeyReponseData {
    private String apiRequestId;
    private String message;
    private String apikeyId;
}
