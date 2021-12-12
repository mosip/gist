package org.mosip.resident.service;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import org.apache.commons.lang3.RandomStringUtils;
import org.mosip.resident.App;
import org.mosip.resident.BuildConfig;
import org.mosip.resident.pojo.APIResponse;
import org.mosip.resident.pojo.AuthHistory;
import org.mosip.resident.pojo.AuthRequest;

import org.mosip.resident.pojo.ChangeOfAddressRequest;
import org.mosip.resident.pojo.IdentityWrapper;

import org.mosip.resident.pojo.ResidentCredRequest;
import org.mosip.resident.pojo.ResidentEUINRequest;
import org.mosip.resident.pojo.ResidentHistoryRequest;
import org.mosip.resident.pojo.ResidentOTPRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;


import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;


import okhttp3.ResponseBody;
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
/*
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
  */
        String generatedString = RandomStringUtils.random(targetStringLength, false, true);

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
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String requestResidentEUIN(String id, String idType, String otp, String trId){
        Call<APIResponse> call = apiInterface.requestResidentEUIN(createResidentEUINReq(id,idType,otp, trId));
        call.enqueue(new Callback<APIResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d("TAG",response.code()+"");
                if(response.code() == 200) {
                    if(response.body().getResponse() != null) {
                        Log.d("TAG", response.body().getResponse().getMessage());
                    }
                    else {
                        if(response.body().getErrors() != null){
                            Log.d("TAG", response.body().getErrors().toString());
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
        return "";
    }
    */

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

    public static void openSaveAs(String nameAs){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf"); //not needed, but maybe usefull
        intent.putExtra(Intent.EXTRA_TITLE, nameAs); //not needed, but maybe usefull
        App.getActivity().startActivityForResult(intent, 12001);
    }
    public static String saveToFile(byte[] barr, String fileName, Context ctx){
        ContentResolver resolver = ctx.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName );
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        Uri pdfUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);


        try {
            OutputStream fos = resolver.openOutputStream(Objects.requireNonNull(pdfUri));
            fos.write(barr);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfUri.toString();
      /*  try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(barr);
            fos.close();
        } catch (Exception e) {
            Log.e("write pdf", e.getMessage());
        }*/
    }
    public static void openPdf(File file, Activity activity){


        //String authority = activity.getApplicationContext().getPackageName() + ".fileprovider";
        //Uri uriToFile = FileProvider.getUriForFile(activity, authority, file);

        Uri uriToFile =FileProvider.getUriForFile(Objects.requireNonNull(activity.getApplicationContext()),
               BuildConfig.APPLICATION_ID + ".provider", file);


        //Uri uriToFile = Uri.fromFile(file);
        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setDataAndType(uriToFile, "application/pdf");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(shareIntent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadResidentCredentials(String reqid, APICallback cb){

        Call<ResponseBody> call = apiInterface.downloadResidentCredentials(reqid);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                byte[] retval = null;
                Log.d("TAG", response.code() + "");
                if (response.code() == 200) {
                    try {
                        retval = response.body().bytes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (cb != null) {
                        cb.onSuccess(retval);
                    }
                } else {
                    //      assert response.body() != null;
                    Log.d("TAG", response.message());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ResidentEUINRequest createResidentEUINReq(String id, String idType, String otp, String trId) {
        ResidentEUINRequest req = new ResidentEUINRequest();
        req.data.individualId= id;
        req.data.individualIdType= idType;
        req.data.transactionID= trId;
        req.data.otp= otp;
        req.data.cardType="UIN";
        return req;
    }*/
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
        req.data.appId= App.getContext().getString(R.string.app_id); //  "resident"
        req.data.secretKey = App.getContext().getString(R.string.clint_secret);// "abc123";
        req.data.clientId=App.getContext().getString(R.string.client_id); //"mosip-resident-client";
        return req;
    }

}
