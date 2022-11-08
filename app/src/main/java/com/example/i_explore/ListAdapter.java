package com.example.i_explore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Trip> {
    public ListAdapter (Context context, List<Trip> tripArrayList) {
//        super(context, R.layout.list_item, R.id.txtActivityName, tripArrayList);
        super(context, R.layout.list_item, R.id.itemTrip, tripArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,false);
        }

        Trip trip = getItem(position);

        TextView activityName = convertView.findViewById(R.id.txtActivityName);
        activityName.setText(trip.activity_name);

        TextView date = convertView.findViewById(R.id.txtDate);
        date.setText(trip.date);

        TextView time = convertView.findViewById(R.id.txtTime);
        time.setText(trip.time);

        TextView destination = convertView.findViewById(R.id.txtDestination);
        destination.setText(trip.destination);

        TextView description = convertView.findViewById(R.id.txtDescription);
        description.setText(trip.description);


        return super.getView(position, convertView, parent);
    }
}
