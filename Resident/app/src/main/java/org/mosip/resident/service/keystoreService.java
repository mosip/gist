package org.mosip.resident.service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;


public class keystoreService {
    KeyStore ks;
    public keystoreService(AppCompatActivity app){
        try {
                app.startActivity(new Intent("com.android.credentials.UNLOCK"));
                ks = KeyStore.getInstance();
        } catch (ActivityNotFoundException e) {
            Log.e("TAG", "No UNLOCK activity: " + e.getMessage(), e);
        }
    }
    public boolean storeKey(String key, String value){
        return ks.put(key, value.getBytes());
    }
    public String getKey(String key){
        byte[] keyBytes = ks.get(key);
        return new String(keyBytes);
    }

}
