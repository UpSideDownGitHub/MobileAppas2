package com.example.mobileappas2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Users;

public class MainMenu extends AppCompatActivity {
	/*
        is called when the view is created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// inflate the view and get the root to set as the content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
		
		// if the action bar is visible then turn it off
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // check if there are the correct values in the data base if not then initialise it
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_CATEGORY_ID},
                null,null,null,null,null,null);
		// if there is not info in the database the put in the the default categories
        if (cursor.getCount() < 1)
        {
            // if there is not data then insert the dummy data into the table
            // CATEGORIES
            dbManager.insert("All", "All Items");
            dbManager.insert("Misc", "Random Items");
            dbManager.insert("Fruit & Veg", "Fresh fruit & Veg picked daily");
            dbManager.insert("Snack", "All of your favourite snacks");
            dbManager.insert("Frozen", "Frozen goods");
            dbManager.insert("Dairy", "All the best dairy products");

            // PRODUCTS
            dbManager.insert("Apple", "Fresh Red Apples",
                    "06/05/2023", "06/05/2023",
                    0.5f,0.5f, 0.5f, 2);
            dbManager.insert("Pear", "Fresh green Pears",
                    "06/05/2023", "06/05/2023",
                    0.5f,0.5f, 0.5f, 2);
            dbManager.insert("Chocolate", "the best milk chocolate",
                    "06/05/2023", "06/05/2023",
                    1f,1f, 1f, 3);
            dbManager.insert("crisps", "crinkle cut crisps",
                    "06/05/2023", "06/05/2023",
                    0.5f,0.5f, 0.5f, 3);
            dbManager.insert("Peas", "super Juicy Peas frozen to last",
                    "06/05/2023", "06/05/2023",
                    0.5f,0.5f, 0.5f, 4);
            dbManager.insert("Milk", "Straight from the cow",
                    "06/05/2023", "06/05/2023",
                    1.5f,1.5f, 1.5f, 5);

            // USER
            dbManager.insert("example", "example@gmail.com",
                    "pass", "NN4 5DQ", "32 Terrace Walk",
                    "06/05/2023", "06/05/2023",
                    "08767170258", "Swimming in the Sea");

        }
        dbManager.close();

        // attach a listener to the login button
        Button loginButton = findViewById(R.id.login_Button);
        loginButton.setOnClickListener(view -> checkLogin());

        // attach a listener to the register button
        Button registerButton = findViewById(R.id.register_Button);
        registerButton.setOnClickListener(view -> loadRegisterScreen());
    }
	
	/*
		this function will load the register screen
	*/
    public void loadRegisterScreen()
    {
        // load the register screen
        Intent intent = new Intent(MainMenu.this, Register.class);
        startActivity(intent);
    }
	
	// username & password for the admin account
    private String adminUsername = "admin";
    private String adminPassword = "admin";
	
	/*
		this function handles checking if the information the user entered matches any in
		the database or the admin login inforamtion
	*/
    public void checkLogin()
    {
        // get the username and password from there text views
        EditText username = findViewById(R.id.username_editText);
        EditText password = findViewById(R.id.password_editText);

        // CHECK FOR ADMIN LOGIN
        if (username.getText().toString().equals(adminUsername) &&
                password.getText().toString().equals(adminPassword))
        {
			// show welcome message
            Toast.makeText(
                    getApplicationContext(),
                    R.string.welcome_toast,
                    Toast.LENGTH_SHORT).show();

            // load the admin activity
            Intent intent = new Intent(MainMenu.this, AdminActivity.class);
            startActivity(intent);
            return;
        }

        // retrieve the wanted values from the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
		// get the player information where the player name is like that of the one entered
        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME, DBDefs.User.C_PASSWORD, DBDefs.User.C_USER_ID},
                DBDefs.User.C_FULL_NAME + " like ?",
                new String[]{username.getText().toString()},
                null, null, null, null);
        dbManager.close();

        // get the values from the cursor and store them
        Users user = new Users();
		// if there is any inforamtion in the cursor
        if (cursor.getCount() > 0) {
            do {
				// save the inforamtion
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_FULL_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_PASSWORD)));
                user.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.User.C_USER_ID)));
            } while (cursor.moveToNext());
        }
        // if the username is equal to the current username
        if (user.getFullName() != null)
        {
            // if the password matches the expected password for this username
            if (password.getText().toString().equals(user.getPassword()))
            {
                // show welcome text
                Toast.makeText(
                        getApplicationContext(),
                        R.string.welcome_toast,
                        Toast.LENGTH_SHORT).show();

                // load
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                intent.putExtra("playerID", user.getID());
                startActivity(intent);
                return;
            }

            // show password incorrect text
            Toast.makeText(
                    getApplicationContext(),
                    R.string.password_incorrect_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // show username incorrect text
        Toast.makeText(
                getApplicationContext(),
                R.string.username_incorrect_toast,
                Toast.LENGTH_SHORT).show();
    }
}