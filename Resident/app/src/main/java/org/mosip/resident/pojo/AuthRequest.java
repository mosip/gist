package org.mosip.resident.pojo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import org.mosip.resident.service.APIHelper;

public class AuthRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requesttime;

    @SerializedName("request")
    public Request data = null;

    public class Request {
        @SerializedName("clientId")
        public String clientId;
        @SerializedName("secretKey")
        public String secretKey;
        @SerializedName("appId")
        public String appId;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public AuthRequest(){
        data = new Request();
        id="mosip.auth";
        version ="1.0";
        requesttime = APIHelper.getUTCDateTime(null);
    }
}
