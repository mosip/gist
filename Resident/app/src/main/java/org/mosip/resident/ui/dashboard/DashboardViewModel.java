package org.mosip.resident.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

   // private MutableLiveData<String> mText;
    private MutableLiveData<String> mAddr1;
    private MutableLiveData<String> mAddr2;
    private MutableLiveData<String> mAddr3;

    public DashboardViewModel() {
     //   mText = new MutableLiveData<>();
        mAddr1 =new MutableLiveData<>();
        mAddr2 =new MutableLiveData<>();
        mAddr3 =new MutableLiveData<>();


        //   mText.setValue("This is dashboard fragment");
    }
    public void setAddr1(String v){
        mAddr1.setValue(v);
    }
    public void setAddr2(String v){
        mAddr2.setValue(v);
    }
    public void setAddr3(String v){
        mAddr3.setValue(v);
    }
    public LiveData<String> getAddr1() {
        return mAddr1;
    }
    public LiveData<String> getAddr2() {
        return mAddr2;
    }
    public LiveData<String> getAddr3() {
        return mAddr3;
    }

}