package com.mobilesoftware.pantryapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void insertFood(Food food);

    @Query("SELECT * FROM Food")
    LiveData<List<Food>> getAllFoods();

    @Query("SELECT * FROM Food WHERE uid =:foodId")
    LiveData<Food> getFood(int foodId);

    @Query("SELECT * FROM Food WHERE fruit")
    LiveData<List<Food>> getFruit();

    @Query("SELECT * FROM Food WHERE dairy")
    LiveData<List<Food>> getDairy();

    @Query("SELECT * FROM Food WHERE meat")
    LiveData<List<Food>> getMeat();

    @Query("SELECT * FROM Food WHERE carb")
    LiveData<List<Food>> getCarb();

    @Query("SELECT * FROM Food WHERE vegetable")
    LiveData<List<Food>> getVegetable();


    @Query("SELECT * FROM Food WHERE name LIKE :foodName AND shopping_list = :shoppingList")
    LiveData<List<Food>> getFoodByName(String foodName, Boolean shoppingList);

    @Query("SELECT * FROM Food WHERE name LIKE :foodName AND fruit = :fruit AND meat = :meat AND carb = :carb AND dairy = :dairy AND vegetable = :vegetable AND shopping_list = :shoppingList")
    LiveData<List<Food>> getFoodByNameAndType(String foodName, Boolean fruit, Boolean dairy, Boolean meat, Boolean carb, Boolean vegetable, Boolean shoppingList);

    @Query("SELECT * FROM Food WHERE searchable")
    LiveData<List<Food>> getFoodSearchable();

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);
}
