package org.mosip.resident.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import org.mosip.resident.pojo.APIResponse;
import org.mosip.resident.pojo.AuthRequest;
import org.mosip.resident.pojo.ChangeOfAddressRequest;
import org.mosip.resident.pojo.IDStatusRequest;
import org.mosip.resident.pojo.OTPRequest;
import org.mosip.resident.pojo.ResidentCredRequest;
import org.mosip.resident.pojo.ResidentEUINRequest;
import org.mosip.resident.pojo.ResidentHistoryRequest;
import org.mosip.resident.pojo.ResidentOTPRequest;
import org.mosip.resident.pojo.VIDRequest;
import org.mosip.resident.pojo.ValidateOTPRequest;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MOSIPAPI {
    /*
     * Get Access token, given client secret and AppId
     */
    @POST("/v1/authmanager/authenticate/clientidsecretkey")
    Call<APIResponse> requestAuthWithSecret(@Body AuthRequest request);

    /*
     *   Pre reg OTP Request
     */
    @POST("/preregistration/v1/login/sendOtp/langcode")
    Call<APIResponse> requestOTP(@Body OTPRequest request);

    /*
     *   Pre reg validate OTP
     */
    @POST("/preregistration/v1/login/validateOtp")
    Call<APIResponse> validateOTP(@Body ValidateOTPRequest request);

    /*
     * Get RID Status
     */
    @POST("/resident/v1/rid/check-status")
    Call<APIResponse> getIDStatus(@Body IDStatusRequest request);

    @POST("/resident/v1/req/otp")
    Call<APIResponse> requestResidentOTP(@Body ResidentOTPRequest request);


    @POST("/resident/v1/req/credential")
    Call<APIResponse> requestResidentCredentials(@Body ResidentCredRequest request);

    @GET("/resident/v1/req/status/{requestId}")
    Call<APIResponse> getResidentCredRequestStatus(@Path("requestId") String requestId);

    @GET("/resident/v1/req/card/{requestId}")
    Call<ResponseBody> downloadResidentCredentials(@Path("requestId") String requestId);


//    @POST("/resident/v1/req/euin")
 //   Call<APIResponse> requestResidentEUIN(@Body ResidentEUINRequest request);

    @POST("/resident/v1/req/auth-history")
    Call<APIResponse> requestAuthHistory(@Body ResidentHistoryRequest request);

    @POST("/resident/v1/req/update-uin")
    Call<APIResponse> requestUpdateDemoData(@Body ChangeOfAddressRequest request);
    @POST("/resident/v1/vid")
    Call<APIResponse> requestVID(@Body VIDRequest request);


}
