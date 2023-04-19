package com.example.mobileappas2.ui.user.edituser;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Orders;
import com.example.mobileappas2.Database.DataHolders.Users;
import com.example.mobileappas2.R;
import com.example.mobileappas2.databinding.FragmentEdituserBinding;
import com.example.mobileappas2.databinding.FragmentUserBinding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EditUserFragment extends Fragment {

    private FragmentEdituserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEdituserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button updateUserButton = binding.updateUserInfoButton;
        updateUserButton.setOnClickListener(view -> updateUserInfo(view));

        Button cancelUpdateButton = binding.cancelEditInfoButton;
        cancelUpdateButton.setOnClickListener(view -> cancelUpdate(view));
        return root;
    }

    public void updateUserInfo(View view)
    {
        // GET THE CURRENT USER ID & OPEN THE DATABASE
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int userID = sharedPref.getInt("userID", 0);

        // GET ALL OF THE TEXT THE USER HAS ENTERED
        String username = binding.usernameEditEditText.getText().toString();
        String email = binding.emailEditEditText.getText().toString();
        String password = binding.passwordEditEditText.getText().toString();
        String postcode = binding.postcodeEditEditText.getText().toString();
        String address = binding.addressEditEditText.getText().toString();
        String phoneNumber = binding.phoneNumberEditEditText.getText().toString();
        String oldPassword = binding.oldpasswordEditText.getText().toString();

        // GET THE CURRENT DATA HELD IN THE DATABASE FOR THIS USER
        Users user = new Users();
        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME, DBDefs.User.C_PASSWORD,
                        DBDefs.User.C_PHONE_NUMBER, DBDefs.User.C_ADDRESS,
                        DBDefs.User.C_EMAIL_ADDRESS, DBDefs.User.C_POSTCODE},
                DBDefs.User.C_USER_ID + " like ?",
                new String[]{Integer.toString(userID)},
                null, null, null, null);
        do {
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_FULL_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_PASSWORD)));
            user.setPhoneNumber(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.User.C_PHONE_NUMBER)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_ADDRESS)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_EMAIL_ADDRESS)));
            user.setPostcode(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_POSTCODE)));
            user.setID(userID);
        }while(cursor.moveToNext());

        if (username.isEmpty())
            username = user.getFullName();
        if (email.isEmpty())
            email = user.getEmail();
        if (password.isEmpty())
            password = user.getPassword();
        if (postcode.isEmpty())
            postcode = user.getPostcode();
        if(address.isEmpty())
            address = user.getAddress();
        if (phoneNumber.isEmpty())
            phoneNumber = user.getPhoneNumber().toString();

        if (oldPassword.isEmpty())
        {
            Toast.makeText(
                    getContext(),
                    "Please Enter Old Password",
                    Toast.LENGTH_SHORT).show();
            dbManager.close();
            return;
        }

        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }

        // UPDATE THE CONTENTS OF THE DATABASE
        dbManager.update(username, email, password,
                postcode, address, date, userID,
                Integer.parseInt(phoneNumber), null);
        dbManager.close();

        // return to the user screen
        Navigation.findNavController(view).navigate(R.id.navigation_user);
    }

    public void cancelUpdate(View view)
    {
        // return to the user screen
        Navigation.findNavController(view).navigate(R.id.navigation_user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}