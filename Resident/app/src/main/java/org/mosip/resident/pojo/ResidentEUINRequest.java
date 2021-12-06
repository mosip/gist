package org.mosip.resident.pojo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;


import org.mosip.resident.service.APIHelper;

public class ResidentEUINRequest {

    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requestTime;

    @SerializedName("request")
    public Request data ;
    public class Request {
        @SerializedName("cardType")
        public String cardType;

        @SerializedName("individualId")
        public String individualId;
        @SerializedName("individualIdType")
        public String individualIdType;

        @SerializedName("otp")
        public String otp;
        @SerializedName("transactionID")
        public String transactionID;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ResidentEUINRequest(){
        id ="mosip.resident.euin";
        version ="v1";
        requestTime = APIHelper.getUTCDateTime(null);
        data = new Request();
    }

}
