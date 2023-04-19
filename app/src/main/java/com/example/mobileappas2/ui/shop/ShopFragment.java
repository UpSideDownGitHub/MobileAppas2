package com.example.mobileappas2.ui.shop;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import android.widget.AdapterView;
import android.view.View;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Categories;
import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.Database.DataHolders.Users;
import com.example.mobileappas2.databinding.FragmentShopBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment{

    private FragmentShopBinding binding;

    private ShopAdapter adapter;

    public ArrayList<String> categoryTitles = new ArrayList();
    public ArrayList<String> categoryDescriptions= new ArrayList();

    public TextView catTitle, catDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // show the all category (as title and description)
        // add listener to the spinner to change the category when item is selected
        catTitle = binding.categoryTitleText;
        catDescription = binding.categoryDescriptionText;

        // update the content shown in the recycler view to show the new content
        // update the info shown on each recycler view elements

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
        do {
            String cat = new String();
            String cat2 = new String();
            cat = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            cat2 = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_DESCRIPTION));
            categoryTitles.add(cat);
            categoryDescriptions.add(cat2);
        }while (cursor.moveToNext());

        // setup the spinner
        Spinner spinner = (Spinner) binding.categorySpinner;
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
        adapter = new ShopAdapter(getActivity(), getContext(), new ArrayList<>());
        RecyclerView recyclerView = (RecyclerView) binding.shopItemsRecyclerView;
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