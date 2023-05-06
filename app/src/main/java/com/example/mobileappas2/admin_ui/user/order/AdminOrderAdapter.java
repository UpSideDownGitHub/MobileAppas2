package com.example.mobileappas2.admin_ui.user.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.R;

import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<OrderData> orderData;
    private Context context;
    private FragmentActivity activity;
    private ToggleButton[] toggles;
    public static int currentSelectedOrderID = -1;
    /*
     * Constructor for initialization
     */
    public AdminOrderAdapter(FragmentActivity givenActivity, Context context, ArrayList givenItems, ToggleButton[] toggles) {
        this.context = context;
        this.orderData = givenItems;
        this.activity = givenActivity;
        this.toggles = toggles;
        //currentSelectedOrderID = -1;
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

        // attach a listener that will be called when an item is pressed
        view.setOnClickListener(v -> {
            // get the index of the pressed item in the list
            int pos = viewHolder.getAbsoluteAdapterPosition();
            int index = orderData.indexOf(orderData.get(pos));
            // set the current selected ID to the ID of the item that was selected
            currentSelectedOrderID = orderData.get(index).getOrderID();

            // turn off all of the toggles as a new item has been selected
            toggles[0].setChecked(false);
            toggles[1].setChecked(false);
            toggles[2].setChecked(false);
            toggles[3].setChecked(false);
        });
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
        holder.orderStatus.setText(quizDataHolder1.getOrderStatus());
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
            orderStatus = (TextView) view.findViewById(R.id.status_OldOrder_Text);
            orderProducts = (TextView) view.findViewById(R.id.products_OldOrder_Text);
            orderTotal = (TextView) view.findViewById(R.id.totalcost_OldOrder_Text);
        }
    }
}
