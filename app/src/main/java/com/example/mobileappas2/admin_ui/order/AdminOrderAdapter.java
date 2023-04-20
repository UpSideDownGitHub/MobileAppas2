package com.example.mobileappas2.admin_ui.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;
import com.example.mobileappas2.ui.basket.BasketData;

import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<OrderData> orderData;
    private Context context;
    private FragmentActivity activity;

    /*
     * Constructor for initialization
     */
    public AdminOrderAdapter(FragmentActivity givenActivity, Context context, ArrayList givenItems) {
        this.context = context;
        this.orderData = givenItems;
        this.activity = givenActivity;
    }

    /*
     * this class will initialize the view holder for the recycler view
     */
    @NonNull
    @Override
    public AdminOrderAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create the new viewHolder and return it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oldorder_item_fragment, parent, false);
        AdminOrderAdapter.ViewHolder2 viewHolder = new AdminOrderAdapter.ViewHolder2(view);
        return viewHolder;
    }

    /*
     * Bind the date to the specified position on screen
     */
    @Override
    public void onBindViewHolder(@NonNull AdminOrderAdapter.ViewHolder2 holder, int position) {
        // get the date at the current position
        final OrderData quizDataHolder1 = orderData.get(position);
        // set the name, score, and date from the data at the specified position
        holder.orderMade.setText(quizDataHolder1.getOrderDate());
        holder.orderUpdated.setText(quizDataHolder1.getOrderUpdateDate());
        holder.orderProducts.setText(quizDataHolder1.getOrderProducts());
        holder.orderTotal.setText(quizDataHolder1.getOrderTotalPrice());
    }

    /*
     * returns to total item count
     */
    @Override
    public int getItemCount() {
        return orderData.size();
    }

    /*
     * Initialize the view, getting all of the text views that will be edited
     */
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        // private variables
        TextView orderMade;
        TextView orderUpdated;
        TextView orderProducts;
        TextView orderTotal;
        /*
         * constructor to initialize the view holder
         */
        public ViewHolder2(View view) {
            super(view);
            // get all of the text views
            orderMade = (TextView) view.findViewById(R.id.datemade_OldOrder_Text);
            orderUpdated = (TextView) view.findViewById(R.id.dateupdated_OldOrder_Text);
            orderProducts = (TextView) view.findViewById(R.id.products_OldOrder_Text);
            orderTotal = (TextView) view.findViewById(R.id.totalcost_OldOrder_Text);
        }
    }
}
