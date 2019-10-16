package com.mobilesoftware.pantryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;

public class CreateItemActivity extends AppCompatActivity {

    private boolean createMode;
    private Food item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        FoodRepository foodRepository = new FoodRepository(getApplicationContext());

        createMode = getIntent().getStringExtra("mode").equals("create");
        if(!createMode) {
            foodRepository.getFood(getIntent().getIntExtra("itemId", 1)).getValue();
        }


    }
}
