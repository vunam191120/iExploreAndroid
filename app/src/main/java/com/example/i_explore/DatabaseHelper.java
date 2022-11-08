package com.example.i_explore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TripDB";

    private static final String TABLE_TRIP= "Trip";
    private static final String TABLE_EXPENSE = "Expense";

    public static final String TRIP_ID = "trip_id";
    public static final String REPORTER_NAME = "reporter_name";
    public static final String ACTIVITY_NAME = "activity_name";
    public static final String DESTINATION = "destination";
    public static final String DESCRIPTION = "description";
    public static final String RISKY_ASSESSMENT = "risky_assessment";
    public static final String DATE = "date";
    public static final String TIME = "time";

    public static final String EXPENSE_ID = "expense_id";
    public static final String EXPENSE_TRIP_ID = "trip_id";
    public static final String TYPE = "type";
    public static final String AMOUNT = "amount";
    public static final String EXPENSE_TIME = "time";
    public static final String COMMENT = "comment";

    private SQLiteDatabase database;

    private static final String TABLE_TRIP_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            TABLE_TRIP, TRIP_ID, REPORTER_NAME, ACTIVITY_NAME, DESTINATION, DESCRIPTION, RISKY_ASSESSMENT, DATE, TIME);

    private static final String TABLE_EXPENSE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s INTEGER, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            TABLE_EXPENSE, EXPENSE_ID, EXPENSE_TRIP_ID, TYPE, AMOUNT, EXPENSE_TIME, COMMENT);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_TRIP_CREATE);
        db.execSQL(TABLE_EXPENSE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);

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

        return database.insertOrThrow(TABLE_TRIP, null, rowValues);
    }

    public long insertExpense(int trip_id, String type, String amount, String time, String comment) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(TRIP_ID, trip_id);
        rowValues.put(TYPE, type);
        rowValues.put(AMOUNT, amount);
        rowValues.put(EXPENSE_TIME, time);
        rowValues.put(COMMENT, comment);

        return database.insertOrThrow(TABLE_EXPENSE, null, rowValues);
    }

    public List<Trip> getTrips() {
        Cursor cursor = database.query(TABLE_TRIP, new String[] {TRIP_ID, REPORTER_NAME, ACTIVITY_NAME, RISKY_ASSESSMENT, DATE, TIME, DESTINATION, DESCRIPTION},
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

    public List<Trip> getTripsFiltered(String key, String value) {
//        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TRIP + " WHERE " + key + " LIKE 'S%'", null);
        Cursor cursor = database.query(TABLE_TRIP, new String[] {TRIP_ID, REPORTER_NAME, ACTIVITY_NAME, RISKY_ASSESSMENT, DATE, TIME, DESTINATION, DESCRIPTION},
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
            switch (key) {
                case "activity_name":
                    if(trip.activity_name.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))) {
                        results.add(trip);
                    }
                    break;
                case "destination":
                    if(trip.destination.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))) {
                        results.add(trip);
                    }
                    break;
                case"date":
                    if(trip.date.contains(value)) {
                        results.add(trip);
                    }
                    break;
                default:
                    break;
            }

            cursor.moveToNext();
        }

        return results;
    }


    public List<Expense> getAllExpenses() {
        Cursor cursor = database.query(TABLE_EXPENSE, new String[] {EXPENSE_ID, EXPENSE_TRIP_ID, TYPE, AMOUNT, TIME, COMMENT},
                null, null, null, null, TYPE);

        List<Expense> results = new ArrayList<Expense>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int expense_id = cursor.getInt(0);
            int trip_id = cursor.getInt(1);
            String type = cursor.getString(2);
            String amount = cursor.getString(3);
            String time = cursor.getString(4);
            String comment = cursor.getString(5);

            Expense expense = new Expense(expense_id, trip_id, comment, amount, type, time);
            results.add(expense);

            cursor.moveToNext();
        }

        return results;
    }

    public List<Expense> getExpenses(int tripId) {
        String MY_QUERY = "SELECT b.expense_id, b.trip_id, b.type, b.amount, b.time, b.comment FROM " + TABLE_TRIP + " a INNER JOIN "
                + TABLE_EXPENSE + " b On a.trip_id=b.trip_id WHERE a.trip_id=?";
//        String MY_QUERY = "SELECT b.detail_id, b.exam_id, a.name,b.detail_pictureURL FROM "+ TABLE_EXAM+ " a INNER JOIN "+
//                TABLE_DETAIL + " b ON a.exam_id=b.exam_id WHERE a.exam_id=?";
        Cursor cursor = database.rawQuery(MY_QUERY, new String[]{String.valueOf(tripId)});

        List<Expense> results = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int expense_id = cursor.getInt(0);
            int trip_id = cursor.getInt(1);
            String type = cursor.getString(2);
            String amount = cursor.getString(3);
            String time = cursor.getString(4);
            String comment = cursor.getString(5);

            Expense expense = new Expense(expense_id, trip_id, comment, amount, type, time);

            results.add(expense);
            cursor.moveToNext();
        }
        return results;
    }

    public boolean updateTrip(Trip newTrip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("reporter_name", newTrip.reporter_name);
        cv.put("activity_name", newTrip.activity_name);
        cv.put("destination", newTrip.destination);
        cv.put("description", newTrip.description);
        cv.put("date", newTrip.date);
        cv.put("time", newTrip.time);
        cv.put("risky_assessment", newTrip.risky_assessment);

//        db.update(DATABASE_NAME, cv, "id = ?", new String[]{String.valueOf(newTrip.id)});
        db.update(TABLE_TRIP, cv, TRIP_ID + "=?", new String[]{String.valueOf(newTrip.trip_id)});
        return true;
    }

    public boolean updateExpense(Expense newExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type", newExpense.type);
        cv.put("amount", newExpense.amount);
        cv.put("comment", newExpense.comment);
        cv.put("time", newExpense.time);

        db.update(TABLE_EXPENSE, cv, EXPENSE_ID + "=?", new String[]{String.valueOf(newExpense.expense_id)});
        return true;
    }

    public boolean deleteTrip(String trip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TRIP, TRIP_ID + "=?", new String[]{trip_id}) > 0;
    }

    public boolean deleteExpense(String expense_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EXPENSE, EXPENSE_ID + "=?", new String[]{expense_id}) > 0;
    }
}
