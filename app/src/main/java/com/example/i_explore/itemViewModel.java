package com.example.i_explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class itemViewModel extends ViewModel {
    private final MutableLiveData<Trip> selectedTrip = new MutableLiveData<Trip>();
    public final MutableLiveData<Expense> selectedExpense = new MutableLiveData<Expense>();

    public void setSelectedTrip(Trip trip) {
        selectedTrip.setValue(trip);
    }
    public void setSelectedExpense(Expense expense) { selectedExpense.setValue(expense);}

    public LiveData<Trip> getSelectedTrip() {
        return selectedTrip;
    }

    public LiveData<Expense> getSelectedExpense() {
        return selectedExpense;
    }
}
