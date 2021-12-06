package org.mosip.resident.pojo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import org.mosip.resident.service.APIHelper;

public class ResidentOTPRequest {

    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requestTime")
    public String requestTime;

    @SerializedName("individualId")
    public String individualId;
    @SerializedName("individualIdType")
    public String individualIdType;

    @SerializedName("otpChannel")
    public String[] otpChannel;
    @SerializedName("transactionID")
    public String transactionID;
    @SerializedName("metadata")
    public Object metadata;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ResidentOTPRequest(){
        id ="mosip.identity.otp.internal";
        version ="1.0";
        requestTime = APIHelper.getUTCDateTime(null);
        metadata = new Object();
    }

}
