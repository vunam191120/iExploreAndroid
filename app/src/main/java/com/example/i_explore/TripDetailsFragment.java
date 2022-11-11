package com.example.i_explore;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripDetailsFragment newInstance(String param1, String param2) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Toolbar mActionBarToolbar;
    private itemViewModel viewModel;

    // Input form
    String[] riskyItems = {"Easy", "Medium", "Hard"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterRiskyItems;
    Button btnTime, btnUpdate, btnDelete, btnAddExpense, btnUpload;
    EditText input_reporter_name, input_activity_name, input_destination, input_description;
    TextInputEditText input_date;
    TextInputLayout input_risky_assessment;
    String risky_assessment = "Easy", reporter_name = "", time="";
    int hour, minute, trip_id;

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        mActionBarToolbar = getActivity().findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("Trip Details");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnTime = view.findViewById(R.id.btnSetTime);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);
        input_activity_name = view.findViewById(R.id.inputActivityName);
        input_reporter_name = view.findViewById(R.id.inputType);
        input_destination = view.findViewById(R.id.inputDestination);
        input_description = view.findViewById(R.id.inputDescription);
        input_date = view.findViewById(R.id.inputDate);
        input_risky_assessment = view.findViewById(R.id.textInputRisky);

        // DatePicker
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDateInput(input_date);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

//        // Dropdown for risky assessment
//        autoCompleteTxt = view.findViewById(R.id.autoCompleteTxt);
//        adapterRiskyItems = new ArrayAdapter<String>(getContext(), R.layout.list_item_risky, riskyItems);
//        autoCompleteTxt.setAdapter(adapterRiskyItems);
//        autoCompleteTxt.setOnItemClickListener((adapterView, view13, position, id) -> {
//            String item = adapterView.getItemAtPosition(position).toString();
//            risky_assessment = item;
//        });

        // Btn add expense
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ExpenseDetailsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_trip_detail, newFragment)
                        .commit();
                mActionBarToolbar.setTitle("Expense Details");
            }
        });



        // Time picker
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });

        // Get item from TripsFragment by observing of ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(itemViewModel.class);
        viewModel.getSelectedTrip().observe(getViewLifecycleOwner(), item -> {
            input_reporter_name.setText(item.reporter_name);
            input_activity_name.setText(item.activity_name);
            input_destination.setText(item.destination);
            input_description.setText(item.description);
            input_date.setText(item.date);
            btnTime.setText(item.time);
            input_risky_assessment.getEditText().setText(item.risky_assessment);

            // Dropdown for risky assessment, because if using the above, after filling data,
            // dropdown will lost the selection
            autoCompleteTxt = view.findViewById(R.id.autoCompleteTxt);
            adapterRiskyItems = new ArrayAdapter<String>(getContext(), R.layout.list_item_risky, riskyItems);
            autoCompleteTxt.setAdapter(adapterRiskyItems);
            autoCompleteTxt.setOnItemClickListener((adapterView, view13, position, id) -> {
                String itemRisk = adapterView.getItemAtPosition(position).toString();
                risky_assessment = itemRisk;
            });

            trip_id = item.trip_id;
            Log.v(String.valueOf(trip_id), "Trip ID from inside ViewModel");

            // If fill adapter outside ViewModel, it will lost track of trip_id
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

            List<Expense> expenses = dbHelper.getExpenses(trip_id);
            Log.v(expenses.toString(), "Expenses: ");

            ArrayAdapter<Expense> arrayAdapter = new ArrayAdapter<Expense>(getActivity(), android.R.layout.simple_list_item_1, expenses);

            ListView expenseList = view.findViewById(R.id.expenseListView);
            expenseList.setAdapter(arrayAdapter);

            expenseList.setOnItemClickListener((adapterView, view1, i, l) -> {
                Expense selectedExpense = expenses.get(i);
                viewModel.setSelectedExpense(selectedExpense);

                Fragment newFragment = new ExpenseDetailsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_trip_detail, newFragment)
                        .commit();
            });
        });

//        Log.v(String.valueOf(trip_id), "Trip ID from outside ViewModel");

        // Expenses
//        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
//        List<Expense> expenses = dbHelper.getExpenses(trip_id);
//        Log.v(expenses.toString(), "Expenses: ");

