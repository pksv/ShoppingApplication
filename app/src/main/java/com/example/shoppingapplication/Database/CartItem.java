package com.example.shoppingapplication.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {

    private final int groceryItemId;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public CartItem(int groceryItemId) {
        this.groceryItemId = groceryItemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroceryItemId() {
        return groceryItemId;
    }
}
