package io.mosip.partner.partnermanagement.service.impl;

import io.mosip.partner.partnermanagement.constant.ParameterConstant;
import io.mosip.partner.partnermanagement.constant.PartnerManagementConstants;
import io.mosip.partner.partnermanagement.logger.PartnerManagementLogger;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.model.apikey.ApiApproveReponseData;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.certificate.CertificateResponseData;
import io.mosip.partner.partnermanagement.model.certificate.PartnerCertificateResponseData;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;
import io.mosip.partner.partnermanagement.model.http.ResponseWrapper;
import io.mosip.partner.partnermanagement.model.partner.PartnerResponse;
import io.mosip.partner.partnermanagement.service.PartnerCreationService;
import io.mosip.partner.partnermanagement.util.KeyMgrUtil;
import io.mosip.partner.partnermanagement.util.RestApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class PartnerCreationServiceImpl implements PartnerCreationService {

    Logger logger = PartnerManagementLogger.getLogger(PartnerCreationServiceImpl.class);

    @Autowired
    Environment env;

    @Autowired
    RestApiClient restApiClient;

    @Autowired
    KeyMgrUtil keyMgrUtil;

    @Override
    public ResponseModel createPartner(Object request) {
        ResponseWrapper<PartnerResponse> response = null;
        ResponseModel responseModel = null;
        try {
            response = restApiClient.postApi(env.getProperty(ParameterConstant.PARTNER_APPID.toString()),
                    MediaType.APPLICATION_JSON, request, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel generateCertificates(String partnerId, String filePrepend) {
        CertificateChainResponseDto certificateChainResponseDto = null;
        ResponseModel responseModel;
        try {
            certificateChainResponseDto = keyMgrUtil.getPartnerCertificates(filePrepend, keyMgrUtil.getKeysDirPath(), partnerId);

            responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_GENERATED);
            responseModel.setResponseData(certificateChainResponseDto);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_GENERATION_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel uploadCACertificates(Object request) {
        ResponseWrapper<CertificateResponseData> response = null;
        ResponseModel responseModel = null;
        try {
            response = restApiClient.postApi(env.getProperty(ParameterConstant.PARTNER_CA_CERTIFICATE_UPLOAD.toString()),
                    MediaType.APPLICATION_JSON, request, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.CA_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.CA_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.CA_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel uploadPartnerCertificates(Object partnerCertificateRequest) {
        ResponseWrapper<PartnerCertificateResponseData> response = null;
        ResponseModel responseModel = null;
        try {
            response = restApiClient.postApi(env.getProperty(ParameterConstant.PARTNER_CERTIFICATE_UPLOAD.toString()),
                    MediaType.APPLICATION_JSON, partnerCertificateRequest, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.PC_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.PC_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.PC_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel partnerApiRequest(Object apiRequestData, String partnerId) {
        ResponseWrapper<LinkedHashMap> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_API_REQUEST.toString());
            apiUrl = apiUrl.replace("{partnerID}", partnerId);
            response = restApiClient.patchApi(apiUrl, MediaType.APPLICATION_JSON, apiRequestData, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.API_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.API_FAIL);
            }
            responseModel.setResponseData(response.getResponse());
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.API_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel approvePartnerApiRequest(Object approveRequestData, String apiId) {
        ResponseWrapper<ApiApproveReponseData> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_API_APROVE_REQUEST.toString());
            apiUrl = apiUrl.replace("{APIkey}", apiId);
            response = restApiClient.patchApi(apiUrl, MediaType.APPLICATION_JSON, approveRequestData, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.API_APPROVE_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.API_APPROVE_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.API_APPROVE_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel addBioExtractos(RequestWrapper<Object> extractRequestWrapper, String partnerId, String policyName) {
        ResponseWrapper<String> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_ADD_BIOEXTRACT_REQUEST.toString());
            apiUrl = apiUrl.replace("{partnerID}", partnerId);
            apiUrl = apiUrl.replace("{policyID}", policyName);
            response = restApiClient.postApi(apiUrl, MediaType.APPLICATION_JSON, extractRequestWrapper, ResponseWrapper.class);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.BIO_EXTRACT_ADD_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.BIO_EXTRACT_ADD_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.BIO_EXTRACT_ADD_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }
}