//        List<Expense> expenses = dbHelper.getAllExpenses();
//        Log.v(expenses.toString(), "Expenses: ");
//        ArrayAdapter<Expense> arrayAdapter = new ArrayAdapter<Expense>(getActivity(), android.R.layout.simple_list_item_1, expenses);
//
//        ListView expenseList = view.findViewById(R.id.expenseListView);
//        expenseList.setAdapter(arrayAdapter);
//
//        expenseList.setOnItemClickListener((adapterView, view1, i, l) -> {
//            Expense selectedExpense = expenses.get(i);
//            viewModel.setSelectedExpense(selectedExpense);
//
//            Fragment newFragment = new ExpenseDetailsFragment();
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container_trip_detail, newFragment)
//                    .commit();
//        });

        // Update btn
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                String activity_name = input_activity_name.getText().toString();
                String destination = input_destination.getText().toString();
                String description = input_description.getText().toString();
                String date = input_date.getText().toString();

                if(activity_name.equals("")){
                    input_activity_name.setError("Activity Name is required");
                    valid = false;
                }
                if(destination.equals("")) {
                    input_destination.setError("Destination is required");
                    valid = false;
                }
                if(description.equals("")) {
                    input_description.setError("Description is required");
                    valid = false;
                }
                if(date.equals("")) {
                    input_date.setError("Date is required");
                    valid = false;
                } else {
                    // Need to set error back to null, due to after filling data, the error suffix
                    // does not disappear even filled data
                    input_date.setError(null);
                }
                if(valid) {
                    reporter_name = input_reporter_name.getText().toString();
                    if(btnTime.getText().toString() != "") {
                        time = btnTime.getText().toString();
                    }

                    DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

                    Trip newTrip = new Trip(trip_id, reporter_name, activity_name, destination, description,
                            risky_assessment, date, time);

                    dbHelper.updateTrip(newTrip);

                    Toast.makeText(getActivity(), "Updated trip successfully! ", Toast.LENGTH_LONG).show();

                    Fragment newListFragment = new TripsFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_trip_detail, newListFragment)
                            .commit();
                }
            }
        });

        // Delete btn
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirm dialog
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you want to delete this trip?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

                                Boolean deleteTrip = dbHelper.deleteTrip(String.valueOf(trip_id));

                                if(deleteTrip == true) {
                                    Toast.makeText(getActivity(), "Deleted trip successfully ", Toast.LENGTH_LONG).show();
                                }

                                Fragment newListFragment = new TripsFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container_trip_detail, newListFragment)
                                        .commit();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                List<Expense> tripDetails = dbHelper.getExpenses(trip_id);

                ArrayList<Object> Upload = new ArrayList<Object>();

                tripDetails.forEach(element -> {
                    String amount1 = element.getAmount();
                    String type1 = element.getType();
                    String time1 = element.getTime();
                    Integer id1 = element.getExpense_id();
                    Integer trip_id1 = element.getTrip_id();

                    try {
                        Upload.add(new JSONObject()
                                .put("amount", amount1)
                                .put("type", type1)
                                .put("time", time1)
                                .put("id", id1)
                                .put("trip_id", trip_id1));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                String jsonString = null;
                try {
                    jsonString = new JSONObject()
                            .put("userId", "gw001249268")
                            .putOpt("detailList", new JSONArray(Upload)).toString()    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//            String text = "{\"userId\": 1249268, \"detailList\":" + Upload.toString() + "}";

//            Log.e("Array", text);
                Log.e("TAG", "onResponse: Message " );

                ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
                Call<Upload> call = apiInterface.getUserInformation(jsonString.toString());
                Log.e("TAG", "onResponse: Message2 " );

                call.enqueue(new Callback<Upload>() {

                    @Override
                    public void onResponse(Call<Upload> call, Response<Upload> response) {
                        Log.e("TAG", "onResponse: Message " + response.body().getMessage());
                        Log.e("TAG", "onResponse: Message " + response.body().getNames());
                        Log.e("TAG", "onResponse: Message " + response.body().getUserid());
                        Log.e("TAG", "onResponse: Message " + response.body().getUploadResponseCode());
                        Log.e("TAG", "onResponse: Message " + response.body().getNumber());

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();;
                    }

                    @Override
                    public void onFailure(Call<Upload> call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            btnTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        input_date.setText(currentDateString);
    }
}