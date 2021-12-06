package org.mosip.resident.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.mosip.resident.pojo.AuthHistory;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<AuthHistory>> authHistoryList;

    public HomeViewModel() {

        authHistoryList = new MutableLiveData<>();
        authHistoryList.setValue(new ArrayList<>());
    }
    public ArrayList<AuthHistory> getAuthHistoryList(){
        return authHistoryList.getValue();
    }


}