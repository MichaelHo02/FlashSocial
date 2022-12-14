package com.michael.flashsocial.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.michael.flashsocial.model.Person;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("select * from person")
    List<Person> getAllPeople();

    @Query("select * from person where person.chooseToLearn is 1")
    List<Person> getAllChosenPeople();

    @Insert
    void insertItem(Person person);

    @Update
    void updateItem(Person person);

    @Delete
    void deleteItem(Person person);

    @Query("select person.role from person")
    List<String> getAllUniqueRole();

    @Query("DELETE FROM person")
    void deleteAll();
}
