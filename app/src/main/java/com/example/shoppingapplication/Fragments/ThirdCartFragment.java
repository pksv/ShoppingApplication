package com.example.shoppingapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shoppingapplication.Activity.PayPalActivity;
import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Order;
import com.example.shoppingapplication.OtherClasses.OrderEndPoint;
import com.example.shoppingapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.shoppingapplication.Fragments.SecondCartFragment.ORDER_KEY;

public class ThirdCartFragment extends Fragment {
    private static final String TAG = "ThirdCartFragment";
    private TextView txtItems, txtPrice, txtAddress, txtPhone;
    private RadioGroup rgPayMethod;
    private Button btnCheckout, btnBack;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_third, container, false);
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final String jsonOrder = bundle.getString(ORDER_KEY);
            if (jsonOrder != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<Order>() {
                }.getType();
                final Order order = gson.fromJson(jsonOrder, type);
                if (order != null) {
                    String items = "";
                    for (GroceryItem i : order.getItems()) {
                        items += "\n\t" + i.getName();
                    }
                    txtItems.setText(items);
                    txtPhone.setText(order.getPhoneNumber());
                    txtPrice.setText(String.valueOf(order.getTotalPrice()));
                    txtAddress.setText(order.getAddress());

                    btnBack.setOnClickListener(view12 -> {
                        Bundle backBundle = new Bundle();
                        backBundle.putString(ORDER_KEY, jsonOrder);
                        SecondCartFragment secondCartFragment = new SecondCartFragment();
                        secondCartFragment.setArguments(backBundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, secondCartFragment);
                        transaction.commit();
                    });

                    btnCheckout.setOnClickListener(view1 -> {
                        switch (rgPayMethod.getCheckedRadioButtonId()) {
                            case R.id.rbCredit:
                                order.setPaymentMethod("Credit Card");
                                break;
                            case R.id.rbDebit:
                                order.setPaymentMethod("Debit Card");
                                break;
                            case R.id.rbPayPal:
                                Intent intent = new Intent(getActivity(), PayPalActivity.class);
                                intent.putExtra(ORDER_KEY, jsonOrder);
                                getActivity().startActivity(intent);
                                break;
                            default:
                                order.setPaymentMethod("Unknown");
                                break;
                        }

                        order.setSuccess(true);

                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY);

                        OkHttpClient client = new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://jsonplaceholder.typicode.com/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(client)
                                .build();

                        OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
                        Call<Order> call = endPoint.newOrder(order);
                        call.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                Log.d(TAG, "onResponse: code" + response.code());
                                if (response.isSuccessful()) {
                                    Bundle resultBundle = new Bundle();
                                    resultBundle.putString(ORDER_KEY, gson.toJson(response.body()));
                                    PaymentResultFragment paymentResultFragment = new PaymentResultFragment();
                                    paymentResultFragment.setArguments(resultBundle);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, paymentResultFragment);
                                    transaction.commit();
                                } else {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, new PaymentResultFragment());
                                    transaction.commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    });

                }

            }
        }

        return view;
    }

    private void initViews(View view) {
        txtAddress = view.findViewById(R.id.txtAddress);
        txtItems = view.findViewById(R.id.txtItems);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtPhone = view.findViewById(R.id.txtPhone);
        rgPayMethod = view.findViewById(R.id.rgPayMethod);
        btnBack = view.findViewById(R.id.btnBack);
        btnCheckout = view.findViewById(R.id.btnCheckout);
    }
}
