package org.mosip.resident.pojo;
import com.google.gson.annotations.SerializedName;
import org.mosip.resident.service.APIHelper;

public class VIDRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requestTime;

    @SerializedName("request")
    public Request data ;
    public class Request {
        @SerializedName("individualId")
        public String individualId;
        @SerializedName("individualIdType")
        public String individualIdType;
        @SerializedName("otp")
        public String otp;
        @SerializedName("transactionID")
        public String transactionID;
        @SerializedName("vidType")
        public String vidType;      //Perpetual | Temporary

    }
    public VIDRequest(){
        id ="mosip.resident.vid";
        version ="v1";
        requestTime = APIHelper.getUTCDateTime(null);
        data = new Request();
    }
}

