package io.mosip.partner.partnermanagement.service;

import io.mosip.partner.partnermanagement.model.PartnerDetailModel;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.model.authmodel.LoginUser;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;

public interface PartnerCreationService {
    public ResponseModel createPartner(PartnerDetailModel partnerModel);
}
