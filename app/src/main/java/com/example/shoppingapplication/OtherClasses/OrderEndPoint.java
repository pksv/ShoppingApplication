package com.example.shoppingapplication.OtherClasses;

import com.example.shoppingapplication.Database.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderEndPoint {

    @POST("posts")
    Call<Order> newOrder(@Body Order order);
}
