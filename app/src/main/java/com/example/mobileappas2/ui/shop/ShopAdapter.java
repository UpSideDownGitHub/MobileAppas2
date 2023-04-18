package com.example.mobileappas2.ui.shop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.R;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<com.example.mobileappas2.ui.shop.ShopAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<ShopDataHolder> shopDataHolder;
    private Context context;
    private FragmentActivity activity;
    // Public variables
    public int globalPosition;

    /*
     * Constructor for initialization
     */
    public ShopAdapter(FragmentActivity givenActivity, Context context, ArrayList givenItems) {
        this.context = context;
        this.shopDataHolder = givenItems;
        this.activity = givenActivity;
    }

    /*
     * this class will initialize the view holder for the recycler view
     */
    @NonNull
    @Override
    public com.example.mobileappas2.ui.shop.ShopAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create the new viewHolder and return it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item_fragment, parent, false);
        com.example.mobileappas2.ui.shop.ShopAdapter.ViewHolder2 viewHolder = new com.example.mobileappas2.ui.shop.ShopAdapter.ViewHolder2(view);

        view.setOnClickListener(v -> {
            // get the position of the adapter
            globalPosition = viewHolder.getAbsoluteAdapterPosition();
            int pos = globalPosition;
            // get the index of the item pressed in the note list
            int index = shopDataHolder.indexOf(shopDataHolder.get(pos));
            // set the current index in shared preferences

            Log.i("DEBUG", "Current Item Index: " + index);
        });

        return viewHolder;
    }

    /*
     * Bind the date to the specified position on screen
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.mobileappas2.ui.shop.ShopAdapter.ViewHolder2 holder, int position) {
        // get the date at the current position
        final ShopDataHolder quizDataHolder1 = shopDataHolder.get(position);
        // set the name, score, and date from the data at the specified position
        holder.title.setText(quizDataHolder1.getTitle());
        holder.description.setText(quizDataHolder1.getDescription());
        holder.price.setText(quizDataHolder1.getPrice().toString());
    }

    /*
     * returns to total item count
     */
    @Override
    public int getItemCount() {
        return shopDataHolder.size();
    }

    /*
     * Initialize the view, getting all of the text views that will be edited
     */
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        // private variables
        TextView title;
        TextView description;
        TextView price;
        /*
         * constructor to initialize the view holder
         */
        public ViewHolder2(View view) {
            super(view);
            // get all of the text views
            title = (TextView) view.findViewById(R.id.title_ShopItem_Text);
            description = (TextView) view.findViewById(R.id.description_ShopItem_Text);
            price = (TextView) view.findViewById(R.id.price_ShopItem_Text);
        }
    }

    /*
     * clear the list of items in the current list
     */
    public void clearList() {shopDataHolder.clear();}

    /*
     * add an item to the list of items to show on the recycler view
     */
    public void addValue(String value1, String value2, Float value3) {
        shopDataHolder.add(new ShopDataHolder(value1, value2, value3));
    }

    /*
     *  update the data set to show the new added items
     */
    public void update(){ notifyDataSetChanged(); }
}
