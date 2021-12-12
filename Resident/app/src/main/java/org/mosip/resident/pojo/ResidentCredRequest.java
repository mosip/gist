package org.mosip.resident.pojo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import org.mosip.resident.service.APIHelper;

public class ResidentCredRequest {

    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requesttime;

    @SerializedName("request")
    public Request data = null;

    public class Request {

        @SerializedName("additionalData")
        public Object additionalData;
        @SerializedName("credentialType")
        public String credentialType;

        @SerializedName("encrypt")
        public Boolean encrypt;

        @SerializedName("encryptionKey")
        public String encryptionKey;

        @SerializedName("individualId")
        public String individualId;

        @SerializedName("issuer")
        public String issuer;
        @SerializedName("otp")
        public String otp;
        @SerializedName("sharableAttributes")
        public Object[] sharableAttributes;
        @SerializedName("recepiant")
        public String recepiant;
        @SerializedName("transactionID")
        public String transactionID;

        @SerializedName("user")
        public String user;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ResidentCredRequest(){
            data = new Request();
            id="";
            version ="v1";
            requesttime = APIHelper.getUTCDateTime(null);
            data.issuer= "mpartner-default-print";
            data.user = "";
            data.additionalData = new Object();
            data.sharableAttributes = new Object[0];
            //data.sharableAttributes[0]= "";
            data.recepiant ="";
            data.encrypt =false;
            data.encryptionKey="";
    }

}
