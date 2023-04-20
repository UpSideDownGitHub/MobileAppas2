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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;
import com.example.mobileappas2.databinding.AdminFragmentUpdateproductBinding;
import com.example.mobileappas2.ui.shop.ShopAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class EditProductFragment extends Fragment {
    private AdminFragmentUpdateproductBinding binding;
    public int productID;
    public int categorySelectedID;
    public ArrayList<String> categoryTitles = new ArrayList();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
        Spinner spinner = (Spinner) binding.adminUpdateSpinner;
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                categoryTitles);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    public void updateProduct()
    {
        // GET ALL OF THE DATA TO BE UPDATED IN THE DATABASE
        String name = binding.adminProductnameupdateEntry.getText().toString();
        String description = binding.adminProductDescriptionUpdateEntry.getText().toString();
        Float price = Float.parseFloat(binding.adminProductPriceUpdateEntry.getText().toString());

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

        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_shop);
    }

    public void deleteProduct(){
        // DELETE THE PRODUCT FROM THE DATABASE
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        dbManager.delete(DBDefs.Product.TABLE_NAME,
                "product_id",
                new String[]{Integer.toString(productID)});
        dbManager.close();
        Navigation.findNavController(getView()).navigate(R.id.navigation_admin_shop);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
