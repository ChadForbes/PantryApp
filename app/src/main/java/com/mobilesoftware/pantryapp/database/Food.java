package com.mobilesoftware.pantryapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public float amount;

    @ColumnInfo(name = "amountType")
    public String amountType;

    @ColumnInfo(name = "aliases")
    public String aliases;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "expiry_date")
    public String expiryDate;

    @ColumnInfo(name = "image_uri")
    public String imageUri;

    @ColumnInfo(name = "searchable")
    public boolean searchable = false;

    @ColumnInfo(name = "shopping_list")
    public boolean shoppingList = false;

    @ColumnInfo(name = "fruit")
    public boolean fruit = false;

    @ColumnInfo(name = "vegetable")
    public boolean vegetable = false;

    @ColumnInfo(name = "carb")
    public boolean carb = false;

    @ColumnInfo(name = "meat")
    public boolean meat = false;

    @ColumnInfo(name = "dairy")
    public boolean dairy = false;
}
