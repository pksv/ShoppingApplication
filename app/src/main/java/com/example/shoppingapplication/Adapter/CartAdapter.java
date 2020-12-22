package com.example.shoppingapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private final Fragment fragment;
    private DeleteItem deleteItem;
    private TotalPrice totalPrice;
    private ArrayList<GroceryItem> items = new ArrayList<>();

    public CartAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtName.setText(items.get(position).getName());
        holder.txtPrice.setText("$" + items.get(position).getPrice());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Deleting...")
                        .setMessage("Are you sure to delete this?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    deleteItem = (DeleteItem) fragment;
                                    deleteItem.onDeleteResult(items.get(position));
                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void calcTotalPrice() {
        double price = 0;
        for (GroceryItem item :
                items) {
            price += item.getPrice();
        }

        price = Math.round(price * 100.0) / 100.0;

        try {
            totalPrice = (TotalPrice) fragment;
            totalPrice.getTotalPrice(price);

        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public void setItems(ArrayList<GroceryItem> items) {
        this.items = items;
        calcTotalPrice();
        notifyDataSetChanged();
    }

    public interface DeleteItem {
        void onDeleteResult(GroceryItem item);
    }

    public interface TotalPrice {
        void getTotalPrice(double price);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final TextView txtPrice;
        private final ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}