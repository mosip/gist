package org.mosip.resident;

import android.content.Intent;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import org.mosip.resident.databinding.ActivityMainBinding;
import org.mosip.resident.pojo.APIResponse;
import org.mosip.resident.pojo.OTPRequest;
import org.mosip.resident.pojo.ValidateOTPRequest;
import org.mosip.resident.service.APIHelper;
import org.mosip.resident.service.MOSIPAPI;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    MOSIPAPI apiInterface;
    APIHelper helper;
    String trId;
    public void setTrId(String tId){
        trId = tId;
    }
    public String getTrId(){
        return trId;
    }
    public APIHelper getHelper(){
        return helper;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        App.setBaseUrl( pref.getString("baseurl",App.getBaseUrl()));
        Log.d("Resident",App.getBaseUrl());


        App.setUIN(pref.getString("uin",App.getUIN()));
        Log.d("Resident",App.getUIN());


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_login,
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //DeviceAuth();
        //createBioPrompt();
        helper = new APIHelper();
        helper.authApp();

       //Fragment ff= getSupportFragmentManager().findFragmentById(R.id.navigation_login);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeValidateOTPCall() {
        Call<APIResponse> call = apiInterface.validateOTP (createValidateOTPRequest("111111", "sanath@mosip.io"));
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.d("TAG",response.code()+"");
                Log.d("TAG", response.headers().get("Set-Cookie"));
                Log.d("TAG",response.body().getResponse().getMessage());

            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    ValidateOTPRequest createValidateOTPRequest(String otp, String userId){
        ValidateOTPRequest req = new ValidateOTPRequest();
        req.id = "mosip.pre-registration.login.useridotp";
        req.requesttime = APIHelper.getUTCDateTime(null);
        req.version="1.0";
        req.data.otp = otp;
        req.data.userId = userId;
        return req;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    static OTPRequest createOTPReq(String userId) {
        OTPRequest req = new OTPRequest();
        req.id="mosip.pre-registration.login.sendotp";
        req.requesttime = APIHelper.getUTCDateTime(null);
        req.data.userId = userId;
        req.data.langCode = "eng";
        req.version="1.0";
        return req;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getHistory(View view) {
        // Do something in response to button click
        Log.d("LOG","getUIN clicked...");
        helper.requestResidentHistory("8539546314","1","111111", getTrId(),null);

    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    void DeviceAuth(){
        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());

        switch (biometricManager.canAuthenticate()){

            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG |
                                android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent,0);

                break;
        }
    }
    BiometricPrompt createBioPrompt(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("MY_APP_TAG", "App onAuthenticationError." + errString);
/*                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_LONG)
                        .show();

 */
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d("MY_APP_TAG", "App onAuthenticationSucceeded.");
                /*
                byte[] encryptedInfo = new byte[0];

                try {
                    encryptedInfo = result.getCryptoObject().getCipher().doFinal(
                                "Sample text".getBytes(Charset.defaultCharset()));
                    Log.d("MY_APP_TAG", "Encrypted information: " +
                            Arrays.toString(encryptedInfo));

                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

*/

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("MY_APP_TAG", "App onAuthenticationFailed.");
                Toast.makeText(getApplicationContext(),
                        "Authentication Failed: " , Toast.LENGTH_LONG)
                        .show();

            }
        });
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate this application.")
                .setSubtitle("Set the subtitle to display.")
                .setDescription("Set the description to display")
                .setNegativeButtonText("Negative Button")
                .build();
        biometricPrompt.authenticate(promptInfo);
        return biometricPrompt;
    }
/*****
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateOTP(View view) {
        // Do something in response to button click
        Log.d("LOG","generate OTP clicked...");

        helper.requestResidentOTP ("5091326710","uin");

    }
****/


}