package com.example.i_explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter {
    public ListAdapter (Context context, ArrayList<Trip> tripArrayList) {
        super(context, R.layout.list_item, tripArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Trip trip = (Trip) getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,false);
        }

        TextView activityName = convertView.findViewById(R.id.txtActivityName);
        TextView date = convertView.findViewById(R.id.txtDate);
        TextView time = convertView.findViewById(R.id.txtTime);
        TextView description = convertView.findViewById(R.id.txtDescription);

        activityName.setText(trip.activity_name);
        date.setText(trip.date);
        time.setText(trip.time);
        description.setText(trip.description);

        return super.getView(position, convertView, parent);
    }
}
