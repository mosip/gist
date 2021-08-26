package io.mosip.partner.management.demo.partnermanagementdemo.service;

import io.mosip.partner.management.demo.partnermanagementdemo.dto.PartnerDetailModel;
import io.mosip.partner.management.demo.partnermanagementdemo.dto.ResponseModel;

public interface PartnerCreationService {
    public ResponseModel createPartner(PartnerDetailModel partnerModel);
}
