package com.example.i_explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout myDrawerLayout;

//    private TripViewModel viewModel;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_LIST_TRIPS = 1;
    private static final int FRAGMENT_SETTINGS = 2;

    private int myCurrentFragment = FRAGMENT_HOME;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set ViewModel to share data between activities and fragments
//        viewModel = new ViewModelProvider(this).get(TripViewModel.class);
//        viewModel.getSelectedItem().observe(this, item -> {
//            Log.v(item.toString(), "Item get from ViewModel Activity");
//        });

        // Hide app title bar if using default theme
        // getSupportActionBar().hide();

        myDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        myDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            if(myCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                mActionBarToolbar = findViewById(R.id.toolbar);
                mActionBarToolbar.setTitle("i-Explore");
                myCurrentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_list_trip) {
            if(myCurrentFragment != FRAGMENT_LIST_TRIPS) {
                replaceFragment(new TripsFragment());
                mActionBarToolbar = findViewById(R.id.toolbar);
                mActionBarToolbar.setTitle("List Trip");
                myCurrentFragment = FRAGMENT_LIST_TRIPS;
            }
        } else if (id == R.id.nav_setting) {
            if(myCurrentFragment != FRAGMENT_SETTINGS) {
                replaceFragment(new SettingsFragment());
                mActionBarToolbar = findViewById(R.id.toolbar);
                mActionBarToolbar.setTitle("Settings");
                myCurrentFragment = FRAGMENT_SETTINGS;
            }
        }
        myDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            myDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}