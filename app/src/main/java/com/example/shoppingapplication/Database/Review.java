package com.example.shoppingapplication.Database;

public class Review {
    private int groceryId;
    private String userName, text, date;

    public Review(int groceryId, String userName, String text, String date) {
        this.groceryId = groceryId;
        this.userName = userName;
        this.text = text;
        this.date = date;
    }

    public int getGroceryId() {
        return groceryId;
    }

    public void setGroceryId(int groceryId) {
        this.groceryId = groceryId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Review{" +
                "groceryId=" + groceryId +
                ", userName='" + userName + '\'' +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}