package com.example.shoppingapplication.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GroceryItemDAO {

    @Insert
    void insert(GroceryItem groceryItem);

    @Query("SELECT * FROM grocery_items")
    List<GroceryItem> getAllItems();

    @Query("UPDATE grocery_items SET rate=:newRate WHERE id=:id")
    void updateRate(int id, int newRate);

    @Query("SELECT * FROM grocery_items WHERE id=:id")
    GroceryItem getItemById(int id);

    @Query("UPDATE grocery_items SET reviews=:reviews WHERE id=:id")
    void updateReviews(int id, String reviews);

    @Query("SELECT * FROM grocery_items WHERE name LIKE :text")
    List<GroceryItem> searchItems(String text);

    @Query("SELECT DISTINCT categories FROM grocery_items")
    List<String> getCategories();

    @Query("SELECT * FROM grocery_items WHERE categories=:category")
    List<GroceryItem> getItemsByCategory(String category);

    @Query("UPDATE grocery_items SET popularityPoint=:points WHERE id=:id")
    void updatePopularityPoint(int id, int points);

    @Query("UPDATE grocery_items SET userPoint=:points WHERE id=:id")
    void updateUserPoint(int id, int points);
}
