package com.example.i_explore;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpenseDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseDetailsFragment newInstance(String param1, String param2) {
        ExpenseDetailsFragment fragment = new ExpenseDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Button btnTime, btnAdd, btnUpdate, btnDelete;
    int hour, minute, trip_id, expense_id;
    String time, comment = "";
    private itemViewModel viewModel;
    EditText input_type, input_amount, input_comment;

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
        View view = inflater.inflate(R.layout.fragment_expense_details, container, false);

        input_amount = view.findViewById(R.id.inputAmount);
        input_type = view.findViewById(R.id.inputType);
        input_comment = view.findViewById(R.id.inputComment);
        btnTime = view.findViewById(R.id.btnSetTime);
        btnAdd = view.findViewById(R.id.btnAddExpense);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);

        // Time picker
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });

        // View model
        viewModel = new ViewModelProvider(requireActivity()).get(itemViewModel.class);
        viewModel.getSelectedTrip().observe(getActivity(), item -> {
            trip_id = item.trip_id;
        });
        viewModel.getSelectedExpense().observe(getActivity(), item -> {
            input_type.setText(item.type);
            input_amount.setText(item.amount);
            input_comment.setText(item.comment);
            btnTime.setText(item.time);
            expense_id = item.expense_id;

            Log.v(String.valueOf(expense_id), "Expense_ID");
        });

        // Add btn
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = input_amount.getText().toString();
                String type = input_type.getText().toString();
                time = btnTime.getText().toString();
                comment = input_comment.getText().toString();

//                Toast.makeText(getActivity(), "Time: " + time, Toast.LENGTH_SHORT).show();

                Boolean valid = true;

                if(amount.equals("")) {
                    input_amount.setError("Amount is required!");
                    valid = false;
                }
                if(type.equals("")) {
                    input_type.setError("Type is required!");
                    valid = false;
                }
                if(time.equals("Set Time")) {
                    Toast.makeText(getActivity(), "Time is required!", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if(valid) {
                    time = btnTime.getText().toString();

                    DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                    dbHelper.insertExpense(trip_id, type, amount, time, comment);
                    Toast.makeText(getActivity(), "Added new expense successfully", Toast.LENGTH_LONG).show();
//                    Fragment newFragment = new TripsFragment();
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container_expense_detail, newFragment)
//                            .commit();
                }
            }
        });

        // Update btn
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                String amount = input_amount.getText().toString();
                String type = input_type.getText().toString();
                time = btnTime.getText().toString();
                comment = input_comment.getText().toString();

                if(amount.equals("")) {
                    input_amount.setError("Amount is required!");
                    valid = false;
                }
                if(type.equals("")) {
                    input_type.setError("Type is required!");
                    valid = false;
                }
                if(time.equals("Set Time")) {
                    Toast.makeText(getActivity(), "Time is required!", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if(valid) {
                    time = btnTime.getText().toString();

                    DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                    Expense newExpense = new Expense(expense_id, trip_id, comment, amount, type, time);

                    dbHelper.updateExpense(newExpense);

                    Toast.makeText(getActivity(), "Updated expense successfully! ", Toast.LENGTH_LONG).show();

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
                        .setMessage("Are you want to delete this expense?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

                                Boolean deleteExpense = dbHelper.deleteExpense(String.valueOf(expense_id));

                                if(deleteExpense == true) {
                                    Toast.makeText(getActivity(), "Deleted expense successfully ", Toast.LENGTH_LONG).show();
                                }

                                Fragment newListFragment = new TripsFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container_expense_detail, newListFragment)
                                        .commit();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
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
}