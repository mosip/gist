package org.mosip.resident.pojo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import org.mosip.resident.service.APIHelper;

public class ResidentHistoryRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requesttime;

    @SerializedName("request")
    public Request data ;
    public class Request {
        @SerializedName("pageFetch")
        public String pageFetch;

        @SerializedName("individualId")
        public String individualId;
        @SerializedName("pageStart")
        public String pageStart;

        @SerializedName("otp")
        public String otp;
        @SerializedName("transactionID")
        public String transactionID;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ResidentHistoryRequest(){
        id ="mosip.resident.authhistory";
        version ="v1";
        requesttime = APIHelper.getUTCDateTime(null);
        data = new Request();
    }

}
