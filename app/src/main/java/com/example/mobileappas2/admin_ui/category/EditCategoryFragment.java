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
    // public varaibles
    public int currentCatID = 0;
    public Categories oldCat;

    // private variables
    private AdminFragmentEditcategoryBinding binding;

    ArrayList<String> catNames = new ArrayList();
    ArrayList<Categories> categoryList = new ArrayList();

    /*
        is called whenthe view is created
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // inflate the view and get the root
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

        // return the root
        return root;
    }

    /*
        when this is called it will create a new category, if possible, and then
        add it to the data base
     */
    public void newCategory()
    {
        // GET THE NAME AND DESCRIPTION FROM THE ENTRY POINTS
        String name = binding.editCatNameEntry.getText().toString();
        String desc = binding.editCatDescEntry.getText().toString();

        // if the user has not entered anything
        if (name.isEmpty() || desc.isEmpty())
        {
            // tell user they need to enter something
            Toast.makeText(
                    getContext(),
                    "Make Sure all fields are filled in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // CREATE A NEW CATEGORY
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        // fetch all instances in the category table where the name is equal to the entered name
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_CATEGORY_ID},
                DBDefs.Category.C_NAME + " like ?",
                new String[]{name},
                null,null,null,null);
        // if there where any items found by the above search
        if (cursor.getCount() > 0)
        {
            // tell the user they cannot create a category with the same name as another category
            Toast.makeText(
                    getContext(),
                    "There is already a category with that name",
                    Toast.LENGTH_SHORT).show();
            dbManager.close();
            return;
        }
        // insert the data the user entered into the database
        dbManager.insert(name, desc);
        dbManager.close();

        // update the spinner to show the new info (as there is now a new category)
        updateSpinner();
    }

    /*
        when this is called it will update the existing element in the database with
        new information entered by the user
     */
    public void updateCategory()
    {
        // if current selected category is one that is not allowed to be updated then stop
        // the All & Misc categories are the ones that are not allowed to be edited
        if (currentCatID == 0 || currentCatID == 1 || currentCatID == 2)
        {
            Toast.makeText(
                    getContext(),
                    "Cant Edit This Category",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // GET THE NAME AND DESCRIPTION FROM THE ENTRY POINTS
        String name = binding.editCatNameEntry.getText().toString();
        String desc = binding.editCatDescEntry.getText().toString();

        // if the inforamtion the user entered was blank then tell the user that they have to
        // enter some data then return
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
            // if something was found by the above fetch then return as there is allready
            // a category with that name
            if (cursor.getCount() > 0) {
                Toast.makeText(
                        getContext(),
                        "There is already a category with that name",
                        Toast.LENGTH_SHORT).show();
                dbManager.close();
                return;
            }
        }
        // update the existing category in the dataase with the new information
        dbManager.update(name, desc, currentCatID, null);
        dbManager.close();

        // update the spinner to show this new information
        updateSpinner();
    }

    /*
        this will be called when the user wants to delete the currently selcted cateogry, it will only delete,
        the category if it is allwoed
     */
    public void deleteCategory()
    {
        // if cant delete this category as is one of the inbuilt ones then tell the user they cant
        // and return
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
        // update any products that use this category to belong under the MISC category
        // as there original category is about to be deleted
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

    /*
        this will update the spinner that shows all of the categories to the user
     */
    public void updateSpinner()
    {
        // open the data base and clear the previous data from the spinner
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

    /*
        when called (when an item in the spinner is selcted) this will update the information being shown
        to match that of the spinner that was just selected
     */
    public void setNameandDescription(int position)
    {
        // get the TextViews for the infroatmion to show
        TextView name = binding.editCatNameEntry;
        TextView desc = binding.editCatDescEntry;

        // open the database
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        // load all of the data about the selected categry
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME, DBDefs.Category.C_DESCRIPTION, DBDefs.Category.C_CATEGORY_ID},
                DBDefs.Category.C_CATEGORY_ID + " like ?",
                new String[]{Integer.toString(categoryList.get(position).getID())},
                null,null,null,null);
        oldCat = new Categories();
        // if there is a category returned
        if (cursor.getCount() > 0) {
            // get all of theinforamtion and save it
            do {
                oldCat.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME)));
                oldCat.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_DESCRIPTION)));
                oldCat.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.Category.C_CATEGORY_ID)));
            } while (cursor.moveToNext());

            // update the information on screen to show the inforamtion about the category just selected
            // by the user
            currentCatID = oldCat.getID();
            //Log.i("DEBUG", "CURRENT ITEM ID: " + currentCatID);
            name.setText(oldCat.getName());
            desc.setText(oldCat.getDescription());
        }

        dbManager.close();
    }

    // used to properly destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
