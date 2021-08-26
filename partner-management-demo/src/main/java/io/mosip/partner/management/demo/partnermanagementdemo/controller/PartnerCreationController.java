package io.mosip.partner.management.demo.partnermanagementdemo.controller;

import io.mosip.partner.management.demo.partnermanagementdemo.dto.PartnerDetailModel;
import io.mosip.partner.management.demo.partnermanagementdemo.dto.ResponseModel;
import io.mosip.partner.management.demo.partnermanagementdemo.service.PartnerCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class PartnerCreationController {

    @Autowired
    PartnerCreationService partnerCreationService;

    @PostMapping(value = "/createPartner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createPartner(@RequestBody PartnerDetailModel partnerDetailModel) {
        ResponseModel responseModel = partnerCreationService.createPartner(partnerDetailModel);
        return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
    }
}
