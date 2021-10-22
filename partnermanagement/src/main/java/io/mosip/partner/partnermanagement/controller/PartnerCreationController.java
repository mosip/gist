package io.mosip.partner.partnermanagement.controller;

import io.mosip.partner.partnermanagement.constant.LoggerFileConstant;
import io.mosip.partner.partnermanagement.constant.LoginType;
import io.mosip.partner.partnermanagement.constant.ParameterConstant;
import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import io.mosip.partner.partnermanagement.model.*;
import io.mosip.partner.partnermanagement.logger.PartnerManagementLogger;
import io.mosip.partner.partnermanagement.model.apikey.ApiApproveRequestData;
import io.mosip.partner.partnermanagement.model.apikey.ApiKeyRequestData;
import io.mosip.partner.partnermanagement.model.authmodel.LoginUser;
import io.mosip.partner.partnermanagement.model.biometricextractors.ExtractorsRequestData;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.certificate.CertificateRequestData;
import io.mosip.partner.partnermanagement.model.certificate.PartnerCertificateRequestData;
import io.mosip.partner.partnermanagement.model.certificate.PartnerCertificateResponseData;
import io.mosip.partner.partnermanagement.model.device.*;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;
import io.mosip.partner.partnermanagement.model.http.ResponseWrapper;
import io.mosip.partner.partnermanagement.model.partner.PartnerRequest;
import io.mosip.partner.partnermanagement.model.restapi.Metadata;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricActivateRequestDto;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricsRequestDto;
import io.mosip.partner.partnermanagement.model.securebiometrics.SecureBiometricsResponseDto;
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

