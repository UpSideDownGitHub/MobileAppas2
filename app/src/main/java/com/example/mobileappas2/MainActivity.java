package com.example.mobileappas2;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.Database;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mobileappas2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	// private variables
    private ActivityMainBinding binding;
	
	/*
        is called whenthe view is created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// inflate the view and get the root to set as the content view
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

		// if the action bar is visable then turn it off
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // get the current user ID from the intent and save it to shared preferences
        int i = getIntent().getExtras().getInt("playerID");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("userID", i);
        editor.apply();

        // setup the nav bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
		// Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_shop,
                R.id.navigation_user,
                R.id.navigation_basket,
                R.id.navigation_edituser)
                .build();
		// create the navcontroller 
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navController.navigate(R.id.navigation_shop);
    }

}