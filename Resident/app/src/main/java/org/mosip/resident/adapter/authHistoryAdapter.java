package org.mosip.resident.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.mosip.resident.pojo.AuthHistory;

import java.text.ParseException;
import java.util.ArrayList;
import org.mosip.resident.R;
import org.mosip.resident.service.TimeAgo;

public class authHistoryAdapter extends ArrayAdapter<AuthHistory> {

    public authHistoryAdapter(@NonNull Context context, ArrayList<AuthHistory> authHistoryArrayList) {
        super(context, 0, authHistoryArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.history_item_layout, parent, false);
        }
        AuthHistory model = getItem(position);
        TextView slno = listitemView.findViewById(R.id.idSlno);
        TextView idUsed = listitemView.findViewById(R.id.idUsed);
        TextView idAuthMod = listitemView.findViewById(R.id.idAuthModality);
        TextView idAgo = listitemView.findViewById(R.id.idAgo);

        slno.setText(model.serialNumber);
        idUsed.setText(model.idUsed);
        idAuthMod.setText(model.authModality);
        try {
            idAgo.setText(TimeAgo.toTimeAgo( model.date, model.time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listitemView;
    }
}