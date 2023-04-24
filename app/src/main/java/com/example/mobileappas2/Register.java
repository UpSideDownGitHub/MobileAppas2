package com.example.mobileappas2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Register extends AppCompatActivity {


    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button cancelButton = findViewById(R.id.cancelRegistraction_Button);
        cancelButton.setOnClickListener(view -> cancel());

        Button registerButton = findViewById(R.id.registerUser_Button);
        registerButton.setOnClickListener(view -> register());

        EditText name = findViewById(R.id.registerName_editText);
        EditText password = findViewById(R.id.registerPassword_editText);
        EditText re_password = findViewById(R.id.registerRePassword_editText);
        EditText email = findViewById(R.id.registerEmail_editText);
        EditText re_email = findViewById(R.id.registerReEmail_editText);
        EditText address = findViewById(R.id.registerAddress_editText);
        EditText postcode = findViewById(R.id.registerPostcode_editText);
        EditText phoneNumber = findViewById(R.id.registerPhoneNumber_editText);

        name.setTextColor(getColor(R.color.black));
        password.setTextColor(getColor(R.color.black));
        re_password.setTextColor(getColor(R.color.black));
        email.setTextColor(getColor(R.color.black));
        re_email.setTextColor(getColor(R.color.black));
        address.setTextColor(getColor(R.color.black));
        postcode.setTextColor(getColor(R.color.black));
        phoneNumber.setTextColor(getColor(R.color.black));
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

        name.setTextColor(getColor(R.color.black));
        password.setTextColor(getColor(R.color.black));
        re_password.setTextColor(getColor(R.color.black));
        email.setTextColor(getColor(R.color.black));
        re_email.setTextColor(getColor(R.color.black));
        address.setTextColor(getColor(R.color.black));
        postcode.setTextColor(getColor(R.color.black));
        phoneNumber.setTextColor(getColor(R.color.black));

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
        // need to check if they are all in the right format also need to check if there is anyone else
        // with the same name and if there is then don't add
        DBManager dbManager = new DBManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME},
                DBDefs.User.C_FULL_NAME + " like ?",
                new String[]{name.getText().toString()},
                null, null, null, null);
        if (cursor.getCount() > 0) {
            name.setTextColor(getColor(R.color.white));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_nameinuse,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.getText().toString().matches(EMAIL_PATTERN)) {
            email.setTextColor(getColor(R.color.white));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_validemail,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!postcode.getText().toString().matches("^([A-Z][A-HJ-Y]?\\d[A-Z\\d]? ?\\d[A-Z]{2}|GIR ?0A{2})$")) {
            postcode.setTextColor(getColor(R.color.white));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_validpostcode,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phoneNumber.getText().toString().matches("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")) {
            phoneNumber.setTextColor(getColor(R.color.white));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_validphone,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.getText().toString().equals(re_email.getText().toString())) {
            email.setTextColor(getColor(R.color.white));
            re_email.setTextColor(getColor(R.color.white));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_email_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.getText().toString().equals(re_password.getText().toString())) {
            password.setTextColor(getColor(R.color.white));
            re_password.setTextColor(getColor(R.color.white));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_password_toast,
                    Toast.LENGTH_SHORT).show();
            return;
        }

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
                phoneNumber.getText().toString());

        dbManager.close();

        Toast.makeText(
                getApplicationContext(),
                R.string.register_succesful_toast,
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Register.this, MainMenu.class);
        startActivity(intent);
    }
}