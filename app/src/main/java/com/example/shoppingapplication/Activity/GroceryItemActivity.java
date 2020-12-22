package com.example.shoppingapplication.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapplication.Adapter.ReviewsAdapter;
import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Review;
import com.example.shoppingapplication.Database.Utils;
import com.example.shoppingapplication.Dialog.AddReviewDialogue;
import com.example.shoppingapplication.OtherClasses.TrackUserTime;
import com.example.shoppingapplication.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialogue.AddReview {

    public static final String GROCERY_ITEM_KEY = "incoming";
    private static final String TAG = "GroceryItemActivity";
    private boolean isBound;
    private TrackUserTime mService;
    private TextView txtName, txtDescription, txtPrice, txtAddReview;
    private ImageView itemImage, firstEmptyStar, secondEmptyStar, thirdEmptyStar,
            firstFilledStar, secondFilledStar, thirdFilledStar;
    private Button btnAddToCart;
    private RecyclerView reviewsRV;
    private RelativeLayout firstStarRelLay, secondStarRelLay, thirdStarRelLay;
    private MaterialToolbar toolbar;
    private ReviewsAdapter adapter;
    private GroceryItem incomingItem;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TrackUserTime.LocalBinder binder = (TrackUserTime.LocalBinder) iBinder;
            mService = binder.getService();
            isBound = true;
            mService.setItem(incomingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    public void onAddReviewResult(Review review) {
        Log.d(TAG, "onAddReviewResult: new review " + review);
        Utils.addReview(this, review);
        Utils.changeUserPoint(this, incomingItem, 3);
        ArrayList<Review> reviews = Utils.getReviewsById(this, review.getGroceryId());
        if (reviews != null) {
            adapter.setReviews(reviews);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);
        initViews();

        setSupportActionBar(toolbar);

        adapter = new ReviewsAdapter();

        Intent intent = getIntent();
        if (intent != null) {
            incomingItem = intent.getParcelableExtra(GROCERY_ITEM_KEY);
            if (incomingItem != null) {
                Utils.changeUserPoint(this, incomingItem, 1);
                txtName.setText(incomingItem.getName());
                txtDescription.setText(incomingItem.getDescription());
                txtPrice.setText("$" + incomingItem.getPrice());
                Glide.with(this)
                        .asBitmap()
                        .load(incomingItem.getImageUrl())
                        .into(itemImage);
                ArrayList<Review> reviews = Utils.getReviewsById(this, incomingItem.getId());
                reviewsRV.setAdapter(adapter);
                reviewsRV.setLayoutManager(new LinearLayoutManager(this));
                if (reviews != null) {
                    if (reviews.size() > 0) {
                        adapter.setReviews(reviews);
                    }
                }

                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.addItemToCart(GroceryItemActivity.this, incomingItem);
                        Intent cartIntent = new Intent(GroceryItemActivity.this, CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                    }
                });

                txtAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddReviewDialogue dialogue = new AddReviewDialogue();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(GROCERY_ITEM_KEY, incomingItem);
                        dialogue.setArguments(bundle);
                        dialogue.show(getSupportFragmentManager(), "add review");
                    }
                });

                handleRating();
            }

        }
    }

    private void handleRating() {
        switch (incomingItem.getRate()) {
            case 0:
                firstEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 1:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 2:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 3:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        firstStarRelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (incomingItem.getRate() != 1) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 1);

                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (1 - incomingItem.getRate()) * 2);

                    incomingItem.setRate(1);
                    handleRating();
                }
            }
        });

        secondStarRelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (incomingItem.getRate() != 2) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 2);

                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (2 - incomingItem.getRate()) * 2);

                    incomingItem.setRate(2);
                    handleRating();
                }
            }
        });

        thirdStarRelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (incomingItem.getRate() != 3) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 3);

                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (3 - incomingItem.getRate()) * 2);

                    incomingItem.setRate(3);
                    handleRating();
                }
            }
        });
    }

    private void initViews() {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddReview = findViewById(R.id.txtAddReview);
        itemImage = findViewById(R.id.itemImage);
        firstEmptyStar = findViewById(R.id.firstEmptyStar);
        firstFilledStar = findViewById(R.id.firstFilledStar);
        secondEmptyStar = findViewById(R.id.secondEmptyStar);
        secondFilledStar = findViewById(R.id.secondFilledStar);
        thirdEmptyStar = findViewById(R.id.thirdEmptyStar);
        thirdFilledStar = findViewById(R.id.thirdFilledStar);
        firstStarRelLay = findViewById(R.id.firstStarRelLay);
        secondStarRelLay = findViewById(R.id.secondStarRelLay);
        thirdStarRelLay = findViewById(R.id.thirdStarRelLay);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        reviewsRV = findViewById(R.id.reviewsRV);
        toolbar = findViewById(R.id.toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TrackUserTime.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(connection);
        }
    }
}