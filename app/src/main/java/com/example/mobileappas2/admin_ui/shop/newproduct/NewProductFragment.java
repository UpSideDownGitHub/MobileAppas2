package com.example.mobileappas2.admin_ui.shop.newproduct;

import android.database.Cursor;
import android.os.Build;
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
import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;
import com.example.mobileappas2.databinding.AdminFragmentNewproductBinding;
import com.example.mobileappas2.ui.shop.ShopAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class NewProductFragment extends Fragment {
	// public varaibles
	public ArrayList<String> categoryTitles = new ArrayList();
    public int categorySelectedID;

    // private variables
    private AdminFragmentNewproductBinding binding;
    
	/*
        is called whenthe view is created
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		// inflate the view and get the root
        binding = AdminFragmentNewproductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // CHECK FOR THE BUTTON PRESSES
        Button cancelButton = binding.adminCancelNewProductUpdateButton;
        Button createButton = binding.adminCreateProductButton;
        cancelButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_admin_shop);
        });
        createButton.setOnClickListener(view -> createNewProduct());

        // SET UP THE SPINNER TO SHOW THE CATEGORIES AVAILABLE
        // open the database
        DBManager dbManager = new DBManager(root.getContext());
        dbManager.open();
        // load the data from the database
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME},
                null,
                null,
                null, null, null, null);
        dbManager.close();

        // convert data in database to a usable list of strings
        do {
            String cat = new String();
            cat = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            categoryTitles.add(cat);
        }while (cursor.moveToNext());

        // setup the spinner
        Spinner spinner = (Spinner) binding.adminNewItemUpdateSpinner;
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getActivity(),
                R.layout.spinner_item,
                categoryTitles);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
        // add listener to add to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // change the title and the description of the section based on the item selected
                categorySelectedID = spinner.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        return root;
    }

	/*
		this is called to create a new product and will check if there is not already a product with the 
		given infomation and if there is not will add the new product to the save data
	*/
    public void createNewProduct()
    {
		// load the information the user has entered
        String name = binding.adminProductnameupdateEntry.getText().toString();
        String description = binding.adminProductDescriptionUpdateEntry.getText().toString();
        String price = binding.adminProductPriceUpdateEntry.getText().toString();
		// if the user has not entered a valid price
        try {
            Float temp = Float.parseFloat(price);
        }
        catch (Exception e)
        {
			// stop and warn the user of there mistake
            Toast.makeText(
                    getContext(),
                    "Please Enter A Valid Price",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		
		// if the user has not entered any inforamtion then stop and warn them
        if (name.isEmpty() || description.isEmpty())
        {
            Toast.makeText(
                    getContext(),
                    "Please Fill In All Entries",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		
		// get the current date
        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }

		// open the database
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        // need to check if there are another items the same type
        Cursor cursor = dbManager.fetch(DBDefs.Product.TABLE_NAME,
                new String[]{DBDefs.Product.C_PRODUCT_NAME},
                DBDefs.Product.C_PRODUCT_NAME + " Like ?",
                new String[]{name},
                null, null, null, null);
		// if any items where found in the above fetch statement 
        if (cursor.getCount() > 0)
        {
			// warn the user taht there is allready an item with that name
            Toast.makeText(
                    getContext(),
                    "There is Already A Product With That Name",
                    Toast.LENGTH_SHORT).show();
            dbManager.close();
            return;
        }

		// insert the new product into the database
        dbManager.insert(name, description, date, date, Float.parseFloat(price),
                Float.parseFloat(price), Float.parseFloat(price), categorySelectedID);
        dbManager.close();
		// load the admin main shop screen
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_shop);
    }

	// used to properly destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
