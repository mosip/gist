package io.mosip.partner.partnermanagement.controller;

import io.mosip.partner.partnermanagement.constant.LoggerFileConstant;
import io.mosip.partner.partnermanagement.constant.LoginType;
import io.mosip.partner.partnermanagement.constant.ParameterConstant;
import io.mosip.partner.partnermanagement.model.PartnerDetailModel;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.logger.PartnerManagementLogger;
import io.mosip.partner.partnermanagement.model.authmodel.LoginUser;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;
import io.mosip.partner.partnermanagement.model.restapi.Metadata;
import io.mosip.partner.partnermanagement.service.PartnerCreationService;
import io.mosip.partner.partnermanagement.util.DateUtils;
import io.mosip.partner.partnermanagement.util.RestApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class PartnerCreationController {

    Logger logger = PartnerManagementLogger.getLogger(PartnerCreationController.class);

    @Autowired
    PartnerCreationService partnerCreationService;

    @Autowired
    Environment env;

    @Autowired
    RestApiClient restApiClient;

    @PostMapping(value = "/createPartner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createPartner(@RequestBody PartnerDetailModel partnerDetailModel) {
        // Authentication with Auth Manager using User Id & Password
        logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.APPLICATIONID.toString(), LoggerFileConstant.APPLICATIONID.toString(),  "Authenticate With AuthService");
        RequestWrapper<LoginUser> requestWrapper = new RequestWrapper<>();
        LoginUser request = new LoginUser(env.getProperty(ParameterConstant.AUTHENTICATION_USERID.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_PASSWORD.toString()),
                        env.getProperty(ParameterConstant.AUTHENTICATION_APPID.toString()));
        requestWrapper.setRequest(request);
        requestWrapper.setRequesttime(DateUtils.getUTCCurrentDateTimeString());
        requestWrapper.setId(env.getProperty(ParameterConstant.AUTHENTICATION_REQUEST_ID.toString()));
        requestWrapper.setVersion(env.getProperty(ParameterConstant.AUTHENTICATION_VERSION_ID.toString()));
        requestWrapper.setMetadata(new Metadata());
        restApiClient.setLoginRequestData(LoginType.LOGIN_BY_USERID, requestWrapper);





        ResponseModel responseModel = partnerCreationService.createPartner(partnerDetailModel);
        return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
    }
}
