package org.mosip.resident.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import org.mosip.resident.App;
import org.mosip.resident.R;
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            String val = sharedPreferences.getString("urlbase", App.getBaseUrl());
            if(!val.equals(App.getBaseUrl())){
                App.setBaseUrl(val.trim());
            }
            val = sharedPreferences.getString("uin",App.getUIN());
            if(!val.equals(App.getUIN())){
                App.setUIN(val.trim());
            }
            //starts live change listener
            sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if(key.equals("urlbase")) {
                        String newVal = sharedPreferences.getString(key, App.getBaseUrl());
                        if (newVal != null && !newVal.equals(""))
                            App.setBaseUrl(newVal.trim());
                            //SharedPreferences.Editor.commit();
                    }
                    else
                    if(key.equals("uin")) {
                        String newVal = sharedPreferences.getString(key, App.getUIN());
                        if (newVal != null && !newVal.equals(""))
                            App.setUIN(newVal.trim());
                        //SharedPreferences.Editor.commit();
                    }
                 }

            });
        }
    }
}