package com.example.i_explore;

import androidx.annotation.NonNull;

public class Expense {

    @NonNull
    @Override
    public String toString() {
        return expense_id + ". Type: " + type + ", Amount: $" + amount + ", Time: " + time + " , Comment: " + comment;
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Expense(int expense_id, int trip_id, String comment, String amount, String type, String time) {
        this.expense_id = expense_id;
        this.trip_id = trip_id;
        this.comment = comment;
        this.amount = amount;
        this.type = type;
        this.time = time;
    }

    protected int expense_id;
    protected int trip_id;
    protected String comment;
    protected String amount;

    protected String type;
    protected String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
