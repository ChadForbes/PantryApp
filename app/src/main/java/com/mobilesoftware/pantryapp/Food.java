package com.mobilesoftware.pantryapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="amount")
    public float amount;

    @ColumnInfo(name="amountType")
    public String amountType;

    @ColumnInfo(name="aliases")
    public String aliases;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "expiry_date")
    public String expiryDate;

    @ColumnInfo(name="searchable")
    public boolean searchable = false;
}
