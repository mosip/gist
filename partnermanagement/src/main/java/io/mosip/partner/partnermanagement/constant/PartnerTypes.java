package io.mosip.partner.partnermanagement.constant;

public enum PartnerTypes {

    DEVICE("device", "Device_Provider", "DEVICE", true),
    FTM("ftm", "FTM_Provider", "FTM", false),
    AUTH("auth", "Auth_Partner", "AUTH", false),
    MISP("misp","MISP_Partner", "AUTH", false),
    CREDENTIAL("credential","Credential_Partner", "AUTH", false),
    ABIS("abis","ABIS_Partner", "AUTH", false),
    ADJUDICATION("adj", "Manual_Adjudication", "AUTH",false);

/*    AUTH("auth", "Auth Partner"),
    CREDENTIAL("credential", "Credential Partner"),
    PARTNER_ADMIN("", "Partner Admin"),
    ONLINE_VERIFICATION("","Online_Verification_Partner"),
    ABIS("", "ABIS_Partner"),
    MANUAL("", "Manual_Adjudication"),
    MISP("", "MISP_Partner"),*/

    private String filePrepend;
    private String partnerType;
    private String partnerDomain;
    private Boolean isItForRegistrationDevice;

    private PartnerTypes(String filePrepend, String partnerType, String partnerDomain, Boolean isItForRegistrationDevice ) {
        this.filePrepend = filePrepend;
        this.partnerType = partnerType;
        this.partnerDomain = partnerDomain;
        this.isItForRegistrationDevice = isItForRegistrationDevice;
    }

    public String getFilePrepend() {
        return this.filePrepend;
    }

    public String getPartnerType() {
        return this.partnerType;
    }

    public String getPartnerDomain() {
        return this.partnerDomain;
    }

    public Boolean isItForRegistrationDevice() {
        return this.isItForRegistrationDevice;
    }

}
