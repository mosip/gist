package io.mosip.partner.partnermanagement.controller;

import io.mosip.partner.partnermanagement.constant.LoggerFileConstant;
import io.mosip.partner.partnermanagement.constant.LoginType;
import io.mosip.partner.partnermanagement.constant.ParameterConstant;
import io.mosip.partner.partnermanagement.model.PartnerDetailModel;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.logger.PartnerManagementLogger;
import io.mosip.partner.partnermanagement.model.apikey.ApiApproveRequestData;
import io.mosip.partner.partnermanagement.model.apikey.ApiKeyRequestData;
import io.mosip.partner.partnermanagement.model.authmodel.LoginUser;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.certificate.CertificateRequestData;
import io.mosip.partner.partnermanagement.model.certificate.PartnerCertificateRequestData;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;
import io.mosip.partner.partnermanagement.model.http.ResponseWrapper;
import io.mosip.partner.partnermanagement.model.partner.PartnerRequest;
import io.mosip.partner.partnermanagement.model.partner.PartnerResponse;
import io.mosip.partner.partnermanagement.model.restapi.Metadata;
import io.mosip.partner.partnermanagement.service.PartnerCreationService;
import io.mosip.partner.partnermanagement.util.DateUtils;
import io.mosip.partner.partnermanagement.util.KeyMgrUtil;
import io.mosip.partner.partnermanagement.util.RestApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

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
        LoginUser request = new LoginUser(env.getProperty(ParameterConstant.AUTHENTICATION_USERID.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_PASSWORD.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_APPID.toString()));

        RequestWrapper<Object> requestWrapper = createRequestWrapper(request);
        restApiClient.setLoginRequestData(LoginType.LOGIN_BY_USERID, requestWrapper);

        // Creation of Partner from Self Partner Creation request
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.setPartnerId(partnerDetailModel.getPartnerId());
        partnerRequest.setPartnerType(partnerDetailModel.getPartnerType());
        partnerRequest.setAddress(partnerDetailModel.getPartnerAddress());
        partnerRequest.setContactNumber(partnerDetailModel.getPartnerContactNumber());
        partnerRequest.setEmailId(partnerDetailModel.getPartnerEmailId());
        partnerRequest.setOrganizationName(partnerDetailModel.getPartnerOrganizationName());
        partnerRequest.setPolicyGroup(partnerDetailModel.getPolicyGroup());

        RequestWrapper<Object> partnerRequestWrapper = createRequestWrapper(partnerRequest);
        ResponseModel responseModel = partnerCreationService.createPartner(partnerRequestWrapper);

        if (responseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.EXPECTATION_FAILED);

        // Generating CA, SUB-CA, Partner Certificate
        ResponseWrapper<LinkedHashMap> responseWrapper = (ResponseWrapper<LinkedHashMap>) responseModel.getResponseData();
        LinkedHashMap partnerResponse = responseWrapper.getResponse();
        String partnerId = partnerResponse.get("partnerId").toString();
        String filePrepend = partnerDetailModel.getPartnerDomain().getFilePrepend();
        ResponseModel certificateResponseModel  = partnerCreationService.generateCertificates(partnerId, filePrepend);

        if (certificateResponseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(certificateResponseModel, HttpStatus.EXPECTATION_FAILED);

        CertificateChainResponseDto certificateChainResponseDto = (CertificateChainResponseDto) certificateResponseModel.getResponseData();

        // Upload CA Certificates
        CertificateRequestData certificateRequest = new CertificateRequestData();
        certificateRequest.setCertificateData(certificateChainResponseDto.getCaCertificate());
        certificateRequest.setPartnerDomain(partnerDetailModel.getPartnerDomain().toString());
        RequestWrapper<Object> caCertWrapper = createRequestWrapper(certificateRequest);
        ResponseModel caCertUploadResponse = partnerCreationService.uploadCACertificates(caCertWrapper);

        if (caCertUploadResponse.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(caCertUploadResponse, HttpStatus.EXPECTATION_FAILED);

        // Upload SUB-CA Certificates
        CertificateRequestData subCertificateRequest = new CertificateRequestData();
        subCertificateRequest.setCertificateData(certificateChainResponseDto.getInterCertificate());
        subCertificateRequest.setPartnerDomain(partnerDetailModel.getPartnerDomain().toString());
        RequestWrapper<Object> subCaCertWrapper = createRequestWrapper(subCertificateRequest);
        ResponseModel subCaCertUploadResponse = partnerCreationService.uploadCACertificates(subCaCertWrapper);

        if (subCaCertUploadResponse.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(subCaCertUploadResponse, HttpStatus.EXPECTATION_FAILED);

        // Upload Partner Certificate
        PartnerCertificateRequestData partnerCertificateRequest = new PartnerCertificateRequestData();
        partnerCertificateRequest.setCertificateData(certificateChainResponseDto.getPartnerCertificate());
        partnerCertificateRequest.setPartnerDomain(partnerDetailModel.getPartnerDomain().toString());
        partnerCertificateRequest.setPartnerId(partnerId);
        RequestWrapper<Object> partnerCertWrapper = createRequestWrapper(partnerCertificateRequest);
        ResponseModel partnerCertUploadResponse = partnerCreationService.uploadPartnerCertificates(partnerCertWrapper);

        if (partnerCertUploadResponse.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(partnerCertUploadResponse, HttpStatus.EXPECTATION_FAILED);


        // Submit PartnerAPI Key request Copy
        ApiKeyRequestData apiRequestData = new ApiKeyRequestData();
        apiRequestData.setPolicyName(partnerDetailModel.getPolicyName());
        apiRequestData.setUseCaseDescription("");
        RequestWrapper<Object> apiRequestWrapper = createRequestWrapper(apiRequestData);
        ResponseModel apiResonseModel = partnerCreationService.partnerApiRequest(apiRequestWrapper, partnerId);

        if (apiResonseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(apiResonseModel, HttpStatus.EXPECTATION_FAILED);

        // Approve PartnerAPI
        ApiApproveRequestData approveRequestData = new ApiApproveRequestData();
        approveRequestData.setStatus("Approved");
        RequestWrapper<Object> apiApproveRequestWrapper = createRequestWrapper(approveRequestData);
        ResponseModel approveResonseModel = partnerCreationService.approvePartnerApiRequest(apiApproveRequestWrapper, partnerId);

        if (approveResonseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(approveResonseModel, HttpStatus.EXPECTATION_FAILED);




        return new ResponseEntity<ResponseModel>(certificateResponseModel, HttpStatus.OK);
    }

    private RequestWrapper<Object> createRequestWrapper(Object request) {
        RequestWrapper<Object> requestWrapper = new RequestWrapper<>();
        requestWrapper.setRequesttime(DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime()));
        requestWrapper.setId(env.getProperty(ParameterConstant.AUTHENTICATION_REQUEST_ID.toString()));
        requestWrapper.setVersion(env.getProperty(ParameterConstant.AUTHENTICATION_VERSION_ID.toString()));
        requestWrapper.setMetadata(new Metadata());
        requestWrapper.setRequest(request);
        return requestWrapper;
    }
}
