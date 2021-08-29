package io.mosip.partner.partnermanagement.service;

import io.mosip.partner.partnermanagement.model.PartnerDetailModel;
import io.mosip.partner.partnermanagement.model.ResponseModel;
import io.mosip.partner.partnermanagement.model.apikey.ApiApproveRequestData;
import io.mosip.partner.partnermanagement.model.apikey.ApiKeyRequestData;
import io.mosip.partner.partnermanagement.model.authmodel.LoginUser;
import io.mosip.partner.partnermanagement.model.certificate.CertificateChainResponseDto;
import io.mosip.partner.partnermanagement.model.certificate.PartnerCertificateRequestData;
import io.mosip.partner.partnermanagement.model.http.RequestWrapper;

public interface PartnerCreationService {
    public ResponseModel createPartner(Object partnerModel);
    public ResponseModel generateCertificates(String partnerId, String filePrepend);
    public ResponseModel uploadCACertificates(Object certificateModel);
    public ResponseModel uploadPartnerCertificates(Object partnerCertificateRequest);
    public ResponseModel partnerApiRequest(Object apiRequestData, String partnerId);
    public ResponseModel approvePartnerApiRequest(Object approveRequestData, String apiId);
}
