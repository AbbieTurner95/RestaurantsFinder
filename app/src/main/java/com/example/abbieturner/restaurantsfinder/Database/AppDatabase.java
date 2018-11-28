package com.example.abbieturner.restaurantsfinder.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

//@Database(entities = {RestaurantsModel.Restaurant.class}, version = 1, exportSchema = false)

    public abstract class AppDatabase extends RoomDatabase {

        private static final Object LOCK = new Object();
        private static final String DATABASE_NAME = "restaurantsDatabase";
        private static AppDatabase sInstance;

        public static AppDatabase getInstance(Context context) {
            if (sInstance == null) {
                synchronized (LOCK) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
            return sInstance;
        }

        public abstract DAORestaurantInterface restaurantsDAO();
    }
