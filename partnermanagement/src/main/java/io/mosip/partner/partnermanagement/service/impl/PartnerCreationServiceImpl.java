package io.mosip.partner.partnermanagement.service.impl;

import io.mosip.partner.partnermanagement.consent.PartnerManagementConstants;
import io.mosip.partner.partnermanagement.dto.PartnerDetailModel;
import io.mosip.partner.partnermanagement.dto.ResponseModel;
import io.mosip.partner.partnermanagement.service.PartnerCreationService;
import org.springframework.stereotype.Service;

@Service
public class PartnerCreationServiceImpl implements PartnerCreationService {

    @Override
    public ResponseModel createPartner(PartnerDetailModel partnerModel) {

        return new ResponseModel(PartnerManagementConstants.SUCCESS);
    }
}
