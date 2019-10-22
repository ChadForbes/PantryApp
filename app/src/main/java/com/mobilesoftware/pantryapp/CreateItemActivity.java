package com.mobilesoftware.pantryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class CreateItemActivity extends AppCompatActivity {

    private boolean createMode;

    private TextView titleTV;
    private EditText nameET, expirET, amtET, unitET, aliasET;
    private CheckBox addsearch;
    private Button cancelbtn, delbtn, createbtn;
    int id;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        final FoodRepository foodRepository = new FoodRepository(getApplicationContext());

        initViews();

        id = getIntent().getExtras().getInt("itemId", 1);

        i = new Intent();

        delbtn.setEnabled(false);
        delbtn.setVisibility(View.INVISIBLE);

        cancelbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CreateItemActivity.this.finish();
            }
        });

        createMode = getIntent().getExtras().getString("mode").equals("create");

        if (!createMode) {
            createbtn.setText("Save");
            titleTV.setText("Edit Item");
            delbtn.setEnabled(true);
            delbtn.setVisibility(View.VISIBLE);
            foodRepository.getFood(id).observe(this, new Observer<Food>() {
                @Override
                public void onChanged(@NonNull final Food foods) {

                    Food food = new Food();
                    food = foods;

                    nameET.setText(food.name);
                    expirET.setText(food.expiryDate);
                    amtET.setText(Float.toString(food.amount));
                    unitET.setText(food.amountType);
                    aliasET.setText(food.aliases);
                    addsearch.setChecked(food.searchable);
                }
            });


            /** currently not setup to saving to database*/
            // Edit item
            createbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Food food = new Food();
                    food.uid = id;
                    food.name = nameET.getText().toString();
                    food.expiryDate = expirET.getText().toString();
                    food.amount = Float.parseFloat(amtET.getText().toString());
                    food.amountType = unitET.getText().toString();
                    food.aliases = aliasET.getText().toString();
                    food.searchable = addsearch.isChecked();

                    broadcast(true);
                    foodRepository.updateFood(food);
                    CreateItemActivity.this.finish();
                }
            });

            /** currently not setup to delete to database*/
            // Delete Item
            delbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Food food = new Food();
                    food.uid = id;
                    food.name = nameET.getText().toString();
                    food.expiryDate = expirET.getText().toString();
                    food.amount = Float.parseFloat(amtET.getText().toString());
                    food.amountType = unitET.getText().toString();
                    food.aliases = aliasET.getText().toString();
                    food.searchable = addsearch.isChecked();
                    foodRepository.deleteFood(food);
                    CreateItemActivity.this.finish();
                }
            });

        } else if (createMode) {
            createbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Food food = new Food();
                    food.name = nameET.getText().toString();
                    food.expiryDate = expirET.getText().toString();
                    food.amount = Float.parseFloat(amtET.getText().toString());
                    food.amountType = unitET.getText().toString();
                    food.aliases = aliasET.getText().toString();
                    food.searchable = addsearch.isChecked();
                    broadcast(true);
                    foodRepository.insertFood(food);
                    CreateItemActivity.this.finish();
                }
            });
        }
    }

    public void initViews(){
        titleTV = findViewById(R.id.titletv);
        nameET = findViewById(R.id.nameet);
        expirET = findViewById(R.id.expiryet);
        amtET = findViewById(R.id.amountet);
        unitET = findViewById(R.id.unitet);
        aliasET = findViewById(R.id.aliaseset);
        addsearch = findViewById(R.id.searchbtntog);
        cancelbtn = findViewById(R.id.cancelbtn);
        delbtn = findViewById(R.id.deletebtn);
        createbtn = findViewById(R.id.submitbtn);
    }

    public void broadcast(Boolean update){
        i.putExtra("update",update);
        i.setAction("com.send.broadcast.OUTGOING BROADCAST");
        sendBroadcast(i);
    }
}
