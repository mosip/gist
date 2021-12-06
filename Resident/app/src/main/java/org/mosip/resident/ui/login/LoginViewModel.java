package org.mosip.resident.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> requestId;
    private MutableLiveData<String> requestStatus;

    public LoginViewModel() {
        requestId = new MutableLiveData<>();
        requestStatus = new MutableLiveData<>();

    }
    public void setRequestId(String reqId){
        requestId.setValue(reqId);
    }
    public String getRequestId() {
        return requestId.getValue();
    }

    public void setRequestStatus(String reqId){
        requestStatus.setValue(reqId);
    }
    public String getRequestStatus() {
        return requestStatus.getValue();
    }
    public void onRequestUpdateAddress(){

    }
}
