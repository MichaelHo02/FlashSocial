package com.michael.flashsocial.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "group_item")
public class GroupItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int itemSize;
    private Boolean isLearning;

    public GroupItem(String name, int itemSize, Boolean isLearning) {
        this.name = name;
        this.itemSize = itemSize;
        this.isLearning = isLearning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public Boolean getIsLearning() {
        return isLearning;
    }

    public void setIsLearning(Boolean isLearning) {
        this.isLearning = isLearning;
    }
}
