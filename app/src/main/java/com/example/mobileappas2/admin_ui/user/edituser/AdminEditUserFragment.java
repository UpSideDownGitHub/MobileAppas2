package com.example.mobileappas2.admin_ui.user.edituser;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Users;
import com.example.mobileappas2.R;
import com.example.mobileappas2.admin_ui.user.AdminUserData;
import com.example.mobileappas2.admin_ui.user.AdminUsersAdapter;
import com.example.mobileappas2.databinding.AdminFragmentEdituserBinding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class AdminEditUserFragment extends Fragment {
	// public varaibles
    public int userID;
	
    // private variables
    private AdminFragmentEdituserBinding binding;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/*
        is called whenthe view is created
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		// inflate the view and get the root
        binding = AdminFragmentEdituserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
		
		// load shared preferences and get the current user that has been selected
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        userID = sharedPref.getInt("adminSelectedUser", 0);
		
		// add listenters to all of the buttons on the layout
        Button cancelButton = binding.adminCancelNewProductUpdateButtonUser;
        Button deleteButton = binding.adminDeleteUserButtonUser;
        Button updateButton = binding.adminUpdateProductButtonUser;
        cancelButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_admin_user);
        });
        deleteButton.setOnClickListener(view -> deleteElement());
        updateButton.setOnClickListener(view -> update());

        // GET ALL OF THE TEXT THE USER HAS ENTERED
        TextView name = binding.adminProductnameupdateEntryUser;
        TextView email = binding.registerEmailEditTextUser;
        TextView password = binding.adminProductDescriptionUpdateEntryUser;
        TextView postcode = binding.registerPostcodeEditTextUser;
        TextView address = binding.registerAddressEditTextUser;
        TextView number = binding.registerPhoneNumberEditTextUser;
        TextView hobby = binding.adminUpdateHobbyEditText;

        // GET THE CURRENT DATA HELD IN THE DATABASE FOR THIS USER
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        Users user = new Users();
        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME, DBDefs.User.C_PASSWORD,
                        DBDefs.User.C_PHONE_NUMBER, DBDefs.User.C_ADDRESS,
                        DBDefs.User.C_EMAIL_ADDRESS, DBDefs.User.C_POSTCODE,
                        DBDefs.User.C_HOBBIES},
                DBDefs.User.C_USER_ID + " like ?",
                new String[]{Integer.toString(userID)},
                null, null, null, null);
        do {
			// save the data for this current user
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_FULL_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_PASSWORD)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_PHONE_NUMBER)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_ADDRESS)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_EMAIL_ADDRESS)));
            user.setPostcode(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_POSTCODE)));
            user.setHobby(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_HOBBIES)));
            user.setID(userID);
        }while(cursor.moveToNext());
		
		// set the text views to hold the existing data
        name.setText(user.getFullName());
        password.setText(user.getPassword());
        number.setText(user.getPhoneNumber());
        address.setText(user.getAddress());
        email.setText(user.getEmail());
        postcode.setText(user.getPostcode());
        hobby.setText(user.getHobby());

        dbManager.close();
		
		// retrn root
        return root;
    }
	
	/*
		this function will handle delting users from the database
	*/
    public void deleteElement()
    {
        // DELETE THE USER FROM THE DATABASE
        // NEED TO DELETE THERE ORDERS AS WELL AND THEN ALSO DELETE THE ELEMENTS HELD IN PRODUCT_ORDER TO KEEP
        // CLEAN
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        // remove the user and also remove all of there orders
        dbManager.delete(DBDefs.User_Order.TABLE_NAME, "user_id",
                new String[]{Integer.toString(userID)});
        dbManager.delete(DBDefs.User.TABLE_NAME, "user_id",
                new String[]{Integer.toString(userID)});
		
		// close the data base and load to the admin user page
        dbManager.close();
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_user);
    }
	
	/*
		this function when called will update all of the information entererd by the admin
		about the current user
	*/
    public void update()
    {
		// load all of the information that has been entered
        String name = binding.adminProductnameupdateEntryUser.getText().toString();
        String email = binding.registerEmailEditTextUser.getText().toString();
        String password = binding.adminProductDescriptionUpdateEntryUser.getText().toString();
        String postcode = binding.registerPostcodeEditTextUser.getText().toString();
        String address = binding.registerAddressEditTextUser.getText().toString();
        String number = binding.registerPhoneNumberEditTextUser.getText().toString();
        String hobby = binding.adminUpdateHobbyEditText.getText().toString();
		
		// get the current date
        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }
		
		// if any of the ented peices of data are empty then tell the user that 
		// they cant be empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || postcode.isEmpty() ||
                address.isEmpty() || number.isEmpty() || hobby.isEmpty())
        {
			// tell user that they cant be empty
            Toast.makeText(
                    getContext(),
                    "Make Sure No Entries Are Empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		// if the email is not the correct format then warn the user
        if (!email.matches(EMAIL_PATTERN))
        {
            Toast.makeText(
                    getContext(),
                    "Please Enter a Valid Email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		// if the postcode is not the right format then warn the user
        if (!postcode.matches("^([A-Z][A-HJ-Y]?\\d[A-Z\\d]? ?\\d[A-Z]{2}|GIR ?0A{2})$"))
        {
            Toast.makeText(
                    getContext(),
                    "Please Enter a Valid Postcode",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		// if the phone number is not the right format then warn the user
        if (!number.matches("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$"))
        {
            Toast.makeText(
                    getContext(),
                    "Please Enter a Valid Phone Number",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		
		// open the data base
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
		// get all users with the same name as the entered name
        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME},
                DBDefs.User.C_FULL_NAME + " like ?",
                new String[]{name},
                null, null, null, null);
		// if there is a user with the current entered name
        if (cursor.getCount() > 0)
        {
			//warn the user that there name is a duplicate
            Toast.makeText(
                    getContext(),
                    "User Name Already Taken",
                    Toast.LENGTH_SHORT).show();
            dbManager.close();
            return;
        }
		// get all the users with he same email as the current user
        Cursor cursor2 = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_EMAIL_ADDRESS},
                DBDefs.User.C_EMAIL_ADDRESS + " like ?",
                new String[]{email},
                null, null, null, null);
		// if there are any users with the game email then warn the user
        if (cursor2.getCount() > 0)
        {
            Toast.makeText(
                    getContext(),
                    "Email Already Taken",
                    Toast.LENGTH_SHORT).show();
            dbManager.close();
            return;
        }
		
		// update the current player with the new infroamtion that has been entered
        dbManager.update(name, email, password, postcode, address, date, userID, number, hobby,null);
        dbManager.close();
		
		// load the admin user layout
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_user);
    }

	// used to properly destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
