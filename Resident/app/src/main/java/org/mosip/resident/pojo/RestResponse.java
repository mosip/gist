package org.mosip.resident.pojo;

//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("authHistory")
    @Expose
    public List<AuthHistory> authHistory;

    @SerializedName("requestId")
    @Expose
    public String requestId;

    @SerializedName("statusCode")
    @Expose
    public String statusCode;

    @SerializedName("registrationId")
    @Expose
    public String registrationId;

    @SerializedName("maskedMobile")
    @Expose
    public String maskedMobile;

    @SerializedName("maskedEmail")
    @Expose
    public String maskedEmail;

    @SerializedName("vid")
    @Expose
    public String vid;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
