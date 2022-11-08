package com.example.i_explore;

import androidx.annotation.NonNull;

public class Trip {
    protected int trip_id;
    protected String reporter_name;
    protected String activity_name;
    protected String destination;
    protected String description;
    protected String date;
    protected String risky_assessment;
    protected String time;

    public Trip(int trip_id, String reporter_name, String activity_name, String destination, String description, String risky_assessment, String date, String time) {
        this.trip_id = trip_id;
        this.reporter_name = reporter_name;
        this.activity_name = activity_name;
        this.destination = destination;
        this.description = description;
        this.date = date;
        this.risky_assessment = risky_assessment;
        this.time = time;
    }

//    @NonNull
//    @Override
//    public String toString() {
//        return id + "-" + reporter_name + "-" + activity_name + "-" + description;
//    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setId(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getReporter_name() {
        return reporter_name;
    }

    public void setReporter_name(String reporter_name) {
        this.reporter_name = reporter_name;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRisky_assessment() {
        return risky_assessment;
    }

    public void setRisky_assessment(String risky_assessment) {
        this.risky_assessment = risky_assessment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
