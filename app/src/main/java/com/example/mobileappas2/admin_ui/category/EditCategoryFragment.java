package com.example.mobileappas2.admin_ui.category;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        updateSpinner();

        // ADD LISTENERS TO THE BUTTONS
        Button newButton = binding.editCatNewButton;
        Button backButton = binding.editCatBackButton;
        Button updateButton = binding.editCatUpdateButton;
        Button deleteButton = binding.editCatDeleteButton;
        backButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_admin_shop);
        });
        newButton.setOnClickListener(view -> newCateogry());
        updateButton.setOnClickListener(view -> updateCategory());
        deleteButton.setOnClickListener(view -> deleteCategory());

        return root;
    }

    public void newCateogry()
    {
        // GET THE NAME AND DESCRIPTION FROM THE ENTRY POINTS
        String name = binding.editCatNameEntry.getText().toString();
        String desc = binding.editCatDescEntry.getText().toString();

        // CREATE A NEW CATEGORY
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        dbManager.insert(name, desc);
        dbManager.close();

        updateSpinner();
    }
    public void updateCategory()
    {
        if (currentCatID == 0 || currentCatID == 1)
        {
            Toast.makeText(
                    getContext(),
                    "Cant Edit This Categories",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // GET THE NAME AND DESCRIPTION FROM THE ENTRY POINTS
        String name = binding.editCatNameEntry.getText().toString();
        String desc = binding.editCatDescEntry.getText().toString();

        // CREATE A NEW CATEGORY
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        dbManager.update(name, desc, currentCatID, null);
        dbManager.close();

        updateSpinner();
    }
    public void deleteCategory()
    {
        if (currentCatID == 0 || currentCatID == 1)
        {
            Toast.makeText(
                    getContext(),
                    "Cant Delete This Categories",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // DELETE THE CATEGORY
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBDefs.Product.C_CATEGORY_ID, 1);
        dbManager.update(DBDefs.Product.TABLE_NAME,
                contentValues,
                "category_id=?",
                new String[]{Integer.toString(currentCatID)});

        // delete the actual category
        dbManager.delete(DBDefs.Category.TABLE_NAME, "category_id",
                new String[]{Integer.toString(currentCatID + 1)});
        dbManager.close();
        updateSpinner();
    }

    public void updateSpinner()
    {
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
        dbManager.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
