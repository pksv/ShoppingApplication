package com.example.shoppingapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppingapplication.Activity.MainActivity;
import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Order;
import com.example.shoppingapplication.Database.Utils;
import com.example.shoppingapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static com.example.shoppingapplication.Fragments.SecondCartFragment.ORDER_KEY;

public class PaymentResultFragment extends Fragment {

    private Button btnHome;
    private TextView txtMessage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_result, container, false);

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
                    if (order.isSuccess()) {
                        txtMessage.setText("Your Payment was Successful\nYour order will arrive in 7 days");
                        Utils.clearCartItem(getActivity());
                        for (GroceryItem i :
                                order.getItems()) {
                            Utils.increasePopularityPoint(getActivity(), i, 1);
                            Utils.changeUserPoint(getActivity(), i, 4);
                        }

                    } else {
                        txtMessage.setText("Payment Failed\nPlease try another method");
                    }
                } else {
                    txtMessage.setText("Payment Failed\nPlease try another method");
                }
            }
        }


        btnHome.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        return view;
    }

    private void initViews(View view) {
        btnHome = view.findViewById(R.id.btnHome);
        txtMessage = view.findViewById(R.id.txtMessage);
    }
}
