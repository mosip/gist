package io.mosip.partner.partnermanagement.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.partner.partnermanagement.constant.MosipCertificateTypeConstant;
import io.mosip.partner.partnermanagement.constant.ParameterConstant;
import io.mosip.partner.partnermanagement.constant.PartnerManagementConstants;
import io.mosip.partner.partnermanagement.logger.PartnerManagementLogger;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.model.apikey.ApiApproveReponseData;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.certificate.CertificateResponseData;
import io.mosip.partner.partnermanagement.model.certificate.KeyManagerCertificateResponseData;
import io.mosip.partner.partnermanagement.model.certificate.PartnerCertificateResponseData;
import io.mosip.partner.partnermanagement.model.device.DeviceRequestDto;
import io.mosip.partner.partnermanagement.model.device.DeviceResponseDto;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;
import io.mosip.partner.partnermanagement.model.http.ResponseWrapper;
import io.mosip.partner.partnermanagement.model.partner.PartnerResponse;
import io.mosip.partner.partnermanagement.model.policy.PolicyMappingResponseData;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricsResponseDto;
import io.mosip.partner.partnermanagement.service.PartnerCreationService;
import io.mosip.partner.partnermanagement.util.KeyMgrUtil;
import io.mosip.partner.partnermanagement.util.RestApiClient;
import org.apache.catalina.mapper.Mapper;
import io.mosip.kernel.core.logger.spi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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
            logger.info("Partner Self Creation");
            response = (ResponseWrapper<PartnerResponse>) callPostApi(env.getProperty(ParameterConstant.PARTNER_APPID.toString()), request);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_FAIL);
            response = new ResponseWrapper<>();
            response.setErrors(new ArrayList<>());
            response.getErrors().add(new ServiceError(PartnerManagementConstants.PARTNER_FAIL.getErrorCode(), e.getMessage()));
            responseModel.setResponseData(response);
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel generateCertificates(String partnerId, String partnerOrganization, String filePrepend) {
        CertificateChainResponseDto certificateChainResponseDto = null;
        ResponseModel responseModel;
        try {
            certificateChainResponseDto = keyMgrUtil.getPartnerCertificates(filePrepend, keyMgrUtil.getKeysDirPath(partnerOrganization), partnerId);

            responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_GENERATED);
            responseModel.setResponseData(certificateChainResponseDto);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_GENERATION_FAIL);
            ResponseWrapper wrapper = new ResponseWrapper();
            wrapper.setErrors(new ArrayList<>());
            wrapper.getErrors().add(new ServiceError(PartnerManagementConstants.CERTIFICATE_GENERATION_FAIL.getErrorCode(), e.getMessage()));
            responseModel.setResponseData(wrapper);
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel uploadCACertificatesIntoKeyManager(Object request) {
        ResponseWrapper<CertificateResponseData> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling Upload CA/SUB Certificate into KeyManager");
            response = (ResponseWrapper<CertificateResponseData>) callPostApi(env.getProperty(ParameterConstant.PARTNER_CA_CERTIFICATE_UPLOAD.toString()), request);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.KEYMANAGR_CA_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.KEYMANAGR_CA_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.KEYMANAGR_CA_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel uploadCACertificatesIntoIDA(Object request) {
        ResponseWrapper<CertificateResponseData> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling Upload CA/SUB Certificate into IDA");
            response = (ResponseWrapper<CertificateResponseData>) callPostApi(env.getProperty(ParameterConstant.PARTNER_IDA_CA_CERTIFICATE_UPLOAD.toString()), request);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.IDA_CA_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.IDA_CA_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.IDA_CA_FAIL);
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
            logger.info("Calling Upload Partner Certificate");
            response = (ResponseWrapper<PartnerCertificateResponseData>) callPostApi(env.getProperty(ParameterConstant.PARTNER_CERTIFICATE_UPLOAD.toString()), partnerCertificateRequest);

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

            logger.info("Calling Partner API Request");
            response = (ResponseWrapper<LinkedHashMap>) callPatchApi(apiUrl, apiRequestData);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.API_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.API_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.API_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel partnerApiRequestForLTS(Object apiRequestData, String partnerId) {
        ResponseWrapper<LinkedHashMap> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_API_REQUEST_FOR_LTS.toString());
            apiUrl = apiUrl.replace("{partnerID}", partnerId);

            logger.info("Calling Partner API Request");
            response = (ResponseWrapper<LinkedHashMap>) callPatchApi(apiUrl, apiRequestData);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.API_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.API_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.API_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel partnerPolicyMappingForLTS(Object policyMapRequestWrapper, String partnerId) {
        ResponseWrapper<PolicyMappingResponseData> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_POLICY_MAPPING_REQUEST.toString());
            apiUrl = apiUrl.replace("{partnerID}", partnerId);

            logger.info("Calling Partner Policy Map Request");
            response = (ResponseWrapper<PolicyMappingResponseData>) callPostApi(apiUrl, policyMapRequestWrapper);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_POLICY_MAP_SUUCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_POLICY_MAP_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_POLICY_MAP_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel approvePartnerPolicyMapRequest(Object apiApproveRequestWrapper, String policyMappingKey) {
        ResponseWrapper<String> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_POLICY_MAPPING_APPROVAL.toString());
            apiUrl = apiUrl.replace("{PolicyMappingId}", policyMappingKey);

            logger.info("Calling Partner policy Map Approval Request");
            response = (ResponseWrapper<String>) callPutApi(apiUrl, apiApproveRequestWrapper);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_POLICY_MAP_APPROVAL_SUUCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_POLICY_MAP_APPROVAL_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.PARTNER_POLICY_MAP_APPROVAL_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public Boolean updateSignedCertificateintoPartnerP12(String signedCertificate, String filePrepand, String partnerOrganization) {
        try {
            X509Certificate x509Cert = (X509Certificate) keyMgrUtil.convertToCertificate(signedCertificate);
            boolean isUpdated = keyMgrUtil.updatePartnerCertificate(filePrepand, x509Cert, keyMgrUtil.getKeysDirPath(partnerOrganization, true));
            return isUpdated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ResponseModel approvePartnerApiRequest(Object approveRequestData, String apiId) {
        ResponseWrapper<ApiApproveReponseData> response = null;
        ResponseModel responseModel = null;
        try {
            String apiUrl = env.getProperty(ParameterConstant.PARTNER_API_APROVE_REQUEST.toString());
            apiUrl = apiUrl.replace("{APIkey}", apiId);

            logger.info("Calling Approve Partner API Request");
            response = (ResponseWrapper<ApiApproveReponseData>) callPatchApi(apiUrl, approveRequestData);

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

            logger.info("Calling Add Bio Extractors");
            response = (ResponseWrapper<String>) callPostApi(apiUrl, extractRequestWrapper);

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

    @Override
    public ResponseModel addDeviceDetails(Object deviceDetails) {
        ResponseWrapper<DeviceResponseDto> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling Add Device");
            response = (ResponseWrapper<DeviceResponseDto>) callPostApi(env.getProperty(ParameterConstant.DEVICE_DETAIL_ADD_REQUEST.toString()), deviceDetails);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.DEVICE_DETAIL_ADD_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.DEVICE_DETAIL_ADD_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.DEVICE_DETAIL_ADD_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel activateDevice(RequestWrapper<Object> activateDeviceRequest) {
        ResponseWrapper<String> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling Activate Device");
            response = (ResponseWrapper<String>) callPatchApi(env.getProperty(ParameterConstant.DEVICE_ACTIVATE_REQUEST.toString()), activateDeviceRequest);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.DEVICE_ACTIVATION_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.DEVICE_ACTIVATION_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.DEVICE_ACTIVATION_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel addSecureBiometricDetails(RequestWrapper<Object> secureBiometricsAddRequest) {
        ResponseWrapper<SecureBiometricsResponseDto> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling Add Secure Biometric for Device");
            response = (ResponseWrapper<SecureBiometricsResponseDto>) callPostApi(env.getProperty(ParameterConstant.SECURE_BIOMETRIC_ADD_REQUEST.toString()), secureBiometricsAddRequest);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.SECURE_BIOMETRICS_DETAIL_ADD_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.SECURE_BIOMETRICS_DETAIL_ADD_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.SECURE_BIOMETRICS_DETAIL_ADD_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel activateSecureBioMetric(RequestWrapper<Object> activateSecureBiometricsRequest) {
        ResponseWrapper<String> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling Activate Secure Biometric for Device");
            response = (ResponseWrapper<String>) callPatchApi(env.getProperty(ParameterConstant.SECURE_BIOMETRIC_ACTIVATE_REQUEST.toString()), activateSecureBiometricsRequest);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.SECURE_BIOMETRICS_ACTIVATION_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.SECURE_BIOMETRICS_ACTIVATION_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.SECURE_BIOMETRICS_ACTIVATION_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }


    @Override
    public ResponseModel generateMISPLicenseKey(RequestWrapper<Object> mispRequestWrapper) {
        ResponseWrapper<String> response = null;
        ResponseModel responseModel = null;
        try {
            logger.info("Calling MISP License Key Generation");
            response = (ResponseWrapper<String>) callPostApi(env.getProperty(ParameterConstant.GENERATE_MISP_LICENSE_KEY.toString().toString()), mispRequestWrapper);

            if (response.getResponse() != null) {
                responseModel = new ResponseModel(PartnerManagementConstants.MISP_LICENSE_GENERATION_SUCCESS);
            } else {
                responseModel = new ResponseModel(PartnerManagementConstants.MISP_LICENSE_GENERATION_FAIL);
            }
            responseModel.setResponseData(response);
        } catch (Exception e) {
            responseModel = new ResponseModel(PartnerManagementConstants.MISP_LICENSE_GENERATION_FAIL);
            responseModel.setResponseData(e.getMessage());
            e.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel getCertificateFromKeyManager(MosipCertificateTypeConstant constant) {
            ResponseWrapper<KeyManagerCertificateResponseData> response = null;
            ResponseModel responseModel = null;
            try {
                logger.info("Fetching Certificate from Key Manager " + constant.toString());

                URI uri = UriComponentsBuilder.fromHttpUrl(env.getProperty(ParameterConstant.FETCH_KEY_MANAGER_CERTIFICATE.toString())).buildAndExpand(constant.toString()).toUri();

                response = (ResponseWrapper<KeyManagerCertificateResponseData>) callGetApi(uri);

                if (response.getResponse() != null) {
                    responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_FETCH_SUCCESSFUL);
                } else {
                    responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_FETCH_FAIL);
                }
                ObjectMapper mapper = new ObjectMapper();
                KeyManagerCertificateResponseData responseData = mapper.convertValue(response.getResponse(), KeyManagerCertificateResponseData.class);
                responseModel.setResponseData(responseData);
            } catch (Exception e) {
                responseModel = new ResponseModel(PartnerManagementConstants.CERTIFICATE_FETCH_FAIL);
                responseModel.setResponseData(e.getMessage());
                e.printStackTrace();
            }

            return responseModel;
    }

    public Object callPatchApi(String url, Object request) throws Exception {
        logger.info("URL : " + url);
        logger.info("Method : PATCH");
        logger.info("Request : \n" + formatObjectToJson(request));
        Object response =  restApiClient.patchApi(url, MediaType.APPLICATION_JSON, request, ResponseWrapper.class);
        logger.info("Response : \n" + formatObjectToJson(response));
        return response;
    }

    public Object callPostApi(String url, Object request) throws Exception {
        logger.info("URL : " + url);
        logger.info("Method  : POST");
        logger.info("Request :  \n" + formatObjectToJson(request));
        Object response =  restApiClient.postApi(url, MediaType.APPLICATION_JSON, request, ResponseWrapper.class);
        logger.info("Response : \n" + formatObjectToJson(response));
        return response;
    }

    public Object callPutApi(String url, Object request) throws Exception {
        logger.info("URL : " + url);
        logger.info("Method  : PUT");
        logger.info("Request :  \n" + formatObjectToJson(request));
        Object response =  restApiClient.putApi(url, request, ResponseWrapper.class, MediaType.APPLICATION_JSON);
        logger.info("Response : \n" + formatObjectToJson(response));
        return response;
    }

    public Object callGetApi(URI url) throws Exception {
        logger.info("URL : " +  url);
        logger.info("Method : GET");
        Object response =  restApiClient.getApi(url, ResponseWrapper.class);
        logger.info("Response : \n" + formatObjectToJson(response));
        return response;
    }

    public String formatObjectToJson(Object object) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
    }
}
