package com.mobilesoftware.pantryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mobilesoftware.pantryapp.ui.main.MyPantryFragment;
import com.mobilesoftware.pantryapp.ui.main.ViewPagerAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    FoodRepository foodRepository;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foodRepository = new FoodRepository(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new MyPantryFragment(), "My Pantry");
        adapter.AddFragment(new RecipeFragment(), "Recipes");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Food food = new Food();
        food.name = "Salmon";
        food.aliases = "";
        food.amount = 16;
        food.amountType = "lb";

        foodRepository.insertFood(food);
        final LifecycleOwner owner = this;

        Food newFood;
 
        foodRepository.getFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable final List<Food> foods) {
                Log.i("nice", foods.get(0).name);
            }
        });



        final FloatingActionButton createButton = (FloatingActionButton) findViewById(R.id.createButton);
        final Intent createIntent = new Intent(this, CreateItemActivity.class);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIntent.putExtra("mode", "create");
                startActivity(createIntent);
            }
        });

    }
}