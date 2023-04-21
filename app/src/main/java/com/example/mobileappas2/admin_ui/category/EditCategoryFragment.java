package com.example.mobileappas2.admin_ui.category;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Categories;
import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;
import com.example.mobileappas2.admin_ui.shop.AdminShopAdapter;
import com.example.mobileappas2.databinding.AdminFragmentEditcategoryBinding;

import java.util.ArrayList;

public class EditCategoryFragment extends Fragment {
    private AdminFragmentEditcategoryBinding binding;
    public int currentCatID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AdminFragmentEditcategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // SET UP THE SPINNER TO SHOW THE CATEGORIES AVAILABLE
        // open the database
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        ArrayList<String> catNames = new ArrayList();

        // load the data from the database
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME},
                null,
                null,
                null, null, null, null);
        dbManager.close();

        // convert data in database to a usable list of strings
        do {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            catNames.add(name);
        } while (cursor.moveToNext());
        catNames.add(0, "All");

        // setup the spinner
        Spinner spinner = (Spinner) binding.editCatSpinner;
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                catNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // add listener to add to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                currentCatID = spinner.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
