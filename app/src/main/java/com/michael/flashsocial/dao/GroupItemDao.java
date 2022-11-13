package com.michael.flashsocial.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.michael.flashsocial.model.GroupItem;

import java.util.List;

@Dao
public interface GroupItemDao {
    @Insert
    void create(GroupItem groupItem);

    @Query("select * from group_item")
    List<GroupItem> getAll();

//    void delete();
}
