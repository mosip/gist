package io.mosip.partner.partnermanagement.constant;

public enum PartnerTypes {

    DEVICE("device"), 
    RELYING_PARTY("rp"), 
    FTM("ftm"),
    EKYC("ekyc");

    private String filePrepend;

    private PartnerTypes(String filePrepend) {
        this.filePrepend = filePrepend;
    }

    public String getFilePrepend() {
        return this.filePrepend;
    }
}
