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

    public void insertDummyData()
    {
        DBManager dbManager = new DBManager(this);
        dbManager.open();

        dbManager.purgeDatabase();

        // INSERTING A CATEGORY
        dbManager.insert("All", "All Items");
        dbManager.insert("Fresh", "fresh food");
        dbManager.insert("Frozen", "frozen food");
        dbManager.insert("Drink", "including: juice, soft, & alcohol");
        dbManager.insert("Snack", "Crisps, Chocolate, & sweets");
        dbManager.insert("Misc", "the random items that we have");

        // INSERTING A PRODUCT
        dbManager.insert("Apple", "fresh apples straight from the tree",
                "10/10/2000", "10/10/2020", new Float(0.5f), new Float(0.5f),
                new Float(0.5f), 1);
        dbManager.insert("Walkers", "crisps straight from the factory",
                "10/10/2000", "10/10/2020", new Float(1.0f), new Float(1.0f),
                new Float(1.0f), 4);
        dbManager.insert("hair Dryer", "for frying hair",
                "10/10/2000", "10/10/2020", new Float(2.5f), new Float(2.5f),
                new Float(2.5f), 5);

        dbManager.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // attach a listener to the login button
        Button loginButton = findViewById(R.id.login_Button);
        loginButton.setOnClickListener(view -> checkLogin());

        // attach a listener to the register button
        Button registerButton = findViewById(R.id.register_Button);
        registerButton.setOnClickListener(view -> loadRegisterScreen());

        Button TEST = findViewById(R.id.TESTDATASPAWN);
        TEST.setOnClickListener(view -> {
            insertDummyData();
        });
    }

    public void loadRegisterScreen()
    {
        // load the register screen
        Intent intent = new Intent(MainMenu.this, Register.class);
        startActivity(intent);
    }
    public void checkLogin()
    {
        // get the username and password from there text views
        EditText username = findViewById(R.id.username_editText);
        EditText password = findViewById(R.id.password_editText);

        // retrieve the wanted values from the database
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME, DBDefs.User.C_PASSWORD, DBDefs.User.C_USER_ID},
                DBDefs.User.C_FULL_NAME + " like ?",
                new String[]{username.getText().toString()},
                null, null, null, null);
        dbManager.close();

        // get the values from the cursor and store them
        Users user = new Users();
        do {
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_FULL_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_PASSWORD)));
            user.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.User.C_USER_ID)));
        } while (cursor.moveToNext());

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