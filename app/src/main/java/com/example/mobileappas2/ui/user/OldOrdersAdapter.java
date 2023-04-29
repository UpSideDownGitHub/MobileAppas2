package com.example.mobileappas2.ui.user;

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

public class OldOrdersAdapter extends RecyclerView.Adapter<com.example.mobileappas2.ui.user.OldOrdersAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<OldOrderData> oldOrderData;
    private Context context;
    private FragmentActivity activity;

    /*
     * Constructor for initialization
     */
    public OldOrdersAdapter(FragmentActivity givenActivity, Context context, ArrayList givenItems) {
        this.context = context;
        this.oldOrderData = givenItems;
        this.activity = givenActivity;
    }

    /*
     * this class will initialize the view holder for the recycler view
     */
    @NonNull
    @Override
    public com.example.mobileappas2.ui.user.OldOrdersAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create the new viewHolder and return it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oldorder_item_fragment, parent, false);
        com.example.mobileappas2.ui.user.OldOrdersAdapter.ViewHolder2 viewHolder = new com.example.mobileappas2.ui.user.OldOrdersAdapter.ViewHolder2(view);
        return viewHolder;
    }

    /*
     * Bind the date to the specified position on screen
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.mobileappas2.ui.user.OldOrdersAdapter.ViewHolder2 holder, int position) {
        // get the date at the current position
        final OldOrderData quizDataHolder1 = oldOrderData.get(position);
        // set the name, score, and date from the data at the specified position
        holder.orderMade.setText(quizDataHolder1.getOrderDate());
        holder.orderUpdated.setText(quizDataHolder1.getOrderUpdateDate());
        holder.orderProducts.setText(quizDataHolder1.getOrderProducts());
        holder.orderStatus.setText(quizDataHolder1.getOrderStatus());
        holder.orderTotal.setText(quizDataHolder1.getOrderTotalPrice());
    }

    /*
     * returns to total item count
     */
    @Override
    public int getItemCount() {
        return oldOrderData.size();
    }

    /*
     * Initialize the view, getting all of the text views that will be edited
     */
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        // private variables
        TextView orderMade;
        TextView orderUpdated;
        TextView orderProducts;
        TextView orderStatus;
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
            orderStatus = (TextView) view.findViewById(R.id.status_OldOrder_Text);
            orderTotal = (TextView) view.findViewById(R.id.totalcost_OldOrder_Text);
        }
    }
}
