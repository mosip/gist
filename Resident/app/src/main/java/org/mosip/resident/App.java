package org.mosip.resident;


import android.app.Application;
import android.content.Context;

public class App extends Application {

        private static Context mContext;
        private static String baseUrl;
        private static String otp;
        private static String uin;

    @Override
        public void onCreate() {
            super.onCreate();
            mContext = getApplicationContext();
            baseUrl = "https://your-company.mosip.net";
            otp = "111111";
            uin = "12345678";
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

