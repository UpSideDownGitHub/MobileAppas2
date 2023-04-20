package com.example.mobileappas2.admin_ui.shop;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;
import com.example.mobileappas2.ui.basket.BasketData;

import java.util.ArrayList;

public class AdminShopAdapter extends RecyclerView.Adapter<com.example.mobileappas2.admin_ui.shop.AdminShopAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<AdminShopData> shopData;
    private Context context;
    private FragmentActivity activity;
    // Public variables
    public int globalPosition;

    /*
     * Constructor for initialization
     */
    public AdminShopAdapter(FragmentActivity givenActivity, Context context, ArrayList givenItems) {
        this.context = context;
        this.shopData = givenItems;
        this.activity = givenActivity;
    }

    /*
     * this class will initialize the view holder for the recycler view
     */
    @NonNull
    @Override
    public com.example.mobileappas2.admin_ui.shop.AdminShopAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create the new viewHolder and return it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_shop_item_fragment, parent, false);
        AdminShopAdapter.ViewHolder2 viewHolder = new com.example.mobileappas2.admin_ui.shop.AdminShopAdapter.ViewHolder2(view);

        view.setOnClickListener(v -> {
            // get the position of the adapter
            globalPosition = viewHolder.getAbsoluteAdapterPosition();
            int pos = globalPosition;
            // get the index of the item pressed in the note list
            int index = shopData.indexOf(shopData.get(pos));
            // set the current index in shared preferences
            //Log.i("DEBUG", "Current Item Index: " + index);

            // NEED TO OPEN THE EDIT WINDOW WITH THIS PRODUCT
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("currentProductID", shopData.get(index).getID());
            editor.apply();
            Navigation.findNavController(view).navigate(R.id.navigation_admin_edit_product);
        });

        return viewHolder;
    }

    /*
     * Bind the date to the specified position on screen
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.mobileappas2.admin_ui.shop.AdminShopAdapter.ViewHolder2 holder, int position) {
        // get the date at the current position
        final AdminShopData quizDataHolder1 = shopData.get(position);
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
        return shopData.size();
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
            title = (TextView) view.findViewById(R.id.admin_title_BasketItem_Text);
            description = (TextView) view.findViewById(R.id.admin_description_ShopItem_Text);
            price = (TextView) view.findViewById(R.id.admin_price_BasketItem_Text);
        }
    }

    /*
     * clear the list of items in the current list
     */
    public void clearList() {shopData.clear();}

    /*
     * add an item to the list of items to show on the recycler view
     */
    public void addValue(String value1, String value2, Float value3, Integer value4) {
        shopData.add(new AdminShopData(value1, value2, value3, value4));
    }

    /*
     *  update the data set to show the new added items
     */
    public void update(){ notifyDataSetChanged(); }
}
