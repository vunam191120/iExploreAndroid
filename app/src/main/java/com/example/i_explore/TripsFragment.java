package com.example.i_explore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.i_explore.databinding.FragmentTripsBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public TripsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripsFragment newInstance(String param1, String param2) {
        TripsFragment fragment = new TripsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ListView tripList;
    public FragmentTripsBinding binding;
    String[] searchItems = {"Activity Name", "Destination", "Date"};
    String keySearch = "activity_name";
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterSearchItems;
    private itemViewModel itemViewModel;
    Toolbar mActionBarToolbar;
    EditText input_search_trip;

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        mActionBarToolbar = getActivity().findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("List Trip");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActionBarToolbar = getActivity().findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("List Trip");

        // Remove detail fragment when delete successfully and redirect to back this list fragment
//        if (container != null) {
//            container.removeAllViews();
//        }
        container.clearDisappearingChildren();

        // ViewModel
        itemViewModel = new ViewModelProvider(requireActivity()).get(itemViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentTripsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        autoCompleteTxt = view.findViewById(R.id.autoCompleteTxt);
        // Set default filed to search
        autoCompleteTxt.setText("Activity Name");

        adapterSearchItems = new ArrayAdapter<String>(getContext(), R.layout.list_item_risky, searchItems);
        autoCompleteTxt.setAdapter(adapterSearchItems);
        autoCompleteTxt.setOnItemClickListener((adapterView, view12, position, id) -> {
            if(position == 0) {
                keySearch = "activity_name";
            } else if (position == 1) {
                keySearch = "destination";
            } else if (position == 2) {
                keySearch = "date";
            }
        });

        // Set On Change Event
        input_search_trip = view.findViewById(R.id.inputSearchTrip);
        input_search_trip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

                List<Trip> trips = dbHelper.getTripsFiltered(keySearch, String.valueOf(charSequence));

                ListAdapter arrayAdapter = new ListAdapter(getActivity(), trips);

                tripList = view.findViewById(R.id.listTrips);
                tripList.setAdapter(arrayAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        List<Trip> trips = dbHelper.getTripsFiltered("activity_name", "");

        ListAdapter arrayAdapter = new ListAdapter(getActivity(), trips);

        tripList = view.findViewById(R.id.listTrips);
        tripList.setAdapter(arrayAdapter);

        tripList.setOnItemClickListener((adapterView, view1, i, l) -> {
            Trip selectedTrip = trips.get(i);

            itemViewModel.setSelectedTrip(selectedTrip);

            // Replace fragment to TripDetailsFragment
            Fragment newFragment = new TripDetailsFragment();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container_trips, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        });

        return view;
    }
}
