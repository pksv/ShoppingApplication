package com.example.shoppingapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.Adapter.CartAdapter;
import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Utils;
import com.example.shoppingapplication.R;

import java.util.ArrayList;

public class FirstCartFragment extends Fragment implements CartAdapter.DeleteItem, CartAdapter.TotalPrice {

    private TextView txtTotalPrice, txtNoItems;
    private Button btnNext;
    private RecyclerView recyclerView;
    private RelativeLayout itemsRL;
    private CartAdapter adapter;

    @Override
    public void getTotalPrice(double price) {
        txtTotalPrice.setText("$" + price);
    }

    @Override
    public void onDeleteResult(GroceryItem item) {
        Utils.deleteItemFromCart(getActivity(), item);
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if (cartItems != null) {
            if (cartItems.size() != 0) {
                txtNoItems.setVisibility(View.GONE);
                itemsRL.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            } else {
                txtNoItems.setVisibility(View.VISIBLE);
                itemsRL.setVisibility(View.GONE);
            }
        } else {
            txtNoItems.setVisibility(View.VISIBLE);
            itemsRL.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_first, container, false);

        initViews(view);

        adapter = new CartAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if (cartItems != null) {
            if (cartItems.size() != 0) {
                txtNoItems.setVisibility(View.GONE);
                itemsRL.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            } else {
                txtNoItems.setVisibility(View.VISIBLE);
                itemsRL.setVisibility(View.GONE);
            }
        } else {
            txtNoItems.setVisibility(View.VISIBLE);
            itemsRL.setVisibility(View.GONE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new SecondCartFragment());
                transaction.commit();
            }
        });
        return view;
    }

    private void initViews(View view) {
        txtNoItems = view.findViewById(R.id.txtNoItems);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        btnNext = view.findViewById(R.id.btnNext);
        recyclerView = view.findViewById(R.id.recyclerView);
        itemsRL = view.findViewById(R.id.itemsRL);
    }
}
