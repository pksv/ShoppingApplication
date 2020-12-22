package com.example.shoppingapplication.Database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

@Entity(tableName = "grocery_items")
public class GroceryItem implements Parcelable {

    @Ignore
    public static final Creator<GroceryItem> CREATOR = new Creator<GroceryItem>() {
        @Override
        public GroceryItem createFromParcel(Parcel in) {
            return new GroceryItem(in);
        }

        @Override
        public GroceryItem[] newArray(int size) {
            return new GroceryItem[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private String categories;
    private double price;
    private int availableAmt;
    private int rate;
    private int userPoint;
    private int popularityPoint;
    @TypeConverters(ReviewsConverter.class)
    private ArrayList<Review> reviews;

    public GroceryItem(int availableAmt, int rate, int userPoint, int popularityPoint, String name, String description, String imageUrl, String categories, double price, ArrayList<Review> reviews) {
        this.availableAmt = availableAmt;
        this.rate = rate;
        this.userPoint = userPoint;
        this.popularityPoint = popularityPoint;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categories = categories;
        this.price = price;
        this.reviews = reviews;
    }

    @Ignore
    public GroceryItem(String name, String description, String imageUrl, String categories, double price, int availableAmt) {
        this.availableAmt = availableAmt;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categories = categories;
        this.price = price;
        this.rate = 0;
        this.userPoint = 0;
        this.popularityPoint = 0;
        reviews = new ArrayList<>();
    }

    @Ignore
    protected GroceryItem(Parcel in) {
        id = in.readInt();
        availableAmt = in.readInt();
        rate = in.readInt();
        userPoint = in.readInt();
        popularityPoint = in.readInt();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        categories = in.readString();
        price = in.readDouble();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(int availableAmt) {
        this.availableAmt = availableAmt;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public int getPopularityPoint() {
        return popularityPoint;
    }

    public void setPopularityPoint(int popularityPoint) {
        this.popularityPoint = popularityPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Ignore
    @Override
    public String toString() {
        return "GroceryItem{" +
                "id=" + id +
                ", availableAmt=" + availableAmt +
                ", rate=" + rate +
                ", userPoint=" + userPoint +
                ", popularityPoint=" + popularityPoint +
                ", name='" + name + '\'' +
                ", desc='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories='" + categories + '\'' +
                ", price=" + price +
                ", reviews=" + reviews +
                '}';
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(availableAmt);
        parcel.writeInt(rate);
        parcel.writeInt(userPoint);
        parcel.writeInt(popularityPoint);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeString(categories);
        parcel.writeDouble(price);
    }
}
