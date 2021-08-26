package io.mosip.partner.management.demo.partnermanagementdemo.service.impl;

import io.mosip.partner.management.demo.partnermanagementdemo.consent.PartnerManagementConstants;
import io.mosip.partner.management.demo.partnermanagementdemo.dto.PartnerDetailModel;
import io.mosip.partner.management.demo.partnermanagementdemo.dto.ResponseModel;
import io.mosip.partner.management.demo.partnermanagementdemo.service.PartnerCreationService;
import org.springframework.stereotype.Service;

@Service
public class PartnerCreationServiceImpl implements PartnerCreationService {

    @Override
    public ResponseModel createPartner(PartnerDetailModel partnerModel) {

        return new ResponseModel(PartnerManagementConstants.SUCCESS);
    }
}
