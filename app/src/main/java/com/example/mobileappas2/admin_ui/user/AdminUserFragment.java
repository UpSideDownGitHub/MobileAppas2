package com.example.mobileappas2.admin_ui.user;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DBDefs;
import com.example.mobileappas2.Database.DBManager;
import com.example.mobileappas2.Database.DataHolders.UserOrders;
import com.example.mobileappas2.Database.DataHolders.Users;
import com.example.mobileappas2.admin_ui.order.OrderData;
import com.example.mobileappas2.admin_ui.shop.AdminShopAdapter;
import com.example.mobileappas2.databinding.AdminFragmentUsersBinding;

import java.util.ArrayList;

public class AdminUserFragment extends Fragment {

    private AdminFragmentUsersBinding binding;
    private AdminUsersAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AdminFragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        Cursor cursor = dbManager.fetch(DBDefs.User.TABLE_NAME,
                new String[]{DBDefs.User.C_FULL_NAME, DBDefs.User.C_EMAIL_ADDRESS, DBDefs.User.C_USER_ID},
                null, null, null,
                null, null, null);


        ArrayList<AdminUserData> users = new ArrayList();
        do {
            AdminUserData user = new AdminUserData();
            user.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_FULL_NAME)));
            user.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBDefs.User.C_EMAIL_ADDRESS)));
            user.setID(cursor.getInt(cursor.getColumnIndexOrThrow(DBDefs.User.C_USER_ID)));
            users.add(user);
        } while (cursor.moveToNext());


        adapter = new AdminUsersAdapter(getActivity(), getContext(), users);
        RecyclerView recyclerView = (RecyclerView) binding.adminUsersRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}