package com.example.mobileappas2.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.example.mobileappas2.databinding.FragmentShopBinding;
import com.example.mobileappas2.databinding.FragmentUserBinding;
import com.example.mobileappas2.ui.basket.BasketData;
import com.example.mobileappas2.ui.shop.ShopAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private OldOrdersAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button floatingButton = binding.editUserActionButton;
        floatingButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_edituser);
        });

        // INITIALISE THE DATABASE & DATA HOLDER
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        ArrayList<OldOrderData> oldOrderData = new ArrayList();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int userID = sharedPref.getInt("userID", 0);

        // GET THE PLAYER NAME AND THE PLAYER EMAIL TO SHOW ON THE SCREEN
        Cursor cur = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME, DBDefs.User.C_EMAIL_ADDRESS},
                DBDefs.User.C_USER_ID + " like ?",
                new String[]{Integer.toString(userID)},
                null, null, null, null);
        String name;
        String email;
        do{
            name = cur.getString(cur.getColumnIndexOrThrow(DBDefs.User.C_FULL_NAME));
            email = cur.getString(cur.getColumnIndexOrThrow(DBDefs.User.C_EMAIL_ADDRESS));
        }while (cur.moveToNext());
        binding.userNameText.setText(name);
        binding.userEmailText.setText(email);

        // GET ALL OF THE ORDER ID'S FOR THE CURRENT PLAYER
        Cursor cursor = dbManager.fetch(DBDefs.User_Order.TABLE_NAME,
                new String[]{DBDefs.User_Order.C_ORDER_ID, DBDefs.User_Order.C_USER_ID},
                DBDefs.User_Order.C_USER_ID + " like ?",
                new String[]{Integer.toString(userID)},
                null, null ,null,null);

        if (cursor.getCount() > 0) {
            ArrayList<UserOrders> userOrders = new ArrayList();
            do {
                UserOrders userOrder = new UserOrders();
                userOrder.setOrderID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.User_Order.C_ORDER_ID)));
                userOrder.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.User_Order.C_USER_ID)));
                userOrders.add(userOrder);
            } while (cursor.moveToNext());
            for (int i = 0; i < userOrders.size(); i++) {
                OldOrderData oldData = new OldOrderData();
                oldOrderData.add(oldData);
            }

            // GET ALL OF THE PRODUCTS FOR EACH OF THE ORDERS I JUST SELECTED & GET THE PRICE
            for (int i = 0; i < userOrders.size(); i++) {
                ArrayList<Products> productList = new ArrayList();
                // product id list
                Cursor cursor2 = dbManager.fetch(DBDefs.Product_Order.TABLE_NAME,
                        new String[]{DBDefs.Product_Order.C_PRODUCT_ID, DBDefs.Product_Order.C_ORDER_ID},
                        DBDefs.Product_Order.C_ORDER_ID + " like ?",
                        new String[]{userOrders.get(i).getOrderID().toString()},
                        null, null, null, null);
                if (cursor2.getCount() > 0) {
                    do {
                        // product info list
                        Cursor cursor3 = dbManager.fetch(DBDefs.Product.TABLE_NAME,
                                new String[]{DBDefs.Product.C_PRODUCT_ID, DBDefs.Product.C_PRODUCT_NAME,
                                        DBDefs.Product.C_PRICE},
                                DBDefs.Product.C_PRODUCT_ID + " like ?",
                                new String[]{cursor2.getString(cursor2.getColumnIndexOrThrow(DBDefs.Product_Order.C_PRODUCT_ID))},
                                null, null, null, null);
                        do {
                            Products products = new Products();
                            products.setName(cursor3.getString(cursor3.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_NAME)));
                            products.setPrice(cursor3.getFloat(cursor3.getColumnIndexOrThrow(DBDefs.Product.C_PRICE)));
                            products.setID(cursor3.getInt(cursor3.getColumnIndexOrThrow(DBDefs.Product.C_PRODUCT_ID)));
                            productList.add(products);
                        } while (cursor3.moveToNext());
                    } while (cursor2.moveToNext());
                }
                // take the generated product list and convert it to a string of the correct type
                // and also turn this list into the total cost
                String orderProducts = convertProductListToString(productList);
                String orderTotal = convertProductListToTotalString(productList);
                oldOrderData.get(i).setOrderProducts(orderProducts);
                oldOrderData.get(i).setOrderTotalPrice(orderTotal);
            }

            // GET THE DATE CREATED AND DATE UPDATED FROM THE ORDER TABLE
            for (int i = 0; i < userOrders.size(); i++) {
                ArrayList<Orders> orderList = new ArrayList();
                // product id list
                Cursor cursor1 = dbManager.fetch(DBDefs.Order.TABLE_NAME,
                        new String[]{DBDefs.Order.C_DATE_CREATED, DBDefs.Order.C_DATE_UPDATED, DBDefs.Order.C_STATUS},
                        DBDefs.Product_Order.C_ORDER_ID + " like ?",
                        new String[]{userOrders.get(i).getOrderID().toString()},
                        null, null, null, null);
                do {
                    Orders order = new Orders();
                    order.setDateCreated(cursor1.getString(cursor1.getColumnIndexOrThrow(DBDefs.Order.C_DATE_CREATED)));
                    order.setDateUpdated(cursor1.getString(cursor1.getColumnIndexOrThrow(DBDefs.Order.C_DATE_UPDATED)));
                    order.setStatus(cursor1.getInt(cursor1.getColumnIndexOrThrow(DBDefs.Order.C_STATUS)));
                    order.setID(userOrders.get(i).getOrderID());
                    orderList.add(order);
                } while (cursor1.moveToNext());
                // convert the data to show the current date and the order date in string form
                String orderDate = "Order Made: " + orderList.get(0).getDateCreated();
                String orderUpdated = "Order Updated: " + orderList.get(0).getDateUpdated();
                String orderStatus = "Status: " + orderList.get(0).getStatus();
                oldOrderData.get(i).setOrderDate(orderDate);
                oldOrderData.get(i).setOrderUpdateDate(orderUpdated);
                oldOrderData.get(i).setOrderStatus(orderStatus);
            }
        }

        adapter = new OldOrdersAdapter(getActivity(), getContext(), oldOrderData);
        RecyclerView recyclerView = (RecyclerView) binding.oldOrdersRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        dbManager.close();
        return root;
    }

    public String convertProductListToTotalString(ArrayList<Products> list)
    {
        float subTotal = 0;
        for (int i = 0; i < list.size(); i++)
        {
            subTotal += list.get(i).getPrice();
        }
        return "Total: £ " + Float.toString(subTotal);
    }

    public String convertProductListToString(ArrayList<Products> list)
    {
        String tempString = "";
        for (int i = 0; i < list.size(); i++)
        {
            tempString += list.get(i).getPrice().toString() + " - " +
                    list.get(i).getName() + "\n";
        }
        return tempString;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}