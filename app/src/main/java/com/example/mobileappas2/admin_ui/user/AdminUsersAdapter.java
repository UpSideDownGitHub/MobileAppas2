package com.example.mobileappas2.admin_ui.user;

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

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.ViewHolder2> {

    // Private variables
    private ArrayList<AdminUserData> userData;
    private Context context;
    private FragmentActivity activity;
    // Public variables
    public int globalPosition;

    /*
     * Constructor for initialization
     */
    public AdminUsersAdapter(FragmentActivity givenActivity, Context context, ArrayList givenItems) {
        this.context = context;
        this.userData = givenItems;
        this.activity = givenActivity;
    }

    /*
     * this class will initialize the view holder for the recycler view
     */
    @NonNull
    @Override
    public AdminUsersAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create the new viewHolder and return it
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_item_fragment, parent, false);
        AdminUsersAdapter.ViewHolder2 viewHolder = new AdminUsersAdapter.ViewHolder2(view);

        view.setOnClickListener(v -> {
            // get the position of the adapter
            globalPosition = viewHolder.getAbsoluteAdapterPosition();
            int pos = globalPosition;
            // get the index of the item pressed in the note list
            int index = userData.indexOf(userData.get(pos));
            // set the current index in shared preferences
            //Log.i("DEBUG", "Current Item Index: " + index);

            // NEED TO OPEN THE EDIT WINDOW WITH THIS PRODUCT
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("adminSelectedUser", userData.get(index).getID());
            editor.apply();
            Navigation.findNavController(view).navigate(R.id.navigation_admin_edit_user);
        });

        return viewHolder;
    }

    /*
     * Bind the date to the specified position on screen
     */
    @Override
    public void onBindViewHolder(@NonNull AdminUsersAdapter.ViewHolder2 holder, int position) {
        // get the date at the current position
        final AdminUserData quizDataHolder1 = userData.get(position);
        // set the name, score, and date from the data at the specified position
        holder.username.setText(quizDataHolder1.getTitle());
        holder.email.setText(quizDataHolder1.getDescription());
    }

    /*
     * returns to total item count
     */
    @Override
    public int getItemCount() {
        return userData.size();
    }

    /*
     * Initialize the view, getting all of the text views that will be edited
     */
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        // private variables
        TextView username;
        TextView email;
        /*
         * constructor to initialize the view holder
         */
        public ViewHolder2(View view) {
            super(view);
            // get all of the text views
            username = (TextView) view.findViewById(R.id.admin_username_user_text);
            email = (TextView) view.findViewById(R.id.admin_email_user_text);
        }
    }

    /*
     *  update the data set to show the new added items
     */
    public void update(){ notifyDataSetChanged(); }
}
