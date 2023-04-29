package com.example.mobileappas2.ui.basket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappas2.Database.DataHolders.Products;
import com.example.mobileappas2.R;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<com.example.mobileappas2.ui.basket.BasketAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<Products> basketDataHolder;
    private Context context;
    private FragmentActivity activity;
    private TextView priceText;
    // Public variables
    public int globalPosition;

    /*
     * Constructor for initialization
     */
    public BasketAdapter(FragmentActivity givenActivity, Context context, TextView price) {
        this.context = context;
        this.basketDataHolder = BasketData.getBasketData();
        this.activity = givenActivity;
        this.priceText = price;
        updateTotalCost();
    }

    /*
     * this class will initialize the view holder for the recycler view
     */
    @NonNull
    @Override
    public com.example.mobileappas2.ui.basket.BasketAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create the new viewHolder and return it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item_fragment, parent, false);
        com.example.mobileappas2.ui.basket.BasketAdapter.ViewHolder2 viewHolder = new com.example.mobileappas2.ui.basket.BasketAdapter.ViewHolder2(view);
        view.setOnClickListener(v -> {
            // get the position of the adapter
            globalPosition = viewHolder.getAbsoluteAdapterPosition();
            int pos = globalPosition;
            // get the index of the item pressed in the note list
            int index = basketDataHolder.indexOf(basketDataHolder.get(pos));
            //Log.i("DEBUG", "Current Item Index: " + index);
            BasketData.removeProduct(index);
            copyBasketData();
            updateTotalCost();
        });
        return viewHolder;
    }

    /*
     * Bind the date to the specified position on screen
     */
    @Override
    public void onBindViewHolder(@NonNull com.example.mobileappas2.ui.basket.BasketAdapter.ViewHolder2 holder, int position) {
        // get the date at the current position
        Products basketDataHolder1 = basketDataHolder.get(position);
        // set the name, score, and date from the data at the specified position
        holder.title.setText(basketDataHolder1.getName());
        holder.price.setText("£ " + basketDataHolder1.getPrice().toString());
    }

    /*
     * returns to total item count
     */
    @Override
    public int getItemCount() { return basketDataHolder.size(); }

    /*
     * Initialize the view, getting all of the text views that will be edited
     */
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        // private variables
        TextView title;
        TextView price;
        /*
         * constructor to initialize the view holder
         */
        public ViewHolder2(View view) {
            super(view);
            // get all of the text views
            title = (TextView) view.findViewById(R.id.title_BasketItem_Text);
            price = (TextView) view.findViewById(R.id.price_BasketItem_Text);
        }
    }

    /*
     * clear the list of items in the current list
     */
    public void clearList() {basketDataHolder.clear();}

    /*
     * add an item to the list of items to show on the recycler view
     */
    public void addValue(String value1, String value2, Float value3) {
        Products products = new Products();
        products.setName(value1);
        products.setPrice(value3);
        BasketData.addProduct(products);
        copyBasketData();
    }

	/*
		this will copy all of the data from the BasketData class and then update the recycler
		view to show this new data
	*/
    public void copyBasketData()
    {
        basketDataHolder = BasketData.getBasketData();
        update();
    }
	
	/*
		this function will handle updating the current total cost and is needed for 
		when the user decides to remove an item from the basket, as the price will update
	*/
    public void updateTotalCost()
    {
		// for all of the products add up the price then set the total to be this number
        ArrayList<Products> products = BasketData.getBasketData();
        float subTotal = 0;
        for (int i = 0; i < products.size(); i++)
        {
            subTotal += products.get(i).getPrice();
        }
        priceText.setText("Total: £ " + Float.toString(subTotal));
    }

    /*
     *  update the data set to show the new added items
     */
    public void update(){ notifyDataSetChanged(); }
}
