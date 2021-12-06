package org.mosip.resident.pojo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import org.mosip.resident.service.APIHelper;

public class OTPRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requesttime;

    @SerializedName("request")
    public Request data = null;

    public class Request {
        @SerializedName("userId")
        public String userId;
        @SerializedName("langCode")

        public String langCode;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public OTPRequest(){
        data = new Request();
        id="mosip.pre-registration.login.sendotp";
        version ="1.0";
        requesttime = APIHelper.getUTCDateTime(null);

    }

}
