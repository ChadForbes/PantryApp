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
    private RadioButton fruitbtn, dairybtn, meatbtn, vegetablebtn, carbbtn, nonebtn;
    private EditText searchtext;
    private ShakeListener mShaker;
    private TextView titletv;
    private RadioGroup rg;
    private String searchText;
    ShakeListener shakeListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        titletv = findViewById(R.id.titletv1);
        titletv.setText("My Shopping List");
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        editIntent = new Intent(getBaseContext(), NewItemActivity.class);
        foodList = new ArrayList<>();

        foodRepository = new FoodRepository(getBaseContext());
        initViews();
        generateItems();
        shakeListener = new ShakeListener(this);
        shakeListener.pause();
    }

    public void generateItems() {
        foodList.clear();
        Boolean fruit = fruitbtn.isChecked();
        Boolean meat = meatbtn.isChecked();
        Boolean vegetable = vegetablebtn.isChecked();
        Boolean carb = carbbtn.isChecked();
        Boolean dairy = dairybtn.isChecked();
        Boolean none = nonebtn.isChecked();

        searchText = searchtext.getText().toString() + "%";

        if (!fruit && !dairy && !carb && !vegetable && !meat) {
            foodRepository.getFoodsByName(searchText, true).observe(this, new Observer<List<Food>>() {
                @Override
                public void onChanged(@Nullable final List<Food> foods) {
                    foodList.addAll(foods);

                    recyclerViewSetup();

                    recyclerAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            editIntent.putExtra("mode", "editS");
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
                            editIntent.putExtra("mode", "editS");
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
                    searchText = searchtext.getText().toString() + "%";
                    foodRepository.getFoodsByName(searchText, true).observe(owner, new Observer<List<Food>>() {
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
        intent.putExtra("mode", "createshop");
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backmenu1:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.addItem1:
                startCreateActivity();
                break;
            case R.id.searchBtn1:
                generateItems();
                break;
        }
        switch (rg.getCheckedRadioButtonId()){
            case R.id.carbButton1:
                generateItems();
                break;
            case R.id.dairyButton1:
                generateItems();
                break;
            case R.id.vegeButton1:
                generateItems();
                break;
            case R.id.meatButton1:
                generateItems();
                break;
            case R.id.fruitButton1:
                generateItems();
                break;
            case R.id.nonebtn1:
                generateItems();
                break;
        }
    }

    public void recyclerViewSetup() {
        mrecyclerView = findViewById(R.id.foodlst1);
        mrecyclerView.removeAllViewsInLayout();
        recyclerAdapter = new RecyclerViewAdapter(getBaseContext(), foodList);

        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(recyclerAdapter);
    }

    public void initViews() {
        fruitbtn = findViewById(R.id.fruitButton1);
        dairybtn = findViewById(R.id.dairyButton1);
        meatbtn = findViewById(R.id.meatButton1);
        vegetablebtn = findViewById(R.id.vegeButton1);
        carbbtn = findViewById(R.id.carbButton1);
        searchtext = findViewById(R.id.searchText1);
        rg = findViewById(R.id.rg1);
        nonebtn = findViewById(R.id.nonebtn1);

    }

}