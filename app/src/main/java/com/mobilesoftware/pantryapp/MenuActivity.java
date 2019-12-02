package com.mobilesoftware.pantryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mobilesoftware.pantryapp.database.FoodRepository;
import com.mobilesoftware.pantryapp.ui.main.PantryActivity;
import com.mobilesoftware.pantryapp.ui.main.ViewPagerAdapter;


public class MenuActivity extends AppCompatActivity {
    FoodRepository foodRepository;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foodRepository = new FoodRepository(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new MyPantryFragment(), "My Pantry");
        adapter.AddFragment(new RecipeFragment(), "Recipes");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);*/

        /*Food food = new Food();
        food.name = "Beef";
        food.aliases = "";
        food.amount = 16;
        food.amountType = "lb";
        food.searchable = true;
//
       foodRepository.insertFood(food);*/
//
//        Food food2 = new Food();
//        food2.name = "Chicken";
//        food2.aliases = "";
//        food2.amount = 16;
//        food2.amountType = "lb";
//        food2.searchable = true;
//
//        foodRepository.insertFood(food2);
//
//        Food food3 = new Food();
//        food3.name = "Spinach";
//        food3.aliases = "";
//        food3.amount = 16;
//        food3.amountType = "lb";
//        food3.searchable = true;
//
//        foodRepository.insertFood(food3);
//
//        Food food4 = new Food();
//        food4.name = "Honey";
//        food4.aliases = "";
//        food4.amount = 16;
//        food4.amountType = "lb";
//        food4.searchable = true;

//        foodRepository.insertFood(food4);

        /*/foodRepository.getFoods().observe(this, new Observer<List<Food>>() {
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
*/
    }

    public void onClick(View v){
        Intent intent;

        switch (v.getId()){
            case R.id.pantry:
                intent = new Intent(this, PantryActivity.class);
                startActivity(intent);
                break;
            case R.id.recipes:
                intent = new Intent(this, RecipeActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping:

                break;
            case R.id.store:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }
}