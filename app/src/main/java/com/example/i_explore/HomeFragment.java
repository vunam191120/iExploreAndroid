package com.example.i_explore;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String[] riskyItems = {"Easy", "Medium", "Hard"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterRiskyItems;
    Button btnTime, btnSubmit;
    EditText input_reporter_name, input_activity_name, input_destination, input_description;
    TextInputEditText input_date;
    String risky_assessment = "Easy", reporter_name = "", time="";
    int hour, minute;
    Toolbar mActionBarToolbar;

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        mActionBarToolbar = getActivity().findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("i-Explore");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnTime = view.findViewById(R.id.btnSetTime);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnTime.setOnClickListener(view12 -> popTimePicker(view12));

        btnSubmit.setOnClickListener(view1 -> {
            // Validate before submit
            String activity_name = input_activity_name.getText().toString();
            String destination = input_destination.getText().toString();
            String description = input_description.getText().toString();
            String date = input_date.getText().toString();

            Boolean valid = true;

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
                if(btnTime.getText().toString() != "SET TIME") {
                    time = btnTime.getText().toString();
                }

                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                dbHelper.insertTrip(reporter_name, activity_name, destination, risky_assessment, date, time, description);
                Toast.makeText(getActivity(), "Added new trip successfully!", Toast.LENGTH_SHORT).show();

                Fragment newListFragment = new TripsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_home, newListFragment)
                        .commit();
            }
        });

        // Input at add trip form
        input_reporter_name = view.findViewById(R.id.inputType);
        input_activity_name = view.findViewById(R.id.inputActivityName);
        input_destination = view.findViewById(R.id.inputDestination);
        input_date = view.findViewById(R.id.inputDate);
        input_description = view.findViewById(R.id.inputDescription);

        // DatePicker
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDateInput(input_date);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        // Dropdown for risky assessment
        autoCompleteTxt = view.findViewById(R.id.autoCompleteTxt);
        adapterRiskyItems = new ArrayAdapter<String>(getContext(), R.layout.list_item_risky, riskyItems);
        autoCompleteTxt.setAdapter(adapterRiskyItems);
        autoCompleteTxt.setOnItemClickListener((adapterView, view13, position, id) -> {
            String item = adapterView.getItemAtPosition(position).toString();
            risky_assessment = item;
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