package com.fyp.electricvehicle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
public class adapterforchargingdevice extends ArrayAdapter<charginpoitnsdataclass> {
    ArrayList<charginpoitnsdataclass> thelist;
    Context context;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View view =layoutInflater.inflate(R.layout.itemslayout,null);
        TextView txtname=view.findViewById(R.id.txtdevicename);
        TextView txtaddress=view.findViewById(R.id.txtaddress);
        txtname.setText(thelist.get(position).getChargeDeviceName());
        txtaddress.setText(thelist.get(position).getAddress());
        return view;
    }
    public adapterforchargingdevice(@NonNull Context context, ArrayList<charginpoitnsdataclass> thelist) {
        super(context, R.layout.itemslayout,thelist);
        this.thelist=thelist;
        this.context=context;
    }

}