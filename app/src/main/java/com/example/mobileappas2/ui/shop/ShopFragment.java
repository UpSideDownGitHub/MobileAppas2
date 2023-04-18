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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Categories;
import com.example.mobileappas2.Database.DataHolders.Users;
import com.example.mobileappas2.databinding.FragmentShopBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment{

    private FragmentShopBinding binding;

    private ShopAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // need to load the list of categories that will be held in the database
        // show the all category (as title and description)
        // add listener to the spinner to change the category when item is selected
        // update the content shown in the recycler view to show the new content
        // update the info shown on each recycler view elements

        // Spinner
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
        ArrayList<String> category = new ArrayList();
        do {
            String cat = new String();
            cat = cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.Category.C_NAME));
            category.add(cat);
        }while (cursor.moveToNext());
        // setup the spinner
        Spinner spinner = (Spinner) binding.categorySpinner;
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        // add listener to add to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String msupplier = spinner.getSelectedItem().toString();
                Log.i("Selected item : ", msupplier);
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


        return root;
    }

    public void updateAdapterView()
    {
        // clear the list of items in the current adapter
        adapter.clearList();

        // TODO: TAKE THE DATA FROM THE DATABASE AND GET ALL OF THE DATA FROM THE SELECTED
        // TODO: CATEGORY AND ADD IT TO THE RECYCLER VIEW
        /*
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i) == quizID)
                adapter.addValue(names.get(i), scores.get(i).toString(), dates.get(i));
        }
        */

        // update the adapter to show all the new data
        adapter.update();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}