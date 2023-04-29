package com.example.mobileappas2.admin_ui.category;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import androidx.constraintlayout.motion.widget.Debug;
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
    public int currentCatID = 0;
    public Categories oldCat;

    ArrayList<String> catNames = new ArrayList();
    ArrayList<Categories> categoryList = new ArrayList();

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
        newButton.setOnClickListener(view -> newCategory());
        updateButton.setOnClickListener(view -> updateCategory());
        deleteButton.setOnClickListener(view -> deleteCategory());

        return root;
    }

    public void newCategory()
    {
        // GET THE NAME AND DESCRIPTION FROM THE ENTRY POINTS
        String name = binding.editCatNameEntry.getText().toString();
        String desc = binding.editCatDescEntry.getText().toString();

        if (name.isEmpty() || desc.isEmpty())
        {
            Toast.makeText(
                    getContext(),
                    "Make Sure all fields are filled in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // CREATE A NEW CATEGORY
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_CATEGORY_ID},
                DBDefs.Category.C_NAME + " like ?",
                new String[]{name},
                null,null,null,null);
        if (cursor.getCount() > 0)
        {
            Toast.makeText(
                    getContext(),
                    "There is already a category with that name",
                    Toast.LENGTH_SHORT).show();
            dbManager.close();
            return;
        }
        dbManager.insert(name, desc);
        dbManager.close();

        updateSpinner();
    }
    public void updateCategory()
    {
        if (currentCatID == 0 || currentCatID == 1 || currentCatID == 2)
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

        if (name.isEmpty() || desc.isEmpty())
        {
            Toast.makeText(
                    getContext(),
                    "Make Sure all fields are filled in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // CREATE A NEW CATEGORY
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        // if the username all ready exists but is not this current object
        if (!oldCat.getName().equals(name)) {
            Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                    new String[]{DBDefs.Category.C_CATEGORY_ID},
                    DBDefs.Category.C_NAME + " like ?",
                    new String[]{name},
                    null, null, null, null);
            if (cursor.getCount() > 0) {
                Toast.makeText(
                        getContext(),
                        "There is already a category with that name",
                        Toast.LENGTH_SHORT).show();
                dbManager.close();
                return;
            }
        }
        dbManager.update(name, desc, currentCatID, null);
        dbManager.close();

        updateSpinner();
    }
    public void deleteCategory()
    {
        if (currentCatID == 0 || currentCatID == 1 || currentCatID == 2)
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
        catNames = new ArrayList();
        categoryList.clear();

        // load the data from the database
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME, DBDefs.Category.C_CATEGORY_ID},
                null,
                null,
                null, null, null, null);
        dbManager.close();

        // convert data in database to a usable list of strings
        do {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            catNames.add(name);

            Categories tempCat = new Categories();
            tempCat.setName(name);
            tempCat.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.Category.C_CATEGORY_ID)));
            categoryList.add(tempCat);
        } while (cursor.moveToNext());

        // setup the spinner
        Spinner spinner = (Spinner) binding.editCatSpinner;
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getActivity(),
                R.layout.spinner_item,
                catNames);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
        setNameandDescription(0);

        // add listener to add to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setNameandDescription(spinner.getSelectedItemPosition());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        dbManager.close();
    }

    public void setNameandDescription(int position)
    {
        TextView name = binding.editCatNameEntry;
        TextView desc = binding.editCatDescEntry;

        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME, DBDefs.Category.C_DESCRIPTION, DBDefs.Category.C_CATEGORY_ID},
                DBDefs.Category.C_CATEGORY_ID + " like ?",
                new String[]{Integer.toString(categoryList.get(position).getID())},
                null,null,null,null);
        oldCat = new Categories();
        if (cursor.getCount() > 0) {
            do {
                oldCat.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME)));
                oldCat.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_DESCRIPTION)));
                oldCat.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.Category.C_CATEGORY_ID)));
            } while (cursor.moveToNext());

            currentCatID = oldCat.getID();
            Log.i("DEBUG", "CURRENT ITEM ID: " + currentCatID);
            name.setText(oldCat.getName());
            desc.setText(oldCat.getDescription());
        }

        dbManager.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
