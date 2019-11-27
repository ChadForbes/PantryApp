package com.mobilesoftware.pantryapp.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilesoftware.pantryapp.Food;
import com.mobilesoftware.pantryapp.FoodRepository;
import com.mobilesoftware.pantryapp.MenuActivity;
import com.mobilesoftware.pantryapp.NewItemActivity;
import com.mobilesoftware.pantryapp.R;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerAdapter;
    Intent editIntent;
    private RecyclerView mrecyclerView;
    private List<Food> foodList;
    private FoodRepository foodRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        editIntent = new Intent(getBaseContext(), NewItemActivity.class);
        foodList = new ArrayList<>();

        foodRepository = new FoodRepository(getBaseContext());
        foodRepository.getFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable final List<Food> foods) {
                foodList.addAll(foods);

                recyclerViewSetup();

                recyclerAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        editIntent.putExtra("mode", "edit");
                        editIntent.putExtra("itemId", foodList.get(position).uid);
                        startActivity(editIntent);
                    }
                });
            }

        });

        final LifecycleOwner owner = this;

        IntentFilter filter = new IntentFilter("com.send.broadcast.OUTGOING BROADCAST");
        BroadcastReceiver listener = new BroadcastReceiver() {
            @Override

            public void onReceive(Context context, Intent intet) {

                if (intet.getExtras().getBoolean("update")) {
                    foodRepository.getFoods().observe(owner, new Observer<List<Food>>() {
                        @Override
                        public void onChanged(@Nullable final List<Food> foods) {
                            foodList = foods;
                            recyclerAdapter.updatelst(foodList);
                            recyclerAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }
        };
        this.registerReceiver(listener, filter);
    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.backmenu:
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.addItem:
                intent = new Intent(this, NewItemActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void recyclerViewSetup() {
        mrecyclerView = (RecyclerView) findViewById(R.id.foodlst);
        recyclerAdapter = new RecyclerViewAdapter(getBaseContext(), foodList);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(recyclerAdapter);
    }

}