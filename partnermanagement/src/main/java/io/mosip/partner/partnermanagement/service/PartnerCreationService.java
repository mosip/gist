package io.mosip.partner.partnermanagement.service;

import io.mosip.partner.partnermanagement.dto.PartnerDetailModel;
import io.mosip.partner.partnermanagement.dto.ResponseModel;

public interface PartnerCreationService {
    public ResponseModel createPartner(PartnerDetailModel partnerModel);
}
