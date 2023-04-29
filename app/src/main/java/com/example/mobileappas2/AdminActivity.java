package com.example.mobileappas2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mobileappas2.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {
	// private variables
    private ActivityAdminBinding binding;
	
	/*
        is called whenthe view is created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// inflate the view and get the root to set as the content view
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
		
		// if the action bar is visable then turn it off
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
		
		// set up the Navgiation view
        BottomNavigationView navView = findViewById(R.id.nav_view_admin);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_admin_shop,
                R.id.navigation_admin_user,
                R.id.navigation_admin_order)
                .build();
		// create the navcontroller 
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewAdmin, navController);
    }

}