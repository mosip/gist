package org.mosip.resident.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthHistory {

    @SerializedName("serialNumber")
    @Expose
    public String serialNumber;
    @SerializedName("idUsed")
    @Expose
    public String idUsed;
    @SerializedName("authModality")
    @Expose
    public String authModality;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("partnerName")
    @Expose
    public String partnerName;
    @SerializedName("partnerTransactionId")
    @Expose
    public String partnerTransactionId;


    }
