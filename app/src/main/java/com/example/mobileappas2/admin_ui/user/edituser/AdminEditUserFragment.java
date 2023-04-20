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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
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
    private AdminFragmentEdituserBinding binding;
    public int userID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AdminFragmentEdituserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        userID = sharedPref.getInt("adminSelectedUser", 0);

        Button cancelButton = binding.adminCancelNewProductUpdateButtonUser;
        Button deleteButton = binding.adminDeleteUserButtonUser;
        Button updateButton = binding.adminUpdateProductButtonUser;
        cancelButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_admin_user);
        });
        deleteButton.setOnClickListener(view -> deleteElement());
        updateButton.setOnClickListener(view -> update());

        return root;
    }

    public void deleteElement()
    {
        // DELETE THE USER FROM THE DATABASE
        // NEED TO DELETE THERE ORDERS AS WELL AND THEN ALSO DELETE THE ELEMENTS HELD IN PRODUCT_ORDER TO KEEP
        // CLEAN
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        // TODO: ADD IN THE REMOVAL OF ITEMS IN PRODUCT_ORDER
        dbManager.delete(DBDefs.User_Order.TABLE_NAME, "user_id",
                new String[]{Integer.toString(userID)});
        dbManager.delete(DBDefs.User.TABLE_NAME, "user_id",
                new String[]{Integer.toString(userID)});

        dbManager.close();
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_user);
    }

    public void update()
    {
        String name = binding.adminProductnameupdateEntryUser.getText().toString();
        String email = binding.registerEmailEditTextUser.getText().toString();
        String password = binding.adminProductDescriptionUpdateEntryUser.getText().toString();
        String postcode = binding.registerPostcodeEditTextUser.getText().toString();
        String address = binding.registerAddressEditTextUser.getText().toString();
        int number = Integer.parseInt(binding.registerPhoneNumberEditTextUser.getText().toString());
        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }

        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        dbManager.update(name, email, password, postcode, address, date, userID, number, null);
        dbManager.close();
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
