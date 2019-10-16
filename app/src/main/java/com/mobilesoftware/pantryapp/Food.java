package com.mobilesoftware.pantryapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}
