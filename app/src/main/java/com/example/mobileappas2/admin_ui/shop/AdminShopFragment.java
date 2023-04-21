package com.example.mobileappas2.admin_ui.shop;

import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Orders;
import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.Database.DataHolders.UserOrders;
import com.example.mobileappas2.R;
import com.example.mobileappas2.databinding.AdminFragmentShopBinding;
import com.example.mobileappas2.ui.basket.BasketData;
import com.example.mobileappas2.ui.shop.ShopAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminShopFragment extends Fragment {

    private AdminFragmentShopBinding binding;
    private AdminShopAdapter adapter;

    public ArrayList<String> categoryTitles = new ArrayList();
    public ArrayList<String> categoryDescriptions= new ArrayList();

    public TextView catTitle, catDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AdminFragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        catTitle = binding.adminCategoryNameText;
        catDescription = binding.adminCategoryDescriptionText;

        // ADD CLICK LISTENER TO THE FLOATING ACTION BUTTON OPEN THE
        // NEW PRODUCT WINDOW
        FloatingActionButton floatingActionButton = binding.adminNewProductActionButton;
        floatingActionButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_admin_new_product);
        });

        FloatingActionButton newCategoryButton = binding.adminNewCategoryActionButton;
        newCategoryButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_admin_edit_category);
        });

        // Spinner
        // open the database
        DBManager dbManager = new DBManager(root.getContext());
        dbManager.open();
        // load the data from the database
        Cursor cursor = dbManager.fetch(DBDefs.Category.TABLE_NAME,
                new String[]{DBDefs.Category.C_NAME, DBDefs.Category.C_DESCRIPTION},
                null,
                null,
                null, null, null, null);
        dbManager.close();

        // convert data in database to a usable list of strings
        categoryTitles.clear();
        categoryDescriptions.clear();
        do {
            String cat = new String();
            String cat2 = new String();
            cat = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            cat2 = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_DESCRIPTION));
            categoryTitles.add(cat);
            categoryDescriptions.add(cat2);
        }while (cursor.moveToNext());

        // setup the spinner
        Spinner spinner = (Spinner) binding.adminShopSpinner;
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
                int ID = spinner.getSelectedItemPosition();
                catTitle.setText(categoryTitles.get(ID));
                catDescription.setText(categoryDescriptions.get(ID));
                updateAdapterView(ID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Recycler View
        adapter = new AdminShopAdapter(getActivity(), getContext(), new ArrayList<>());
        RecyclerView recyclerView = (RecyclerView) binding.adminProductsRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        updateAdapterView(0);

        return root;
    }

    public void updateAdapterView(int ID)
    {
        // clear the list of items in the current adapter
        adapter.clearList();

        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        // load the data from the database
        Cursor cursor;
        if (ID == 0)
        {
            // select all of the products
            cursor = dbManager.fetch(DBDefs.Product.TABLE_NAME,
                    new String[]{DBDefs.Product.C_PRODUCT_NAME, DBDefs.Product.C_PRODUCT_DESCRIPTION,
                            DBDefs.Product.C_PRICE, DBDefs.Product.C_CATEGORY_ID, DBDefs.Product.C_PRODUCT_ID},
                    null, null, null, null, null, null);
        }
        else
        {
            // select a specific product
            cursor = dbManager.fetch(DBDefs.Product.TABLE_NAME,
                    new String[]{DBDefs.Product.C_PRODUCT_NAME, DBDefs.Product.C_PRODUCT_DESCRIPTION,
                            DBDefs.Product.C_PRICE, DBDefs.Product.C_CATEGORY_ID, DBDefs.Product.C_PRODUCT_ID},
                    DBDefs.Product.C_CATEGORY_ID + " like ?",
                    new String[]{Integer.toString(ID)},
                    null, null, null, null);
        }

        dbManager.close();

        // convert data in database to a usable list of strings
        ArrayList<Products> products = new ArrayList();
        if (cursor.getCount() > 0) {
            do {
                Products prod = new Products();
                prod.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_NAME)));
                prod.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_DESCRIPTION)));
                prod.setPrice(cursor.getFloat(cursor.getColumnIndexOrThrow(DBDefs.Product.C_PRICE)));
                prod.setCategoryID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.Product.C_CATEGORY_ID)));
                prod.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_ID)));
                products.add(prod);
            } while (cursor.moveToNext());
        }

        for (int i = 0; i < products.size(); i++) {
            adapter.addValue(products.get(i).getName(), products.get(i).getDescription(),
                    products.get(i).getPrice(), products.get(i).getID());
        }

        // update the adapter to show all the new data
        adapter.update();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}