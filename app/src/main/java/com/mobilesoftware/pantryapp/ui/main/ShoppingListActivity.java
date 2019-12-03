package com.mobilesoftware.pantryapp.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilesoftware.pantryapp.R;
import com.mobilesoftware.pantryapp.ShakeListener;
import com.mobilesoftware.pantryapp.database.Food;
import com.mobilesoftware.pantryapp.database.FoodRepository;
import com.mobilesoftware.pantryapp.ui.main.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerAdapter;
    Intent editIntent;
    private RecyclerView mrecyclerView;
    private List<Food> foodList;
    private FoodRepository foodRepository;
    private RadioButton fruitbtn, dairybtn, meatbtn, vegetablebtn, carbbtn;
    private EditText searchtext;
    private ShakeListener mShaker;
    private TextView titletv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        titletv = findViewById(R.id.titletv);
        titletv.setText("My Shopping List");
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(100);
                startCreateActivity();
            }
        });

        editIntent = new Intent(getBaseContext(), NewItemActivity.class);
        foodList = new ArrayList<>();

        foodRepository = new FoodRepository(getBaseContext());
        initViews();
        generateItems();
    }

    public void generateItems() {
        foodList.clear();
        Boolean fruit = fruitbtn.isChecked();
        Boolean meat = meatbtn.isChecked();
        Boolean vegetable = vegetablebtn.isChecked();
        Boolean carb = carbbtn.isChecked();
        Boolean dairy = dairybtn.isChecked();

        String searchText = searchtext.getText().toString() + "%";

        if (!fruit && !dairy && !carb && !vegetable && !meat) {
            foodRepository.getFoodsByName(searchText, true).observe(this, new Observer<List<Food>>() {
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
        } else {
            foodRepository.getFoodsByNameAndType(searchText, fruit, dairy, meat, carb, vegetable, true).observe(this, new Observer<List<Food>>() {
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
        }

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
                            recyclerAdapter.clear();
                            recyclerAdapter.updatelst(foodList);
                            recyclerAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }
        };
        this.registerReceiver(listener, filter);
    }

    public void startCreateActivity() {
        Intent intent = new Intent(this, NewItemActivity.class);
        intent.putExtra("mode", "create");
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backmenu:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.addItem:
                startCreateActivity();
                break;
            case R.id.searchBtn:
                generateItems();
                break;
        }
    }

    public void recyclerViewSetup() {
        mrecyclerView = findViewById(R.id.foodlst);
        mrecyclerView.removeAllViewsInLayout();
        recyclerAdapter = new RecyclerViewAdapter(getBaseContext(), foodList);

        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(recyclerAdapter);
    }

    public void initViews() {
        fruitbtn = findViewById(R.id.fruitButton);
        dairybtn = findViewById(R.id.dairyButton);
        meatbtn = findViewById(R.id.meatButton);
        vegetablebtn = findViewById(R.id.vegeButton);
        carbbtn = findViewById(R.id.carbButton);
        searchtext = findViewById(R.id.searchText);
    }

}