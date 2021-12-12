package org.mosip.resident;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class App extends Application {

        private static Context mContext;
        private static String baseUrl;
        private static String otp;
        private static String uin;
        private static Activity activity;
        public static byte[] credBytes;
    public static Activity getActivity() {
        return activity;
    }
    public static void setActivity(Activity val){
        activity = val;
    }

    public static byte[] getDownloadedData() {
        return credBytes;
    }
    public static void setDownloadedData(byte[] val){
        credBytes = val;
    }
    @Override
        public void onCreate() {
            super.onCreate();
            mContext = getApplicationContext();
            baseUrl =  "https://qa-single-rc2.mosip.net";     //"https://qa-triple-rc2.mosip.net";
            otp = "111111";
            uin = "9849326072";
        }
        public static void setUIN(String value){
            uin  = value;
        }
        public static String getUIN(){
            return uin;
        }
        public static void setBaseUrl(String url){
            baseUrl = url;
        }
        public static String getBaseUrl(){
            return baseUrl;
        }
        public static void setOtp(String value){
            otp = value;
        }
        public static String getOtp(){
            return otp;
        }
        public static Context getContext(){
            return mContext;
        }
}

