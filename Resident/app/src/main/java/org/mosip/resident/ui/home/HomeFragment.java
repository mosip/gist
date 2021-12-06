package org.mosip.resident.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import org.mosip.resident.App;
import org.mosip.resident.MainActivity;
import org.mosip.resident.R;
import org.mosip.resident.adapter.authHistoryAdapter;
import org.mosip.resident.databinding.FragmentHomeBinding;
import org.mosip.resident.pojo.AuthHistory;
import org.mosip.resident.service.APICallback;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements APICallback {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    GridView authHistoryView;
    authHistoryAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        authHistoryView = view.findViewById(R.id.idHistoryView);
        //authHistoryList = new ArrayList<AuthHistory>();
        getHistory(view);
        adapter = new authHistoryAdapter(getActivity(), homeViewModel.getAuthHistoryList());
        authHistoryView.setAdapter(adapter);

      //  TableView tableView = (TableView) view.findViewById(R.id.tableView);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getHistory(View view) {
        // Do something in response to button click
        Log.d("LOG","getUIN clicked...");
        MainActivity activity = (MainActivity) getActivity();
       activity.getHelper().requestResidentHistory(App.getUIN(),"1",App.getOtp(), activity.getTrId(), this);

        //authHistoryView.refreshDrawableState();
    }

    @Override
    public void onSuccess(Object param) {
        List<AuthHistory> hist = (List<AuthHistory>) param;

        if(hist != null)
            for(AuthHistory ah: hist){
                homeViewModel.getAuthHistoryList().add(ah);
            }
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onError(Object param) {

    }
}