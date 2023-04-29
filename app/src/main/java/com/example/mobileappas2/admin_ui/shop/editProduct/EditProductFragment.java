package com.example.mobileappas2.admin_ui.shop.editProduct;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.mobileappas2.Database.DataHolders.Users;
import com.example.mobileappas2.R;
import com.example.mobileappas2.databinding.AdminFragmentUpdateproductBinding;
import com.example.mobileappas2.ui.shop.ShopAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class EditProductFragment extends Fragment {
	// PUBLIC VARIABLES
	public int productID;
    public int categorySelectedID;
    public ArrayList<String> categoryTitles = new ArrayList();
	
	// PRIVATE VARAIBLES
    private AdminFragmentUpdateproductBinding binding;
    
	/*
        is called whenthe view is created
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		// inflate the view and get the root
        binding = AdminFragmentUpdateproductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // GET THE PRODUCT ID FROM SHARED PREFERENCES
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        productID = sharedPref.getInt("currentProductID", 0);

        // ATTACH LISTENERS TO THE BUTTON
        Button cancelButton = binding.adminCancelNewProductUpdateButton;
        Button deleteButton = binding.adminDeleteProductButton;
        Button updateButton = binding.adminUpdateProductButton;
        cancelButton.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.navigation_admin_shop);
        });
        deleteButton.setOnClickListener(view -> deleteProduct());
        updateButton.setOnClickListener(view -> updateProduct());

        // open the database
        DBManager dbManager = new DBManager(root.getContext());
        dbManager.open();
        // load the previous information to show as the initial values
        // GET ALL OF THE TEXT THE USER HAS ENTERED
        TextView name = binding.adminProductnameupdateEntry;
        TextView description = binding.adminProductDescriptionUpdateEntry;
        TextView price = binding.adminProductPriceUpdateEntry;

        // GET THE CURRENT DATA HELD IN THE DATABASE FOR THIS USER
        Products product = new Products();
        Cursor cur = dbManager.fetch(DBDefs.Product.TABLE_NAME,
                new String[]{DBDefs.Product.C_PRODUCT_NAME, DBDefs.Product.C_PRODUCT_DESCRIPTION,
                        DBDefs.Product.C_PRICE, DBDefs.Product.C_CATEGORY_ID},
                DBDefs.Product.C_PRODUCT_ID + " like ?",
                new String[]{Integer.toString(productID)},
                null, null, null, null);
        do {
			// SAVE THE FOUND DATA
            product.setName(cur.getString(cur.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_NAME)));
            product.setDescription(cur.getString(cur.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_DESCRIPTION)));
            product.setPrice(cur.getFloat(cur.getColumnIndexOrThrow(DBDefs.Product.C_PRICE)));
            product.setCategoryID(cur.getInt(cur.getColumnIndexOrThrow(DBDefs.Product.C_CATEGORY_ID)));
            product.setID(productID);
        }while(cur.moveToNext());
		
		// SET THE CURRENT PRODUCTS INFORMATION TO BE VISABLE ON SCREEN
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice().toString());

        // load the data from the database
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME},
                null,
                null,
                null, null, null, null);
        dbManager.close();

        // SET UP THE SPINNER TO SHOW THE CATEGORIES AVAILABLE
        // convert data in database to a usable list of strings
        do {
            String cat = new String();
            cat = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            categoryTitles.add(cat);
        }while (cursor.moveToNext());

        // setup the spinner
        Spinner spinner = (Spinner) binding.adminUpdateSpinner;
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getActivity(),
                R.layout.spinner_item,
                categoryTitles);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(product.getCategoryID());
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
		when this is called the current product that is selected will updated with the new
		information entered by the user
	*/
    public void updateProduct()
    {
        // GET ALL OF THE DATA TO BE UPDATED IN THE DATABASE
        String name = binding.adminProductnameupdateEntry.getText().toString();
        String description = binding.adminProductDescriptionUpdateEntry.getText().toString();
        Float price = new Float(0);
		// test if the user has entered a valid number if not give a warning to the user
        try {
            price = Float.parseFloat(binding.adminProductPriceUpdateEntry.getText().toString());
        }
        catch (Exception e)
        {
			// tell user they need to enter valid number
            Toast.makeText(
                    getContext(),
                    "Please Enter a Valid Number",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		
		// make sure the user has entered information if not then stop and tell user to enter some
		// new information
        if (name.isEmpty() || description.isEmpty())
        {
            Toast.makeText(
                    getContext(),
                    "Please Enter All Values",
                    Toast.LENGTH_SHORT).show();
            return;
        }
		
		// set the current date
        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }

        // UPDATE THE PRODUCT IN THE DATA BASE
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        dbManager.update(name, description, date, date, price, price, price,
                productID, categorySelectedID, null);
        dbManager.close();
		
		// load the admin shop screen
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_shop);
    }
	
	/*
		when this is called it will delete the current selected product from the database
	*/
    public void deleteProduct(){
        // DELETE THE PRODUCT FROM THE DATABASE
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
		// delete all products where the product ID is equal to the current selected product ID
        dbManager.delete(DBDefs.Product.TABLE_NAME,
                "product_id",
                new String[]{Integer.toString(productID)});
        dbManager.close();
		
		// load the admin shop screen
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_shop);
    }

    // used to properly destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
