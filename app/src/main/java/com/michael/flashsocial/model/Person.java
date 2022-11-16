package com.michael.flashsocial.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private Calendar dob;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] avatar;
    private String role;
    private String uniqueFeature;

    public Person(String firstName, String lastName, Calendar dob, byte[] avatar, String role, String uniqueFeature) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.avatar = avatar;
        this.role = role;
        this.uniqueFeature = uniqueFeature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Calendar getDob() {
        return dob;
    }

    public void setDob(Calendar dob) {
        this.dob = dob;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUniqueFeature() {
        return uniqueFeature;
    }

    public void setUniqueFeature(String uniqueFeature) {
        this.uniqueFeature = uniqueFeature;
    }
}
