package com.example.i_explore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Trips";

    public static final String ID = "id";
    public static final String REPORTER_NAME = "report_name";
    public static final String ACTIVITY_NAME = "activity_name";
    public static final String DESTINATION = "destination";
    public static final String DESCRIPTION = "description";
    public static final String RISKY_ASSESSMENT = "risky_assessment";
    public static final String DATE = "date";
    public static final String TIME = "time";

    private SQLiteDatabase database;

    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            DATABASE_NAME, ID, REPORTER_NAME, ACTIVITY_NAME, DESTINATION, DESCRIPTION, RISKY_ASSESSMENT, DATE, TIME);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        Log.v(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " +
                newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertTrip(String reporter_name,String activity_name, String destination, String risky_assessment, String date, String time, String description) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(REPORTER_NAME, reporter_name);
        rowValues.put(ACTIVITY_NAME, activity_name);
        rowValues.put(DESTINATION, destination);
        rowValues.put(DESCRIPTION, description);
        rowValues.put(RISKY_ASSESSMENT, risky_assessment);
        rowValues.put(DATE, date);
        rowValues.put(TIME, time);

        return database.insertOrThrow(DATABASE_NAME, null, rowValues);
    }

    public List<Trip> getTrips() {
        Cursor cursor = database.query(DATABASE_NAME, new String[] {ID, REPORTER_NAME, ACTIVITY_NAME, RISKY_ASSESSMENT, DATE, TIME, DESTINATION, DESCRIPTION},
                null, null, null, null, ACTIVITY_NAME);

        List<Trip> results = new ArrayList<Trip>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String reporter_name = cursor.getString(1);
            String activity_name = cursor.getString(2);
            String risky_assessment = cursor.getString(3);
            String date = cursor.getString(4);
            String time = cursor.getString(5);
            String destination = cursor.getString(6);
            String description = cursor.getString(7);

            Trip trip = new Trip(id,reporter_name,activity_name,destination,description,risky_assessment,date,time);
            results.add(trip);

            cursor.moveToNext();
        }

        return results;

    }
}
