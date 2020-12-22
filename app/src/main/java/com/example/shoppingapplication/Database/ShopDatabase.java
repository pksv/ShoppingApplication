package com.example.shoppingapplication.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

@Database(entities = {GroceryItem.class, CartItem.class}, version = 1)
public abstract class ShopDatabase extends RoomDatabase {
    private static ShopDatabase instance;
    private static final RoomDatabase.Callback initialCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new InitialAsyncTask(instance).execute();

        }
    };

    public static synchronized ShopDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ShopDatabase.class, "shop_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(initialCallback)
                    .build();
        }
        return instance;
    }

    public abstract GroceryItemDAO groceryItemDAO();

    public abstract CartItemDAO cartItemDAO();

    private static class InitialAsyncTask extends AsyncTask<Void, Void, Void> {

        private final GroceryItemDAO groceryItemDAO;

        public InitialAsyncTask(ShopDatabase db) {
            this.groceryItemDAO = db.groceryItemDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<GroceryItem> allItems = new ArrayList<>();

            GroceryItem milk = new GroceryItem("Milk",
                    "Milk is a white, nutrient-rich liquid food produced in the mammary glands of mammals. It is the primary source of nutrition for infant mammals before they are able to digest other types of food. Early-lactation milk contains colostrum, which carries the mother's antibodies to its young and can reduce the risk of many diseases.",
                    "https://pngimg.com/uploads/milk/milk_PNG12732.png",
                    "Drink", 0.68, 10);
            allItems.add(milk);

            GroceryItem iceCream = new GroceryItem("Ice Cream",
                    "Ice cream is a sweetened frozen food typically eaten as a snack or dessert. It may be made from dairy milk or cream and is flavoured with a sweetener, either sugar or an alternative, and any spice, such as cocoa or vanilla. Colourings are usually added, in addition to stabilizers.",
                    "https://lacremiere.com/wp-content/uploads/2017/05/CremeDure-549x1024.png",
                    "Dessert", 0.81, 50);
            allItems.add(iceCream);

            GroceryItem softDrink = new GroceryItem("Soft Drink",
                    "A soft drink is a drink that usually contains carbonated water, a sweetener, and a natural or artificial flavoring. The sweetener may be a sugar, high-fructose corn syrup, fruit juice, a sugar substitute, or some combination of these. Soft drinks may also contain caffeine, colorings, preservatives, and/or other ingredients",
                    "https://purepng.com/public/uploads/medium/purepng.com-coca-cola-bottlecokecoca-colabeveragedrinksoft-drinkcoke-bottle-1411527237794ycvms.png",
                    "Drink", 0.41, 20);
            allItems.add(softDrink);

            GroceryItem spaghetti = new GroceryItem("Spaghetti",
                    "Spaghetti is a long, thin, solid, cylindrical pasta. It is a staple food of traditional Italian cuisine. Like other pasta, spaghetti is made of milled wheat and water and sometimes enriched with vitamins and minerals. Italian spaghetti is typically made from durum wheat semolina.",
                    "https://pngimg.com/uploads/spaghetti/spaghetti_PNG3.png",
                    "Food", 0.81, 20);
            allItems.add(spaghetti);

            GroceryItem soap = new GroceryItem("Soap",
                    "Soap is a salt of a fatty acid used in a variety of cleansing and lubricating products. In a domestic setting, soaps are surfactants usually used for washing, bathing, and other types of housekeeping.",
                    "https://pngimg.com/uploads/soap/soap_PNG48.png",
                    "Cleanser", 0.61, 30);
            allItems.add(soap);

            GroceryItem juice = new GroceryItem("Juice",
                    "Juice is a drink made from the extraction or pressing of the natural liquid contained in fruit and vegetables. It can also refer to liquids that are flavored with concentrate or other biological food sources.",
                    "https://juiceburst.com/wp-content/themes/fireflytonics/dist/img/homepage-bottle.png",
                    "Drink", 0.27, 15);
            allItems.add(juice);

            GroceryItem cashew = new GroceryItem("Cashew",
                    "The cashew seed is often considered a nut in the culinary sense; this cashew nut is eaten on its own, used in recipes, or processed into cashew cheese or cashew butter. Like the tree, the nut is often simply called a cashew.",
                    "https://www.cloverleaffarmherbs.com/wp-content/uploads/2010/12/cashews.png",
                    "Nuts", 13.5, 10);
            allItems.add(cashew);

            GroceryItem pistachio = new GroceryItem("Pistachio",
                    "The pistachio, a member of the cashew family, is a small tree originating from Central Asia and the Middle East. The tree produces seeds that are widely consumed as food. Pistacia vera often is confused with other species in the genus Pistacia that are also known as pistachio",
                    "https://pngimg.com/uploads/pistachio/pistachio_PNG32.png",
                    "Nuts", 10.1, 8);
            allItems.add(pistachio);

            GroceryItem shampoo = new GroceryItem("Shampoo",
                    "Shampoo (/ ʃ æ m ˈ p uː /) is a hair care product, typically in the form of a viscous liquid, that is used for cleaning hair.Less commonly, shampoo is available in bar form, like a bar of soap. Shampoo is used by applying it to wet hair, massaging the product into the scalp, and then rinsing it out.",
                    "https://bc-images.imgix.net/product-images%2F3032%2Fimgs%2Fpdp-new-daily-shampoo.png",
                    "Cleanser", 2.03, 50);
            allItems.add(shampoo);

            for (GroceryItem g :
                    allItems) {
                groceryItemDAO.insert(g);
            }


            return null;
        }
    }
}
