package org.mosip.resident.pojo;

import com.google.gson.annotations.SerializedName;

public class IDStatusRequest {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("requesttime")
    public String requesttime;

    @SerializedName("request")
    public Request data = null;

    public class Request {
        @SerializedName("individualId")
        public String individualId;
        @SerializedName("individualIdType")

        public String individualIdType;

    }
    public IDStatusRequest(){
        data = new Request();
    }

}
