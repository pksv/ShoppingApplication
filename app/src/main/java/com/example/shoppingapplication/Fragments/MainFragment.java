package com.example.shoppingapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.Activity.CartActivity;
import com.example.shoppingapplication.Activity.SearchActivity;
import com.example.shoppingapplication.Adapter.GroceryItemAdapter;
import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Utils;
import com.example.shoppingapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainFragment extends Fragment {
    private RecyclerView rvNewItems, rvPopularItems, rvSuggestions;
    private GroceryItemAdapter adapterNI, adapterPI, adapterS;


    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initViews(view);
        initButtonNavView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecView();
    }

    private void initRecView() {
        adapterNI = new GroceryItemAdapter(getActivity());
        rvNewItems.setAdapter(adapterNI);
        rvNewItems.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        adapterPI = new GroceryItemAdapter(getActivity());
        rvPopularItems.setAdapter(adapterPI);
        rvPopularItems.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        adapterS = new GroceryItemAdapter(getActivity());
        rvSuggestions.setAdapter(adapterS);
        rvSuggestions.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        ArrayList<GroceryItem> newItems = Utils.getAllItems(getActivity());
        if (newItems != null) {
            final Comparator<GroceryItem> newItemsComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem item1, GroceryItem item2) {
                    return item1.getId() - item2.getId();
                }
            };
            Comparator<GroceryItem> reverseComparator = Collections.reverseOrder(newItemsComparator);
            Collections.sort(newItems, reverseComparator);
            adapterNI.setItems(newItems);
        }

        ArrayList<GroceryItem> popularItems = Utils.getAllItems(getActivity());
        if (popularItems != null) {
            final Comparator<GroceryItem> popularItemsComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem item1, GroceryItem item2) {
                    return item1.getPopularityPoint() - item2.getPopularityPoint();
                }
            };
            Collections.sort(popularItems, Collections.reverseOrder(popularItemsComparator));
            adapterPI.setItems(popularItems);
        }

        ArrayList<GroceryItem> suggestedItems = Utils.getAllItems(getActivity());
        if (suggestedItems != null) {
            final Comparator<GroceryItem> suggestedItemsComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem item1, GroceryItem item2) {
                    return item1.getUserPoint() - item2.getUserPoint();
                }
            };
            Collections.sort(suggestedItems, Collections.reverseOrder(suggestedItemsComparator));
            adapterS.setItems(suggestedItems);
        }
    }

    private void initButtonNavView() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;
                    case R.id.search:
                        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                        searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(searchIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        rvNewItems = view.findViewById(R.id.rvNewItems);
        rvPopularItems = view.findViewById(R.id.rvPopularItems);
        rvSuggestions = view.findViewById(R.id.rvSuggestion);
    }
}
