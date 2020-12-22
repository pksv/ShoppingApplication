package com.example.shoppingapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Order;
import com.example.shoppingapplication.Database.Utils;
import com.example.shoppingapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondCartFragment extends Fragment {

    public static final String ORDER_KEY = "order";

    private EditText etAddress, etEmail, etPhoneNumber, etZipCode;
    private Button btnBack, btnNext;
    private TextView txtWarning;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_second, container, false);

        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<Order>() {
                }.getType();
                Order order = gson.fromJson(jsonOrder, type);
                if (order != null) {
                    etPhoneNumber.setText(order.getPhoneNumber());
                    etZipCode.setText(order.getZipCode());
                    etAddress.setText(order.getAddress());
                    etEmail.setText(order.getEmail());
                }

            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new FirstCartFragment());
                transaction.commit();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {
                    txtWarning.setVisibility(View.GONE);

                    ArrayList<GroceryItem> items = Utils.getCartItems(getActivity());
                    if (items != null) {
                        Order order = new Order();
                        order.setItems(items);
                        order.setAddress(etAddress.getText().toString());
                        order.setEmail(etEmail.getText().toString());
                        order.setPhoneNumber(etPhoneNumber.getText().toString());
                        order.setZipCode(etZipCode.getText().toString());
                        order.setTotalPrice(calcTotalPrice(items));

                        Gson gson = new Gson();
                        String jsonOrder = gson.toJson(order);
                        Bundle bundle = new Bundle();
                        bundle.putString(ORDER_KEY, jsonOrder);
                        ThirdCartFragment thirdCartFragment = new ThirdCartFragment();
                        thirdCartFragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, thirdCartFragment);
                        transaction.commit();
                    }

                } else {
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please Fill The Details");
                }

            }
        });
        return view;
    }

    private boolean validateData() {
        return !etAddress.getText().toString().equals("") && !etEmail.getText().toString().equals("") &&
                !etPhoneNumber.getText().toString().equals("") && !etZipCode.getText().toString().equals("");
    }

    private void initViews(View view) {
        etAddress = view.findViewById(R.id.etAddress);
        etEmail = view.findViewById(R.id.etEmail);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etZipCode = view.findViewById(R.id.etZipCode);
        btnBack = view.findViewById(R.id.btnBack);
        btnNext = view.findViewById(R.id.btnNext);
        txtWarning = view.findViewById(R.id.txtWarning);
    }

    private double calcTotalPrice(ArrayList<GroceryItem> items) {
        double price = 0;
        for (GroceryItem item : items) {
            price += item.getPrice();
        }

        price = Math.round(price * 100.0) / 100.0;
        return price;
    }
}
