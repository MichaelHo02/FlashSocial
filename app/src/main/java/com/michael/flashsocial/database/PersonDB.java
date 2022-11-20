package com.michael.flashsocial.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.michael.flashsocial.dao.PersonDao;
import com.michael.flashsocial.model.Person;

@Database(entities = {Person.class}, version = 4, exportSchema = false)
public abstract class PersonDB extends RoomDatabase {
    private static final String DATABASE_NAME = "person.db";
    private static PersonDB instance;

    public static synchronized PersonDB getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), PersonDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract PersonDao itemDao();
}
