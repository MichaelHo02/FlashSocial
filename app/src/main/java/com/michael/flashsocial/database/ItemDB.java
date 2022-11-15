package com.michael.flashsocial.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.michael.flashsocial.dao.ItemDao;
import com.michael.flashsocial.model.Item;

@Database(entities = Item.class, version = 3, exportSchema = false)
public abstract class ItemDB extends RoomDatabase {
    private static final String DATABASE_NAME = "groupItem.db";
    private static ItemDB instance;

    public static synchronized ItemDB getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), ItemDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
}
