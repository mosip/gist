package org.mosip.resident.service;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.mosip.resident.App;
import org.mosip.resident.pojo.APIResponse;
import org.mosip.resident.pojo.AuthHistory;
import org.mosip.resident.pojo.AuthRequest;

import org.mosip.resident.pojo.ChangeOfAddressRequest;
import org.mosip.resident.pojo.IdentityWrapper;

import org.mosip.resident.pojo.ResidentCredRequest;
import org.mosip.resident.pojo.ResidentEUINRequest;
import org.mosip.resident.pojo.ResidentHistoryRequest;
import org.mosip.resident.pojo.ResidentOTPRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;


import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.mosip.resident.R;
import org.mosip.resident.pojo.VIDRequest;

public class APIHelper {
    MOSIPAPI apiInterface;
    String authToken;
    List<AuthHistory> ret ;
    String retVal = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getUTCDateTime(LocalDateTime time) {
        String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATEFORMAT);
        if (time == null){
            time = LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());
        }
        String utcTime = time.format(dateFormat);
        return utcTime;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String genRandomNumbers(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        //int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return(generatedString);
    }
    public APIHelper(){
        apiInterface = APISetup.getClient().create(MOSIPAPI.class);


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void authApp(){

        Call<APIResponse> call = apiInterface.requestAuthWithSecret( createAuthReq());
        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d("TAG",response.code()+"");
                Log.d("TAG",response.body().getResponse().getMessage());

                if(response.code() == 200){
                    authToken = response.headers().get("Set-Cookie");
                    Log.d("TAG", authToken);
     //               requestResidentOTP("5091326710","uin");
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

    public String requestVID(String id, String idType, String trId, String otp, String vidType, APICallback cb){

        Call<APIResponse> call = apiInterface.requestVID(createVIDReq(id,idType, trId,otp,vidType));
        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
             @Override
             public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                 Log.d("TAG",response.code()+"");
                 if(response.code() == 200) {
                     if(response.body().getResponse() != null) {
                         Log.d("TAG", response.body().getResponse().vid);
                         if(cb != null)
                             cb.onSuccess(response.body().getResponse().vid);
                     }
                     else {
                         if(response.body().getErrors() != null){
                             Log.d("TAG", response.body().getErrors().toString());
                             if(cb != null)
                                 cb.onError (response.body().getErrors().toString());

                         }
                     }

                 }
                 else{
                     //      assert response.body() != null;
                     Log.d("TAG", response.message());
                     if(cb != null)
                         cb.onError (response.message());

                 }

             }
             @Override
             public void onFailure(Call<APIResponse> call, Throwable t) {
                 call.cancel();
             }

        });
        return "";
    }
    /*
        Request OTP based on UIN/RID
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  Boolean requestResidentOTP(String id, String idType, String trId , APICallback cb){

        Call<APIResponse> call = apiInterface.requestResidentOTP(createResidentOTPReq(id,idType, trId));
        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {
                        Log.d("TAG", response.body().getResponse().maskedMobile);
                        if(cb != null)
                            cb.onSuccess("OTP request sent successfully");
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
                            if(cb != null)
                                cb.onError (response.body().getErrors().toString());

                        }
                    }

                }
                else{
              //      assert response.body() != null;
                    Log.d("TAG", response.message());
                    if(cb != null)
                        cb.onError (response.message());

                }

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });
        return true;
    }
   
    
    //private ExecutorService executor = Executors.newSingleThreadExecutor();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<AuthHistory> requestResidentHistory(String id, String pageNumber, String otp, String trId, APICallback cb){
        Call<APIResponse> call = apiInterface.requestAuthHistory(createResidentHistoryReq(id,pageNumber,otp, trId));


         call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {
                        Log.d("TAG", response.body().getResponse().getMessage());
                        Log.d("count=", String.valueOf(response.body().getResponse().authHistory.size()));
                        ret =response.body().getResponse().authHistory;
                        if(cb != null){
                            cb.onSuccess(ret);
                        }
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
                            if(cb != null){
                                cb.onError(response.body().getErrors());
                            }

                        }
                    }

                }
                else{
                    //      assert response.body() != null;
                    Log.d("TAG", response.message());

                }

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });
        return ret;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void requestChangeOfAddress(String id, String idType, String otp, String trId, String[] addressLines, APICallback cb){


        Call<APIResponse> call = apiInterface.requestUpdateDemoData(createCOAReq(id,idType,otp, addressLines, trId));

        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {
                        Log.d("TAG", response.body().getResponse().getMessage());
                        Log.d("regid=", response.body().getResponse().registrationId);
                        retVal = response.body().getResponse().registrationId;
                        if(cb != null){
                            cb.onSuccess(retVal);
                        }
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
                            if(cb != null){
                                cb.onError(response.body().getErrors());
                            }

                        }
                    }

                }
                else{
                    //      assert response.body() != null;
                    Log.d("TAG", response.message());

                }

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadResidentCredentials(String reqid, APICallback cb){

        Call<APIResponse> call = apiInterface.downloadResidentCredentials(reqid);

        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                String retval = "";
                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {

                        try {
                            retval = response.raw().body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("TAG", retval);


                        if(cb != null){
                            cb.onSuccess(retval);
                        }
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
                            if(cb != null){
                                cb.onError(response.body().getErrors());
                            }

                        }
                    }

                }
                else{
                    //      assert response.body() != null;
                    Log.d("TAG", response.message());

                }

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getResidentCredentialRequestStatus(String reqid, APICallback cb){

        Call<APIResponse> call = apiInterface.getResidentCredRequestStatus(reqid);

        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                String retval = "";
                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {
                        retval = response.body().getResponse().statusCode;
                        Log.d("TAG", retval);

                        if(cb != null){
                            cb.onSuccess(retval);
                        }
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
                            if(cb != null){
                                cb.onError(response.body().getErrors());
                            }

                        }
                    }

                }
                else{
                    //      assert response.body() != null;
                    Log.d("TAG", response.message());

                }

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void requestResidentCredentials(String id, String otp, String credType, String trId, APICallback cb){
        Call<APIResponse> call = apiInterface.requestResidentCredentials(createResidentCredReq(id,otp, credType,trId));

        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {
                       // Log.d("TAG", response.body().getResponse().getMessage());
                        Log.d("Request ID=", response.body().getResponse().requestId);

                        if(cb != null){
                            cb.onSuccess(response.body().getResponse().requestId);
                        }
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
                            if(cb != null){
                                cb.onError(response.body().getErrors());
                            }

                        }
                    }

                }
                else{
                    //      assert response.body() != null;
                    Log.d("TAG", response.message());

                }

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

    private VIDRequest createVIDReq(String id, String idType, String trId, String otp, String vidType){
        VIDRequest req = new VIDRequest();
        req.data.individualId = id;
        req.data.individualIdType= idType;
        req.data.vidType= vidType;
        req.data.otp= otp;
        req.data.transactionID= trId;
        return req;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ChangeOfAddressRequest createCOAReq(String id, String idType, String otp, String[] addressLines, String trId){
        ChangeOfAddressRequest req= new ChangeOfAddressRequest();
        IdentityWrapper identity = new IdentityWrapper(1);
        for(int i=0; i < 3; i++) {
            identity.identity.setAddress(i+1, 0, "eng", addressLines[i]);
        }
        identity.identity.UIN = id;
        req.data.setIdentityJson(identity);
        req.data.individualId = id;
        req.data.individualIdType = idType;
        req.data.transactionID = trId;
        req.data.otp = otp;

        return req;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ResidentCredRequest createResidentCredReq(String id, String otp, String credType, String trId){
        ResidentCredRequest req = new ResidentCredRequest();
        req.data.otp = otp;
        req.data.individualId = id;
        req.data.credentialType= credType;
        req.data.transactionID = trId;
        req.data.encrypt =false;

        return req;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ResidentEUINRequest createResidentEUINReq(String id, String idType, String otp, String trId) {
        ResidentEUINRequest req = new ResidentEUINRequest();
        req.data.individualId= id;
        req.data.individualIdType= idType;
        req.data.transactionID= trId;
        req.data.otp= otp;
        req.data.cardType="UIN";
        return req;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ResidentHistoryRequest createResidentHistoryReq(String id, String pageNumber, String otp, String trId) {
        ResidentHistoryRequest req = new ResidentHistoryRequest();
        req.data.individualId= id;
        req.data.pageFetch= "500";
        req.data.transactionID= trId;
        req.data.otp= otp;
        req.data.pageStart = pageNumber;
        return req;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static ResidentOTPRequest createResidentOTPReq(String id, String idType, String trId) {
        ResidentOTPRequest req = new ResidentOTPRequest();

        req.individualId = id;
        req.individualIdType= idType;
        req.transactionID= trId;
        req.otpChannel= new String[]{"PHONE","EMAIL"};
        return req;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static AuthRequest createAuthReq(){
        AuthRequest req = new AuthRequest();
        req.data.appId= App.getContext().getString(R.string.app_id); 
        req.data.secretKey = App.getContext().getString(R.string.clint_secret);
        req.data.clientId=App.getContext().getString(R.string.client_id); 
        return req;
    }

}
