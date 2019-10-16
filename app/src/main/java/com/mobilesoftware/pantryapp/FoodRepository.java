package com.mobilesoftware.pantryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

public class FoodRepository {
    private String DB_NAME = "pantry-app-database";

    private FoodDatabase foodDatabase;

    public FoodRepository(Context context) {
        foodDatabase = Room.databaseBuilder(context,FoodDatabase.class, DB_NAME)
                .build();
    }

    public void insertFood(String name, int amount, String amountType, String aliases) {
        Food food = new Food();
        food.name = name;
        food.amount = amount;
        food.amountType = amountType;
        food.aliases = aliases;
        insertFood(food);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertFood(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDatabase.foodDao().insertFood(food);
                return null;
            }
        }.execute();
    }

    public void deleteFood(final int id) {
        final LiveData<Food> food = getFood(id);
        if(food != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    foodDatabase.foodDao().deleteFood(food.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void updateFood(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDatabase.foodDao().updateFood(food);
                return null;
            }
        }.execute();
    }

    public void deleteFood(final Food food) {
        if(food != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    foodDatabase.foodDao().deleteFood(food);
                    return null;
                }
            }.execute();
        }
    }

    public LiveData<Food> getFood(int id) {
        return foodDatabase.foodDao().getFood(id);
    }

    public LiveData<List<Food>> getFoods() {
        return foodDatabase.foodDao().getAllFoods();
    }
}
