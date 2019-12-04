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
import android.widget.RadioGroup;
import android.widget.Switch;

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

public class PantryActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerAdapter;
    Intent editIntent;
    private RecyclerView mrecyclerView;
    private List<Food> foodList;
    Intent i;
    private FoodRepository foodRepository;
    private RadioButton fruitbtn, dairybtn, meatbtn, vegetablebtn, carbbtn, nonebtn;
    private EditText searchtext;
    private ShakeListener mShaker;
    private RadioGroup rg;
    private String searchText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        rg = findViewById(R.id.rg);
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
        final LifecycleOwner owner = this;
        foodList.clear();
        Boolean fruit = fruitbtn.isChecked();
        Boolean meat = meatbtn.isChecked();
        Boolean vegetable = vegetablebtn.isChecked();
        Boolean carb = carbbtn.isChecked();
        Boolean dairy = dairybtn.isChecked();
        Boolean none = nonebtn.isChecked();

        searchText = searchtext.getText().toString() + "%";


        if (none && !fruit && !dairy && !carb && !vegetable && !meat) {
            foodRepository.getFoodsByName(searchText, false).observe(this, new Observer<List<Food>>() {
                @Override
                public void onChanged(@Nullable final List<Food> foods) {
                    foodList.clear();
                    foodList.addAll(foods);

                    recyclerViewSetup();

                    recyclerAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            editIntent.putExtra("mode", "editP");
                            editIntent.putExtra("itemId", foodList.get(position).uid);
                            startActivity(editIntent);
                        }
                    });
                }

            });
        } else {
            foodRepository.getFoodsByNameAndType(searchText, fruit, dairy, meat, carb, vegetable, false).observe(this, new Observer<List<Food>>() {
                @Override
                public void onChanged(@Nullable final List<Food> foods) {
                    foodList.clear();
                    foodList.addAll(foods);

                    recyclerViewSetup();

                    recyclerAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            editIntent.putExtra("mode", "editP");
                            editIntent.putExtra("itemId", foodList.get(position).uid);
                            startActivity(editIntent);
                        }
                    });
                }

            });
        }


        IntentFilter filter = new IntentFilter("com.send.broadcast.OUTGOING BROADCAST");
        BroadcastReceiver listener = new BroadcastReceiver() {
            @Override

            public void onReceive(Context context, Intent intet) {

                if (intet.getExtras().getBoolean("update")) {
                    searchText = searchtext.getText().toString() + "%";
                    foodRepository.getFoodsByName(searchText, false).observe(owner, new Observer<List<Food>>() {
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
        intent.putExtra("mode", "createpantry");
        startActivity(intent);
        mShaker.pause();
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
        switch (rg.getCheckedRadioButtonId()){
            case R.id.carbButton:
                generateItems();
                break;
            case R.id.dairyButton:
                generateItems();
                break;
            case R.id.vegeButton:
                generateItems();
                break;
            case R.id.meatButton:
                generateItems();
                break;
            case R.id.fruitButton:
                generateItems();
                break;
            case R.id.nonebtn:
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
        nonebtn = findViewById(R.id.nonebtn);
    }

}