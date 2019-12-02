package com.mobilesoftware.pantryapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mobilesoftware.pantryapp.database.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void insertFood(Food food);

    @Query("SELECT * FROM Food")
    LiveData<List<Food>> getAllFoods();

    @Query("SELECT * FROM Food WHERE uid =:foodId")
    LiveData<Food> getFood(int foodId);

    @Query("SELECT * FROM Food WHERE name LIKE :foodName")
    LiveData<List<Food>> getFoodByName(String foodName);

    @Query("SELECT * FROM Food WHERE searchable")
    LiveData<List<Food>> getFoodSearchable();

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);
}
