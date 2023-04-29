package com.example.mobileappas2.ui.basket;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;
import com.example.mobileappas2.databinding.FragmentBasketBinding;
import com.example.mobileappas2.ui.shop.ShopAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class BasketFragment extends Fragment {
	// private variables
    private FragmentBasketBinding binding;
    private BasketAdapter adapter;
	
	/*
        is called whenthe view is created
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
		// inflate the view and get the root
        binding = FragmentBasketBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // setup the Recycler View
        TextView priceText = binding.basketTotalText;
        adapter = new BasketAdapter(getActivity(), getContext(), priceText);
        RecyclerView recyclerView = (RecyclerView) binding.basketRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
		
		// get Buttons from the layout and add the corosponding listeners to them
        Button buyButton = binding.buyContentsButton;
        buyButton.setOnClickListener(view -> buyContent());

        Button removeAll = binding.removeContentsButton;
        removeAll.setOnClickListener(view -> {
            BasketData.removeAllProducts();
            adapter.updateTotalCost();
            adapter.update();
        });

        return root;
    }
	
	/*
		when called will return the total cost of all current items in the basket
	*/
    public float totalCost()
    {	
		// loop through all of the items in the basket and calcuate the total cost 
        ArrayList<Products> products = BasketData.getBasketData();
        float subTotal = 0;
        for (int i = 0; i < products.size(); i++)
        {
            subTotal += products.get(i).getPrice();
        }
		// return the total cost
        return subTotal;
    }
	
	/*
		when this is pressed it will handle buying the contents of the basket
	*/
    public void buyContent()
    {
		// if there are no products in the basket then return
        ArrayList<Products> products = BasketData.getBasketData();
        if (products.size() <= 0)
        {
            return;
        }
        // OPEN THE DATABASE
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();

        
		// get the date
        String date = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate d = LocalDate.now(ZoneId.systemDefault());
            date = d.format(dateFormatter);
        }
		// INSERT ITEM INTO ORDER
        dbManager.insert(date, date, 0);

        // FIND THE CURRENT ORDER ID (OF THE ORDER CREATED ABOVE)
        Cursor cursor = dbManager.fetch(DBDefs.Order.TABLE_NAME,
                new String[]{DBDefs.Order.C_ORDER_ID},
                null,null,null,null,null,null);
        int lastOrderID = cursor.getCount();

        // INSERT ALL OF THE PRODUCTS IN THIS ORDER INTO THE PRODUCT_ORDER TABLE
        for (int i = 0; i < products.size(); i++)
        {
            dbManager.insert(products.get(i).getID(), lastOrderID);
        }

        // INSERT THE CURRENT ORDER AND USER ID INTO THE USER_ORDER TABLE
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int userID = sharedPref.getInt("userID", 0);
        dbManager.insert(userID, lastOrderID, 1);

        dbManager.close();
		
		// remove all products from the basket as they have just been brought 
		// then update the recycler view to show this
        BasketData.removeAllProducts();
        adapter.updateTotalCost();
        adapter.update();
    }
	
	// used to properly destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}