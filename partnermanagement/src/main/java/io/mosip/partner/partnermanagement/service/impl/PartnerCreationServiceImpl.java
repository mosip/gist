package io.mosip.partner.partnermanagement.service.impl;

import io.mosip.partner.partnermanagement.constant.ParameterConstant;
import io.mosip.partner.partnermanagement.constant.PartnerManagementConstants;
import io.mosip.partner.partnermanagement.logger.PartnerManagementLogger;
import io.mosip.partner.partnermanagement.model.PartnerDetailModel;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.model.authmodel.AuthNResponse;
import io.mosip.partner.partnermanagement.model.authmodel.AuthNResponseDto;
import io.mosip.partner.partnermanagement.model.authmodel.LoginUser;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;
import io.mosip.partner.partnermanagement.model.http.ResponseWrapper;
import io.mosip.partner.partnermanagement.model.partner.PartnerResponse;
import io.mosip.partner.partnermanagement.service.PartnerCreationService;
import io.mosip.partner.partnermanagement.util.RestApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class PartnerCreationServiceImpl implements PartnerCreationService {

    Logger logger = PartnerManagementLogger.getLogger(PartnerCreationServiceImpl.class);

    @Autowired
    Environment env;

    @Autowired
    RestApiClient restApiClient;

    @Override
    public ResponseModel createPartner(Object request) {
        ResponseWrapper<PartnerResponse> response = null;
        ResponseModel responseModel = null;
        try {
            response = restApiClient.postApi(env.getProperty(ParameterConstant.PARTNER_APPID.toString()),
                    MediaType.APPLICATION_JSON, request, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }
}
