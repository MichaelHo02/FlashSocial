package com.michael.flashsocial.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "person")
public class Person implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private long dob;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] avatar;
    private String role;
    private String uniqueFeature;
    private boolean chooseToLearn;
    private int correctGuess;
    private int incorrectGuess;

    public Person(String firstName, String lastName, long dob, byte[] avatar, String role, String uniqueFeature, boolean chooseToLearn, int correctGuess, int incorrectGuess) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.avatar = avatar;
        this.role = role;
        this.uniqueFeature = uniqueFeature;
        this.chooseToLearn = chooseToLearn;
        this.correctGuess = correctGuess;
        this.incorrectGuess = incorrectGuess;
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

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
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

    public boolean isChooseToLearn() {
        return chooseToLearn;
    }

    public void setChooseToLearn(boolean chooseToLearn) {
        this.chooseToLearn = chooseToLearn;
    }

    public void toggleLearn() {
        this.chooseToLearn = !this.chooseToLearn;
    }

    public int getCorrectGuess() {
        return correctGuess;
    }

    public void setCorrectGuess(int correctGuess) {
        this.correctGuess = correctGuess;
    }

    public void incrementCorrectGuess() {
        correctGuess++;
    }

    public int getIncorrectGuess() {
        return incorrectGuess;
    }

    public void setIncorrectGuess(int incorrectGuess) {
        this.incorrectGuess = incorrectGuess;
    }

    public void incrementIncorrectGuess() {
        incorrectGuess++;
    }
}