import javax.servlet.http.Part;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

    @PostMapping(value = "/configureL1Device", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceConfigurationResponseModel> configureL0Device(@RequestBody DeviceL1Model deviceL1Model) {
        // Authentication with Auth Manager using User Id & Password
        DeviceConfigurationResponseModel deviceConfigurationResponseModel = new DeviceConfigurationResponseModel();
        deviceConfigurationResponseModel.setResponse(new ArrayList<>());

        logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.APPLICATIONID.toString(), LoggerFileConstant.APPLICATIONID.toString(),  "Authenticate With AuthService");
        LoginUser request = new LoginUser(env.getProperty(ParameterConstant.AUTHENTICATION_USERID.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_PASSWORD.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_APPID.toString()));

        RequestWrapper<Object> requestWrapper = createRequestWrapper(request);
        restApiClient.setLoginRequestData(LoginType.LOGIN_BY_USERID, requestWrapper);

// Configuring Device Partner
        if(deviceL1Model.getDeviceProvider() != null) {
            DeviceL1ResponseModel deviceL1ResponseModel = new DeviceL1ResponseModel();
            deviceL1ResponseModel.setErrors(new ArrayList<>());

            // Creation of Partner from Self Partner Creation request
            ResponseModel responseModel = createSelfPartner(deviceL1Model.getDeviceProvider());

            if (responseModel.getStatus().equals(LoggerFileConstant.FAIL)) {
                deviceL1ResponseModel.getErrors().add(responseModel);
                ResponseWrapper wrapper = (ResponseWrapper) responseModel.getResponseData();
                if (!wrapper.canBeIgnored()) {
                    deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }
            }

            deviceL1ResponseModel.setPartnerId(deviceL1Model.getDeviceProvider().getPartnerId());

            // Upload All Certificates
            ResponseEntity<ResponseModel> responseEntity = uploadAllCertificates(deviceL1Model.getDeviceProvider(), deviceL1Model.getDeviceProvider().getCertificateDetails());

            if (responseEntity != null && !responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                deviceL1ResponseModel.getErrors().add(responseEntity);
                ResponseWrapper wrapper = (ResponseWrapper) responseEntity.getBody().getResponseData();

                if (!wrapper.canBeIgnored()) {
                    deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                ResponseModel partnerCertUploadResponse = responseEntity.getBody();

                if (deviceL1Model.getDeviceProvider().getPartnerType().equals(PartnerTypes.DEVICE)) {
                    ResponseWrapper wrapper = (ResponseWrapper) partnerCertUploadResponse.getResponseData();
                    LinkedHashMap<String, String> partnerCertificateResponseData = (LinkedHashMap<String, String>) wrapper.getResponse();
                    deviceL1ResponseModel.setSignedCertificate(partnerCertificateResponseData.get("signedCertificateData"));
                } else {
                    // Write Error Message
                }
            }

            // Upload Device Details
            for (DeviceModel deviceModel : deviceL1Model.getDeviceProvider().getDeviceDetails()) {
                RequestWrapper<Object> deviceAddRequest = addDeviceDetails(deviceModel, deviceL1Model.getDeviceProvider());
                ResponseModel deviceAddResponse =   partnerCreationService.addDeviceDetails(deviceAddRequest);

                if (deviceAddResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    deviceL1ResponseModel.getErrors().add(deviceAddResponse);
                    deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }

                ResponseWrapper addResponseDto  = (ResponseWrapper) deviceAddResponse.getResponseData();
                LinkedHashMap<String, String> addResponse = (LinkedHashMap<String, String>) addResponseDto.getResponse();
                deviceL1ResponseModel.setDeviceId(addResponse.get("id"));

                // Activate Device
                RequestWrapper<Object> activateDeviceRequest = activateDevice(addResponse.get("id"), deviceL1Model.getDeviceProvider());
                ResponseModel activateDeviceResponse =   partnerCreationService.activateDevice(activateDeviceRequest);

                if (activateDeviceResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    deviceL1ResponseModel.getErrors().add(activateDeviceResponse);
                    deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }

                // Upload Secure Biometric Interface
                RequestWrapper<Object> secureBiometricsAddRequest = addSecureBiometricDetails(addResponse.get("id"), deviceL1Model.getDeviceProvider(), deviceModel);
                ResponseModel secureBiometricsAddResponse =   partnerCreationService.addSecureBiometricDetails(secureBiometricsAddRequest);

                if (secureBiometricsAddResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    deviceL1ResponseModel.getErrors().add(secureBiometricsAddResponse);
                    deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }

                ResponseWrapper secureBiometricsResponseDto  = (ResponseWrapper) secureBiometricsAddResponse.getResponseData();
                LinkedHashMap<String, String> secureBiometricsResponse = (LinkedHashMap<String, String>) secureBiometricsResponseDto.getResponse();
                deviceL1ResponseModel.setSecureBiometricId(secureBiometricsResponse.get("id"));

                // Activate Secure Biometric Interface
                RequestWrapper<Object> activateSecureBiometricsRequest = activateSecureBioMetric(secureBiometricsResponse.get("id"), deviceL1Model.getDeviceProvider());
                ResponseModel activateSecureBiometricsResponse =   partnerCreationService.activateSecureBioMetric(activateSecureBiometricsRequest);

                if (activateSecureBiometricsResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    deviceL1ResponseModel.getErrors().add(activateSecureBiometricsResponse);
                    deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }
                deviceConfigurationResponseModel.getResponse().add(deviceL1ResponseModel);
            }
        }

/* ********************************************************************************************************************************** */

// Configuring FTM Partner
        if (deviceL1Model.getFtmProvider() != null) {
            DeviceL1ResponseModel ftmL1ResponseModel = new DeviceL1ResponseModel();
            ftmL1ResponseModel.setErrors(new ArrayList<>());

            // Creation of Partner from Self Partner Creation request
            ResponseModel ftmResponseModel = createSelfPartner(deviceL1Model.getFtmProvider());

            if (ftmResponseModel.getStatus().equals(LoggerFileConstant.FAIL)) {
                ftmL1ResponseModel.getErrors().add(ftmResponseModel);
                ResponseWrapper wrapper = (ResponseWrapper) ftmResponseModel.getResponseData();
                if (!wrapper.canBeIgnored()) {
                    deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }
            }

            ftmL1ResponseModel.setPartnerId(deviceL1Model.getFtmProvider().getPartnerId());

            // Upload All Certificates
            ResponseEntity<ResponseModel> ftmResponseEntity = uploadAllCertificates(deviceL1Model.getFtmProvider(), deviceL1Model.getFtmProvider().getCertificateDetails());

            if (ftmResponseEntity != null && !ftmResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                ftmL1ResponseModel.getErrors().add(ftmResponseEntity);
                deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
                ResponseWrapper wrapper = (ResponseWrapper) ftmResponseEntity.getBody().getResponseData();

                if (!wrapper.canBeIgnored())
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
            } else {
                ResponseModel ftmPartnerCertUploadResponse = ftmResponseEntity.getBody();

                if (deviceL1Model.getFtmProvider().getPartnerType().equals(PartnerTypes.FTM)) {
                    ResponseWrapper wrapper = (ResponseWrapper) ftmPartnerCertUploadResponse.getResponseData();
                    LinkedHashMap<String, String> partnerCertificateResponseData = (LinkedHashMap<String, String>) wrapper.getResponse();
                    ftmL1ResponseModel.setSignedCertificate(partnerCertificateResponseData.get("signedCertificateData"));
                } else {
                    // write Error Message
                }
            }

            // Upload Device Details
            for (DeviceModel deviceModel : deviceL1Model.getFtmProvider().getDeviceDetails()) {
                RequestWrapper<Object> deviceAddRequest = addDeviceDetails(deviceModel, deviceL1Model.getFtmProvider());
                ResponseModel deviceAddResponse =   partnerCreationService.addDeviceDetails(deviceAddRequest);

                if (deviceAddResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    ftmL1ResponseModel.getErrors().add(deviceAddResponse);
                    deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }

                ResponseWrapper addResponseDto  = (ResponseWrapper) deviceAddResponse.getResponseData();
                LinkedHashMap<String, String> addResponse = (LinkedHashMap<String, String>) addResponseDto.getResponse();
                ftmL1ResponseModel.setDeviceId(addResponse.get("id"));

                // Activate Device
                RequestWrapper<Object> activateDeviceRequest = activateDevice(addResponse.get("id"), deviceL1Model.getFtmProvider());
                ResponseModel activateDeviceResponse =   partnerCreationService.activateDevice(activateDeviceRequest);

                if (activateDeviceResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    ftmL1ResponseModel.getErrors().add(activateDeviceResponse);
                    deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }

                // Upload Secure Biometric Interface
                RequestWrapper<Object> secureBiometricsAddRequest = addSecureBiometricDetails(addResponse.get("id"), deviceL1Model.getFtmProvider(), deviceModel);
                ResponseModel secureBiometricsAddResponse =   partnerCreationService.addSecureBiometricDetails(secureBiometricsAddRequest);

                if (secureBiometricsAddResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    ftmL1ResponseModel.getErrors().add(secureBiometricsAddResponse);
                    deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }

                ResponseWrapper secureBiometricsResponseDto  = (ResponseWrapper) secureBiometricsAddResponse.getResponseData();
                LinkedHashMap<String, String> secureBiometricsResponse = (LinkedHashMap<String, String>) secureBiometricsResponseDto.getResponse();
                ftmL1ResponseModel.setSecureBiometricId(secureBiometricsResponse.get("id"));

                // Activate Secure Biometric Interface
                RequestWrapper<Object> activateSecureBiometricsRequest = activateSecureBioMetric(secureBiometricsResponse.get("id"), deviceL1Model.getFtmProvider());
                ResponseModel activateSecureBiometricsResponse =   partnerCreationService.activateSecureBioMetric(activateSecureBiometricsRequest);

                if (activateSecureBiometricsResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                    ftmL1ResponseModel.getErrors().add(activateSecureBiometricsResponse);
                    deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
                    return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.EXPECTATION_FAILED);
                }
                deviceConfigurationResponseModel.getResponse().add(ftmL1ResponseModel);
            }
        }

        return new ResponseEntity<DeviceConfigurationResponseModel>(deviceConfigurationResponseModel, HttpStatus.OK);
    }

    @PostMapping(value = "/createPartner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createPartner(@RequestBody ResidentPartnerDetailModel partnerDetailModel) {
        // Authentication with Auth Manager using User Id & Password
        Boolean isSignedCertificateCreated = false;
        String partnerSignedCertificate = null;
        logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.APPLICATIONID.toString(), LoggerFileConstant.APPLICATIONID.toString(),  "Authenticate With AuthService");
        LoginUser request = new LoginUser(env.getProperty(ParameterConstant.AUTHENTICATION_USERID.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_PASSWORD.toString()),
                env.getProperty(ParameterConstant.AUTHENTICATION_APPID.toString()));

        RequestWrapper<Object> requestWrapper = createRequestWrapper(request);
        restApiClient.setLoginRequestData(LoginType.LOGIN_BY_USERID, requestWrapper);

        // Creation of Partner from Self Partner Creation request
        ResponseModel responseModel = createSelfPartner(partnerDetailModel.getPartnerModel());

        if (responseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.EXPECTATION_FAILED);

        // Generating CA, SUB-CA, Partner Certificate
        ResponseWrapper<LinkedHashMap> responseWrapper = (ResponseWrapper<LinkedHashMap>) responseModel.getResponseData();
        LinkedHashMap partnerResponse = responseWrapper.getResponse();
        String partnerId = partnerResponse.get("partnerId").toString();
        String filePrepend = partnerDetailModel.getPartnerModel().getPartnerType().getFilePrepend();
        ResponseModel certificateResponseModel  = partnerCreationService.generateCertificates(partnerId, filePrepend);

        if (certificateResponseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(certificateResponseModel, HttpStatus.EXPECTATION_FAILED);

        CertificateChainResponseDto certificateChainResponseDto = (CertificateChainResponseDto) certificateResponseModel.getResponseData();

        // Upload All Certificates
        ResponseEntity<ResponseModel> responseEntity = uploadAllCertificates(partnerDetailModel.getPartnerModel(), certificateChainResponseDto);

        if (responseEntity != null && !responseEntity.getStatusCode().equals(HttpStatus.OK))
            return responseEntity;

        ResponseModel partnerCertUploadResponse = responseEntity.getBody();

        if (partnerDetailModel.getPartnerModel().getPartnerType().equals(PartnerTypes.DEVICE) || partnerDetailModel.getPartnerModel().getPartnerType().equals(PartnerTypes.FTM)) {
            PartnerCertificateResponseData partnerCertificateResponseData = (PartnerCertificateResponseData) partnerCertUploadResponse.getResponseData();
            isSignedCertificateCreated = true;
            partnerSignedCertificate = partnerCertificateResponseData.getSignedCertificateData();
        }

        // Submit PartnerAPI Key request Copy
        ApiKeyRequestData apiRequestData = new ApiKeyRequestData();
        apiRequestData.setPolicyName(partnerDetailModel.getPolicyName());
        apiRequestData.setUseCaseDescription("");
        RequestWrapper<Object> apiRequestWrapper = createRequestWrapper(apiRequestData);
        ResponseModel apiResonseModel = partnerCreationService.partnerApiRequest(apiRequestWrapper, partnerId);

        if (apiResonseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(apiResonseModel, HttpStatus.EXPECTATION_FAILED);

        // Add Bio Extractors for Partner
        ExtractorsRequestData extractors;
        if (partnerDetailModel.getExtractorList() != null && partnerDetailModel.getExtractorList().getExtractors() != null && !partnerDetailModel.getExtractorList().getExtractors().isEmpty()) {
             extractors = partnerDetailModel.getExtractorList();
        } else {
            ExtractorsRequestData extractorsRequestData = new ExtractorsRequestData();
            extractors = extractorsRequestData;
        }
        RequestWrapper<Object> extractRequestWrapper = createRequestWrapper(extractors);
        ResponseModel extractResonseModel = partnerCreationService.addBioExtractos(extractRequestWrapper, partnerId, partnerDetailModel.getPolicyName());

        if (apiResonseModel.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(extractResonseModel, HttpStatus.EXPECTATION_FAILED);


        // Approve PartnerAPI
        LinkedHashMap<String, String> apiKeyResponseData = (LinkedHashMap) apiResonseModel.getResponseData();
        ApiApproveRequestData approveRequestData = new ApiApproveRequestData();
        approveRequestData.setStatus("Approved");
        RequestWrapper<Object> apiApproveRequestWrapper = createRequestWrapper(approveRequestData);
        ResponseModel approveResonseModel = partnerCreationService.approvePartnerApiRequest(apiApproveRequestWrapper, apiKeyResponseData.get("apiRequestId"));

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

    private RequestWrapper<Object> createPartnerRequest(PartnerModel partnerModel) {
        // Creation of Partner from Self Partner Creation request
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.setPartnerId(partnerModel.getPartnerId());
        partnerRequest.setPartnerType(partnerModel.getPartnerType().getPartnerType());
        partnerRequest.setAddress(partnerModel.getPartnerAddress());
        partnerRequest.setContactNumber(partnerModel.getPartnerContactNumber());
        partnerRequest.setEmailId(partnerModel.getPartnerEmailId());
        partnerRequest.setOrganizationName(partnerModel.getPartnerOrganizationName());
 //       partnerRequest.setPolicyGroup(partnerModel.getPolicyGroup() == null ? "" : partnerModel.getPolicyGroup());
  //      partnerRequest.setPolicyName("");
        return createRequestWrapper(partnerRequest);
    }

    private RequestWrapper<Object> createCACertificateRequest(PartnerModel partnerModel, String certificateData) {
        CertificateRequestData certificateRequest = new CertificateRequestData();
        certificateRequest.setCertificateData(certificateData);
        certificateRequest.setPartnerDomain(partnerModel.getPartnerType().getPartnerDomain());
        return createRequestWrapper(certificateRequest);
    }

    private RequestWrapper<Object> createPartnerCertificateRequest(PartnerModel partnerModel, String certificateData){
        PartnerCertificateRequestData partnerCertificateRequest = new PartnerCertificateRequestData();
        partnerCertificateRequest.setCertificateData(certificateData);
        partnerCertificateRequest.setPartnerDomain(partnerModel.getPartnerType().getPartnerDomain());
        partnerCertificateRequest.setPartnerId(partnerModel.getPartnerId());
        return createRequestWrapper(partnerCertificateRequest);
    }

    private ResponseModel createSelfPartner(PartnerModel partnerModel) {
        RequestWrapper<Object> partnerRequestWrapper = createPartnerRequest(partnerModel);
        return partnerCreationService.createPartner(partnerRequestWrapper);
    }

    private ResponseModel uploadCACertificate(PartnerModel partnerModel, String certificateData) {
        RequestWrapper<Object> caCertWrapper = createCACertificateRequest(partnerModel, certificateData);
        return partnerCreationService.uploadCACertificates(caCertWrapper);
    }

    private ResponseModel uploadPartnerCertificate(PartnerModel partnerModel, String certificateData) {
        RequestWrapper<Object> partnerCertWrapper = createPartnerCertificateRequest(partnerModel, certificateData);
        return partnerCreationService.uploadPartnerCertificates(partnerCertWrapper);
    }

    private ResponseEntity<ResponseModel> uploadAllCertificates(PartnerModel partnerModel, CertificateChainResponseDto certificateChainResponseDto) {
        // Upload CA Certificates
        ResponseModel caCertUploadResponse =  uploadCACertificate(partnerModel, certificateChainResponseDto.getCaCertificate());

        if (caCertUploadResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
            ResponseWrapper wrapper = (ResponseWrapper) caCertUploadResponse.getResponseData();
            if (!wrapper.canBeIgnored())
                return new ResponseEntity<ResponseModel>(caCertUploadResponse, HttpStatus.EXPECTATION_FAILED);
        }

        // Upload SUB-CA Certificates
        if (certificateChainResponseDto.getInterCertificate() != null) {
            ResponseModel subCaCertUploadResponse =  uploadCACertificate(partnerModel, certificateChainResponseDto.getInterCertificate());

            if (subCaCertUploadResponse.getStatus().equals(LoggerFileConstant.FAIL)) {
                ResponseWrapper wrapper = (ResponseWrapper) subCaCertUploadResponse.getResponseData();
                if (!wrapper.canBeIgnored())
                    return new ResponseEntity<ResponseModel>(subCaCertUploadResponse, HttpStatus.EXPECTATION_FAILED);
            }
        }

        // Upload Partner Certificate
        ResponseModel partnerCertUploadResponse = uploadPartnerCertificate(partnerModel, certificateChainResponseDto.getPartnerCertificate());

        if (partnerCertUploadResponse.getStatus().equals(LoggerFileConstant.FAIL))
            return new ResponseEntity<ResponseModel>(partnerCertUploadResponse, HttpStatus.EXPECTATION_FAILED);

        return new ResponseEntity<ResponseModel>(partnerCertUploadResponse, HttpStatus.OK);
    }

    private RequestWrapper<Object> addDeviceDetails(DeviceModel deviceModel, PartnerModel partnerModel) {
        // Adding Device Details into Application
        DeviceRequestDto deviceRequestDto = new DeviceRequestDto();
        deviceRequestDto.setDeviceProviderId(partnerModel.getPartnerId());
        deviceRequestDto.setDeviceTypeCode(deviceModel.getDeviceType().getDeviceTypeCode());
        deviceRequestDto.setDeviceSubTypeCode(deviceModel.getDeviceType().getDeviceSubTypeCode());
        deviceRequestDto.setIsItForRegistrationDevice(partnerModel.getPartnerType().isItForRegistrationDevice().toString());
        deviceRequestDto.setMake(deviceModel.getMake());
        deviceRequestDto.setModel(deviceModel.getModel());
        deviceRequestDto.setId(UUID.randomUUID().toString());
        deviceRequestDto.setPartnerOrganizationName(partnerModel.getPartnerOrganizationName());

        return createRequestWrapper(deviceRequestDto);
    }

    private RequestWrapper<Object> activateDevice(String addResponse, PartnerModel partnerModel) {
        DeviceActivateRequestDto activateRequestDto = new DeviceActivateRequestDto();
        activateRequestDto.setApprovalStatus("Activate");
        activateRequestDto.setId(addResponse);
        activateRequestDto.setIsItForRegistrationDevice(partnerModel.getPartnerType().isItForRegistrationDevice().toString());
        return createRequestWrapper(activateRequestDto);
    }

    private RequestWrapper<Object> addSecureBiometricDetails(String addResponse, PartnerModel partnerModel, DeviceModel deviceModel) {
        SecureBiometricsRequestDto biometricsRequestDto = new SecureBiometricsRequestDto();
        biometricsRequestDto.setDeviceDetailId(addResponse);
        if (deviceModel.getSecureBiometricsModel() != null) {
            biometricsRequestDto.setSwBinaryHash(deviceModel.getSecureBiometricsModel().getSwBinaryHash());
            biometricsRequestDto.setSwCreateDateTime(deviceModel.getSecureBiometricsModel().getSwCreateDateTime());
            biometricsRequestDto.setSwExpiryDateTime(deviceModel.getSecureBiometricsModel().getSwExpiryDateTime());
            biometricsRequestDto.setSwVersion(deviceModel.getSecureBiometricsModel().getSwVersion());
        } else {
            LocalDateTime dateTime = DateUtils.getUTCCurrentDateTime();
            LocalDateTime dateTimeExp = dateTime.plusYears(2);
            biometricsRequestDto.setSwBinaryHash("1");
            biometricsRequestDto.setSwCreateDateTime(DateUtils.formatToISOString(dateTime));
            biometricsRequestDto.setSwExpiryDateTime(DateUtils.formatToISOString(dateTimeExp));
            biometricsRequestDto.setSwVersion("1.0.0");
        }

        biometricsRequestDto.setIsItForRegistrationDevice(partnerModel.getPartnerType().isItForRegistrationDevice().toString());
        return createRequestWrapper(biometricsRequestDto);
    }

    private RequestWrapper<Object> activateSecureBioMetric(String secureBiometricsResponse, PartnerModel partnerModel) {
        SecureBiometricActivateRequestDto biometricActivateRequestDto = new SecureBiometricActivateRequestDto();
        biometricActivateRequestDto.setApprovalStatus("Activate");
        biometricActivateRequestDto.setId(secureBiometricsResponse);
        biometricActivateRequestDto.setIsItForRegistrationDevice(partnerModel.getPartnerType().isItForRegistrationDevice().toString());
        return createRequestWrapper(biometricActivateRequestDto);
    }
}
