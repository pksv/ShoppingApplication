package com.example.shoppingapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.Adapter.GroceryItemAdapter;
import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Utils;
import com.example.shoppingapplication.Dialog.AllCategoriesDialog;
import com.example.shoppingapplication.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static com.example.shoppingapplication.Dialog.AllCategoriesDialog.ALL_CATEGORIES;
import static com.example.shoppingapplication.Dialog.AllCategoriesDialog.CALLING_ACTIVITY;

public class SearchActivity extends AppCompatActivity implements AllCategoriesDialog.GetCategory {


    private MaterialToolbar toolbar;
    private EditText searchBox;
    private ImageView btnSearch;
    private TextView txtFirstCat, txtSecondCat, txtThirdCat, txtAllCat;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private GroceryItemAdapter adapter;

    @Override
    public void onGetCategoryResult(String category) {
        ArrayList<GroceryItem> items = Utils.getItemsByCategory(this, category);
        if (items != null) {
            adapter.setItems(items);
            increaseUserPoint(items);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initButtonNavView();

        setSupportActionBar(toolbar);

        adapter = new GroceryItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        if (intent != null) {
            String category = intent.getStringExtra("category");
            if (category != null) {
                ArrayList<GroceryItem> items = Utils.getItemsByCategory(this, category);
                if (items != null) {
                    adapter.setItems(items);
                    increaseUserPoint(items);
                }
            }
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSearch();
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayList<String> categories = Utils.getCategories(this);
        if (categories != null) {
            if (categories.size() > 0) {
                if (categories.size() == 1) {
                    showCategories(categories, 1);
                } else if (categories.size() == 2) {
                    showCategories(categories, 2);
                } else {
                    showCategories(categories, 3);
                }
            }
        }

        txtAllCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllCategoriesDialog dialog = new AllCategoriesDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ALL_CATEGORIES, Utils.getCategories(SearchActivity.this));
                bundle.putString(CALLING_ACTIVITY, "search_activity");
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "all category dialog");
            }
        });

    }

    private void showCategories(final ArrayList<String> categories, final int i) {
        switch (i) {
            case 1:
                txtFirstCat.setVisibility(View.VISIBLE);
                txtFirstCat.setText(categories.get(0));
                txtSecondCat.setVisibility(View.GONE);
                txtThirdCat.setVisibility(View.GONE);
                txtFirstCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                break;
            case 2:
                txtFirstCat.setVisibility(View.VISIBLE);
                txtFirstCat.setText(categories.get(0));
                txtSecondCat.setVisibility(View.VISIBLE);
                txtSecondCat.setText(categories.get(1));
                txtThirdCat.setVisibility(View.GONE);
                txtFirstCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                txtSecondCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(1));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                break;
            case 3:
                txtFirstCat.setVisibility(View.VISIBLE);
                txtFirstCat.setText(categories.get(0));
                txtSecondCat.setVisibility(View.VISIBLE);
                txtSecondCat.setText(categories.get(1));
                txtThirdCat.setVisibility(View.VISIBLE);
                txtThirdCat.setText(categories.get(2));
                txtFirstCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(0));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                txtSecondCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(1));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                txtThirdCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(SearchActivity.this, categories.get(2));
                        if (items != null) {
                            adapter.setItems(items);
                            increaseUserPoint(items);
                        }
                    }
                });
                break;
            default:
                txtFirstCat.setVisibility(View.GONE);
                txtSecondCat.setVisibility(View.GONE);
                txtThirdCat.setVisibility(View.GONE);
                break;
        }
    }

    private void initSearch() {
        if (!searchBox.getText().toString().equals("")) {
            String name = searchBox.getText().toString();
            ArrayList<GroceryItem> items = Utils.searchItems(this, name);
            if (items != null) {
                adapter.setItems(items);
                increaseUserPoint(items);
            }
        }
    }

    private void increaseUserPoint(ArrayList<GroceryItem> items) {
        for (GroceryItem i : items) {
            Utils.changeUserPoint(this, i, 1);
        }
    }

    private void initButtonNavView() {
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        Intent cartIntent = new Intent(SearchActivity.this, CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;
                    case R.id.home:
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        searchBox = findViewById(R.id.searchBox);
        btnSearch = findViewById(R.id.btnSearch);
        txtAllCat = findViewById(R.id.txtAllCat);
        txtFirstCat = findViewById(R.id.txtFirstCat);
        txtSecondCat = findViewById(R.id.txtSecondCat);
        txtThirdCat = findViewById(R.id.txtThirdCat);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}