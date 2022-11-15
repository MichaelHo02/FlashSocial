package com.michael.flashsocial.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.michael.flashsocial.model.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("Select * from item")
    List<Item> getAllItems();

    @Insert
    void insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);


}
