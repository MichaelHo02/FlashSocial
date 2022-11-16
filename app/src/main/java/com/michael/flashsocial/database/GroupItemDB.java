package com.michael.flashsocial.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.michael.flashsocial.dao.GroupItemDao;
import com.michael.flashsocial.model.GroupItem;

@Database(entities = {GroupItem.class}, version = 1, exportSchema = false)
public abstract class GroupItemDB extends RoomDatabase {
    private static final String DATABASE_NAME = "groupItem.db";
    private static GroupItemDB instance;

    public static synchronized GroupItemDB getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), GroupItemDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract GroupItemDao groupItemDao();
}
