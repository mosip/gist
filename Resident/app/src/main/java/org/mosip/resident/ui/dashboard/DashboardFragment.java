package org.mosip.resident.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.mosip.resident.App;
import org.mosip.resident.MainActivity;
import org.mosip.resident.R;
import org.mosip.resident.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    private EditText editLine1;
    private EditText editLine2;
    private EditText editLine3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_dashboard,
                container, false);
        editLine1 =  view.findViewById(R.id.text_line1);
        editLine2 =  view.findViewById(R.id.text_line2);
        editLine3 =  view.findViewById(R.id.text_line3);

        Button button = (Button) view.findViewById(R.id.button_updateAddress);
        Log.d("Button",button.getText().toString());
        button.setOnClickListener(v -> {
            Log.d("Button", "Button clicked...");
            dashboardViewModel.setAddr1( editLine1.getText().toString().trim());
            dashboardViewModel.setAddr2( editLine2.getText().toString().trim());
            dashboardViewModel.setAddr3( editLine3.getText().toString().trim());
            requestUpdateAddress(dashboardViewModel);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void requestUpdateAddress(DashboardViewModel dashboardViewModel){
        MainActivity activity = (MainActivity) getActivity();
        String [] addrLines = new String[3];
        addrLines[0] = dashboardViewModel.getAddr1().getValue().toString();
        addrLines[1] = dashboardViewModel.getAddr2().getValue().toString();
        addrLines[2] = dashboardViewModel.getAddr3().getValue().toString();

        activity.getHelper().requestChangeOfAddress(App.getUIN(),"UIN",App.getOtp(), activity.getTrId(),addrLines,null);
    }
}