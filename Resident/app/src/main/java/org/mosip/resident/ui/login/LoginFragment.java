package org.mosip.resident.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.mosip.resident.App;
import org.mosip.resident.MainActivity;
import org.mosip.resident.databinding.FragmentLoginBinding;
import  org.mosip.resident.R;
import org.mosip.resident.service.APICallback;
import org.mosip.resident.service.APIHelper;

public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private TextView msgArea;
    private EditText txtOtp;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_login,
                container, false);
        Button button = (Button) view.findViewById(R.id.button_getotp);
        Log.d("Button",button.getText().toString());
        button.setOnClickListener(v -> {
            Log.d("Button", "Button clicked...");

            generateOTP(v);
            toggleVisible(1,view);
        });

        Button button1 = (Button) view.findViewById(R.id.button_downloadcreds);
        Log.d("Button",button1.getText().toString());
        button1.setOnClickListener(v -> {
            Log.d("Button", "Button clicked...");
            downloadCreds(view);
            //getEUIN(v);
        });
        Button button2 = (Button) view.findViewById(R.id.button_reqcreds);
        Log.d("Button",button2.getText().toString());
        button2.setOnClickListener(v -> {
            Log.d("Button", "Button 2 clicked...");
            reqCreds(view);

        });
        Button button3 = (Button) view.findViewById(R.id.button_status);
        Log.d("Button",button3.getText().toString());
        button3.setOnClickListener(v -> {
            Log.d("Button", "Button 3 clicked...");
            getCredReqStatus(view);

        });
        Button button4 = (Button) view.findViewById(R.id.button_vid);
        Log.d("Button",button4.getText().toString());
        button4.setOnClickListener(v -> {
            Log.d("Button", "Button 3 clicked...");
            getVid(view);

        });

        msgArea = view.findViewById(R.id.label_msgarea);
        msgArea.setText("");
        txtOtp = view.findViewById(R.id.text_otpvalue);
        toggleVisible(0,view);
        txtOtp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String otpVal =getOTPValue();
                if(otpVal.trim().length() >3) {
                    App.setOtp(otpVal);
                    toggleVisible(2,view);
                }
                else{
           //         toggleVisible(1,view);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        return view;
    }
    public String getOTPValue(){
        return txtOtp.getText().toString();
    }
    public void setMsg(String msg){
        msgArea.setText(msg);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    /*
        if toggleVal ==0, then enable Generate OTP and disable all other buttons/OTP text
        if toggleVal==1, then disable Generate OTP and enable OTP Text, disable other buttons
        if toggleVal==2, then disable Generate OTP and enable OTP Text, enable other buttons

    */
    static int[] control_ids = { R.id.button_getotp, R.id.text_otpvalue,
            R.id.button_reqcreds,R.id.button_status,
            R.id.button_vid, R.id.button_downloadcreds};

    public void toggleVisible(int  toggleVal, View vw){
/*
        switch(toggleVal){
            case 0:
                vw.findViewById(control_ids[0]).setEnabled(true);
                for(int i=1; i< control_ids.length; i++){
                    vw.findViewById(control_ids[i]).setEnabled(false);
                }
                break;
            case 1:
                for(int i=0; i< control_ids.length; i++){
                    vw.findViewById(control_ids[i]).setEnabled(false);
                }
                vw.findViewById(control_ids[1]).setEnabled(true);
                break;
            case 2:
                for(int i=0; i< control_ids.length; i++){
                    vw.findViewById(control_ids[i]).setEnabled(false);
                }
                vw.findViewById(control_ids[1]).setEnabled(true);
                vw.findViewById(control_ids[2]).setEnabled(true);
                vw.findViewById(control_ids[4]).setEnabled(true);
                break;
            case 3: //case when requestCred is clicked - enable status and download
                for(int i=0; i< control_ids.length; i++){
                    vw.findViewById(control_ids[i]).setEnabled(false);
                }
                break;

        }
*/
    }
    private void getVid(View view){
        MainActivity activity = (MainActivity) getActivity();
        // Do something in response to button click
        Log.d("LOG","Get VID clicked...");
        activity.getHelper().requestVID(App.getUIN(), "UIN", activity.getTrId(),
                App.getOtp(), "Perpetual", new APICallback() {
                    @Override
                    public void onSuccess(Object param) {
                        setMsg(param.toString());
                    }

                    @Override
                    public void onError(Object param) {
                        setMsg(param.toString());
                    }
                });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateOTP(View view) {
        MainActivity activity = (MainActivity) getActivity();
        // Do something in response to button click
        Log.d("LOG","generate OTP clicked...");
        activity.setTrId(APIHelper.genRandomNumbers(10));
        activity.onKeyDown(KeyEvent.KEYCODE_MENU, null);
       activity.getHelper().requestResidentOTP(App.getUIN(), "UIN", activity.getTrId(),
               new APICallback() {
                   @Override
                   public void onSuccess(Object param) {
                       setMsg(param.toString());
                   }

                   @Override
                   public void onError(Object param) {
                       setMsg(param.toString());
                   }
               });

    }
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getEUIN(View view) {
        MainActivity activity = (MainActivity) getActivity();
        App.setOtp( getOTPValue());
        Log.d("LOG","getUIN clicked...");
        activity.getHelper().requestResidentEUIN(App.getUIN(),"UIN",App.getOtp(),activity.getTrId());

    }*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reqCreds(View view) {
        MainActivity activity = (MainActivity) getActivity();
        App.setOtp( getOTPValue());

        //cardType: euin | qrcode
        Log.d("LOG","reqCreds started...");
        activity.getHelper().requestResidentCredentials(App.getUIN(),App.getOtp(), "euin", activity.getTrId(),
                new APICallback() {
                    @Override
                    public void onSuccess(Object param) {
                        loginViewModel.setRequestId(param.toString());
                        setMsg(param.toString());
                    }

                    @Override
                    public void onError(Object param) {
                        //Toast.makeText(getActivity().getApplicationContext(),param.toString(),Toast.LENGTH_LONG).show();
                        toggleVisible(0,view);
                        setMsg(param.toString());
                    }
                });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCredReqStatus(View view) {
        MainActivity activity = (MainActivity) getActivity();



        Log.d("LOG","getCredReqStatus started...");
        activity.getHelper().getResidentCredentialRequestStatus( loginViewModel.getRequestId(),
                new APICallback() {
                    @Override
                    public void onSuccess(Object param) {
                        loginViewModel.setRequestStatus(param.toString());
                        setMsg(param.toString());
                    }

                    @Override
                    public void onError(Object param) {
                        Toast.makeText(getActivity().getApplicationContext(),param.toString(),Toast.LENGTH_LONG).show();
                        setMsg(param.toString());
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadCreds(View view) {
        MainActivity activity = (MainActivity) getActivity();

        Log.d("LOG","reqCreds started...");
        activity.getHelper().downloadResidentCredentials(loginViewModel.getRequestId(), new APICallback() {
            @Override
            public void onSuccess(Object param) {
                setMsg(param.toString());

            }

            @Override
            public void onError(Object param) {
                setMsg(param.toString());

            }
        });

    }

}
