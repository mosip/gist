package io.mosip.partner.partnermanagement.service;

import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;

public interface PartnerCreationService {
    public ResponseModel createPartner(Object partnerModel);
    public ResponseModel generateCertificates(String partnerId, String filePrepend);
    public ResponseModel uploadCACertificates(Object certificateModel);
    public ResponseModel uploadPartnerCertificates(Object partnerCertificateRequest);
    public ResponseModel partnerApiRequest(Object apiRequestData, String partnerId);
    public ResponseModel approvePartnerApiRequest(Object approveRequestData, String apiId);
    public ResponseModel addBioExtractos(RequestWrapper<Object> extractRequestWrapper, String partnerId, String policyName);
    public ResponseModel addDeviceDetails(Object deviceDetails);
    public ResponseModel activateDevice(RequestWrapper<Object> activateDeviceRequest);
    public ResponseModel addSecureBiometricDetails(RequestWrapper<Object> secureBiometricsAddRequest);
    public ResponseModel activateSecureBioMetric(RequestWrapper<Object> activateSecureBiometricsRequest);
}
