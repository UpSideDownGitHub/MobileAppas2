package com.example.mobileappas2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileappas2.Database.DBManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button cancelButton = findViewById(R.id.cancelRegistraction_Button);
        cancelButton.setOnClickListener(view -> cancel());

        Button registerButton = findViewById(R.id.registerUser_Button);
        registerButton.setOnClickListener(view -> register());
    }

    public void cancel() {
        // load the main screen
        Intent intent = new Intent(Register.this, MainMenu.class);
        startActivity(intent);
    }

    public void register() {
        // save the new data to the database and then return to the main screen
        EditText name = findViewById(R.id.registerName_editText);
        EditText password = findViewById(R.id.registerPassword_editText);
        EditText re_password = findViewById(R.id.registerRePassword_editText);
        EditText email = findViewById(R.id.registerEmail_editText);
        EditText re_email = findViewById(R.id.registerReEmail_editText);
        EditText address = findViewById(R.id.registerAddress_editText);
        EditText postcode = findViewById(R.id.registerPostcode_editText);
        EditText phoneNumber = findViewById(R.id.registerPhoneNumber_editText);

        if (name.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                re_password.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() ||
                re_email.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty() ||
                postcode.getText().toString().isEmpty() ||
                phoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_empty_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.getText().toString().equals(re_email.getText().toString())) {
            Log.i("DEBUG", "Email: " + email.getText().toString() + "2nd Email: " +
                    re_email.getText().toString());
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_email_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.getText().toString().equals(re_password.getText().toString())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_password_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DBManager dbManager = new DBManager(this);
        dbManager.open();

        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }

        dbManager.insert(name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                postcode.getText().toString(),
                address.getText().toString(),
                date,
                date,
                Integer.parseInt(phoneNumber.getText().toString()));

        dbManager.close();

        Toast.makeText(
                getApplicationContext(),
                R.string.register_succesful_toast,
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Register.this, MainMenu.class);
        startActivity(intent);
    }
}