package com.mobilesoftware.pantryapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.mobilesoftware.pantryapp.R;
import com.mobilesoftware.pantryapp.ShakeListener;
import com.mobilesoftware.pantryapp.database.Food;
import com.mobilesoftware.pantryapp.database.FoodRepository;


public class NewItemActivity extends AppCompatActivity {

    Intent intent;
    int id;
    Intent i;
    private String mode;
    private boolean createMode;
    private TextView titleTV;
    private EditText nameET, expirET, amtET, unitET, aliasET;
    private CheckBox addsearch, shoppingList;

    private RadioButton fruitbtn, dairybtn, meatbtn, vegetablebtn, carbbtn;
    private Button cancelbtn, delbtn, createbtn;
    private ShakeListener mShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        id = getIntent().getExtras().getInt("itemId", 1);

        final FoodRepository foodRepository = new FoodRepository(getApplicationContext());

        initViews();
        mShaker = new ShakeListener(this);
        mShaker.pause();

        i = new Intent();

        delbtn.setEnabled(false);
        delbtn.setVisibility(View.INVISIBLE);
        shoppingList.setEnabled(false);
        shoppingList.setVisibility(View.INVISIBLE);


        cancelbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NewItemActivity.this.finish();
                if (mode.equals("createpantry")) {
                    intent = new Intent(getBaseContext(), PantryActivity.class);
                } else if (mode.equals("createshop")) {
                    intent = new Intent(getBaseContext(), ShoppingListActivity.class);
                }
            }
        });

        // receives message tell the activity if this is an edit or create new
        if (getIntent().getExtras().getString("mode").equals("createpantry")) {
            mode = "createpantry";
        } else if (getIntent().getExtras().getString("mode").equals("editP")) {
            mode = "editP";
        } else if (getIntent().getExtras().getString("mode").equals("createshop")) {
            mode = "createshop";
        }else if (getIntent().getExtras().getString("mode").equals("editS")) {
            mode = "editS";
        }

        if (mode.equals("editP")) {
            createbtn.setText("Save");
            titleTV.setText("Edit Item");
            delbtn.setEnabled(true);
            delbtn.setVisibility(View.VISIBLE);
            shoppingList.setEnabled(true);
            shoppingList.setVisibility(View.VISIBLE);
            foodRepository.getFood(id).observe(this, new Observer<Food>() {
                @Override
                public void onChanged(@NonNull final Food foods) {
                    if (foods != null) {
                        nameET.setText(foods.name);
                        expirET.setText(foods.expiryDate);
                        amtET.setText(Float.toString(foods.amount));
                        unitET.setText(foods.amountType);
                        aliasET.setText(foods.aliases);
                        addsearch.setChecked(foods.searchable);
                        shoppingList.setChecked(foods.shoppingList);
                        fruitbtn.setChecked(foods.fruit);
                        meatbtn.setChecked(foods.meat);
                        vegetablebtn.setChecked(foods.vegetable);
                        carbbtn.setChecked(foods.carb);
                        dairybtn.setChecked(foods.dairy);
                    }
                }
            });

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getBaseContext(), PantryActivity.class);
                    NewItemActivity.this.finish();
                }
            });

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
                    food.shoppingList = shoppingList.isChecked();
                    food.fruit = fruitbtn.isChecked();
                    food.meat = meatbtn.isChecked();
                    food.vegetable = vegetablebtn.isChecked();
                    food.carb = carbbtn.isChecked();
                    food.dairy = dairybtn.isChecked();
                    broadcast(true);
                    foodRepository.updateFood(food);
                    NewItemActivity.this.finish();
                }
            });

            // currently not setup to delete to database
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
                    food.shoppingList = false;
                    food.fruit = fruitbtn.isChecked();
                    food.meat = meatbtn.isChecked();
                    food.vegetable = vegetablebtn.isChecked();
                    food.carb = carbbtn.isChecked();
                    food.dairy = dairybtn.isChecked();
                    broadcast(true);
                    foodRepository.deleteFood(food);
                    NewItemActivity.this.finish();
                }
            });
        }else if(mode.equals("editS")){
                createbtn.setText("Save");
                titleTV.setText("Edit Item");
                delbtn.setEnabled(true);
                delbtn.setVisibility(View.VISIBLE);
                shoppingList.setText("Remove from Shopping List");
                shoppingList.setEnabled(true);
                shoppingList.setVisibility(View.VISIBLE);
                foodRepository.getFood(id).observe(this, new Observer<Food>() {
                    @Override
                    public void onChanged(@NonNull final Food foods) {
                        if (foods != null) {
                            nameET.setText(foods.name);
                            expirET.setText(foods.expiryDate);
                            amtET.setText(Float.toString(foods.amount));
                            unitET.setText(foods.amountType);
                            aliasET.setText(foods.aliases);
                            addsearch.setChecked(foods.searchable);
                            shoppingList.setChecked(false);
                            fruitbtn.setChecked(foods.fruit);
                            meatbtn.setChecked(foods.meat);
                            vegetablebtn.setChecked(foods.vegetable);
                            carbbtn.setChecked(foods.carb);
                            dairybtn.setChecked(foods.dairy);
                        }
                    }
                });

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getBaseContext(), ShoppingListActivity.class);
                    NewItemActivity.this.finish();
                }
            });

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
                        if (food.shoppingList){
                        food.shoppingList = false;
                        }
                        food.fruit = fruitbtn.isChecked();
                        food.meat = meatbtn.isChecked();
                        food.vegetable = vegetablebtn.isChecked();
                        food.carb = carbbtn.isChecked();
                        food.dairy = dairybtn.isChecked();
                        broadcast(true);
                        foodRepository.updateFood(food);
                        NewItemActivity.this.finish();
                    }
                });

                // currently not setup to delete to database
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
                        food.shoppingList = true;
                        food.fruit = fruitbtn.isChecked();
                        food.meat = meatbtn.isChecked();
                        food.vegetable = vegetablebtn.isChecked();
                        food.carb = carbbtn.isChecked();
                        food.dairy = dairybtn.isChecked();
                        broadcast(true);
                        foodRepository.deleteFood(food);
                        NewItemActivity.this.finish();
                    }
                });

            } else if (mode.equals("createpantry")) {
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
                    food.shoppingList = shoppingList.isChecked();
                    food.fruit = fruitbtn.isChecked();
                    food.meat = meatbtn.isChecked();
                    food.vegetable = vegetablebtn.isChecked();
                    food.carb = carbbtn.isChecked();
                    food.dairy = dairybtn.isChecked();
                    broadcast(true);
                    foodRepository.insertFood(food);
                    intent = new Intent(getBaseContext(), PantryActivity.class);
                    NewItemActivity.this.finish();
                }
            });
            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getBaseContext(), PantryActivity.class);
                    NewItemActivity.this.finish();
                }
            });
        } else if (mode.equals("createshop")) {
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
                    food.shoppingList = true;
                    food.fruit = fruitbtn.isChecked();
                    food.meat = meatbtn.isChecked();
                    food.vegetable = vegetablebtn.isChecked();
                    food.carb = carbbtn.isChecked();
                    food.dairy = dairybtn.isChecked();
                    broadcast(true);
                    foodRepository.insertFood(food);
                    intent = new Intent(getBaseContext(), ShoppingListActivity.class);
                    NewItemActivity.this.finish();
                }
            });
            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getBaseContext(), ShoppingListActivity.class);
                    NewItemActivity.this.finish();
                }
            });
        }
    }

    public void initViews() {
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
        fruitbtn = findViewById(R.id.fruitButton);
        dairybtn = findViewById(R.id.dairyButton);
        meatbtn = findViewById(R.id.meatButton);
        vegetablebtn = findViewById(R.id.vegeButton);
        carbbtn = findViewById(R.id.carbButton);
        shoppingList = findViewById(R.id.shoppingListButton);
    }

    public void broadcast(Boolean update) {
        i.putExtra("update", update);
        i.setAction("com.send.broadcast.OUTGOING BROADCAST");
        sendBroadcast(i);
    }
}


