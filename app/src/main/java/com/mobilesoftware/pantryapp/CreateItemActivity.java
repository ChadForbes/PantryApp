package com.mobilesoftware.pantryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class CreateItemActivity extends AppCompatActivity {

    private boolean createMode;
    private Food item;
    private View view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int foodId = getIntent().getIntExtra("foodId", 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        final FoodRepository foodRepository = new FoodRepository(getApplicationContext());

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createMode = getIntent().getStringExtra("mode").equals("create");
        if(!createMode) {
            foodRepository.getFood(foodId).observe(this, new Observer<Food>() {
                @Override
                public void onChanged(Food food) {
                    if(food == null) {
                        food = new Food();
                        food.name = "";
                        food.expiryDate = "";
                        food.amount = 0;
                        food.amountType = "";
                        food.aliases = "";
                        food.searchable = false;
                    }
                    EditText nameEdit = findViewById(R.id.nameEdit);
                    EditText expiryEdit = findViewById(R.id.expiryEdit);
                    EditText amountEdit = findViewById(R.id.amountEdit);
                    EditText amountTypeEdit = findViewById(R.id.amountType);
                    EditText aliasesEdit = findViewById(R.id.aliasesText);
                    CheckBox searchButtonToggle = findViewById(R.id.searchButtonToggle);

                    nameEdit.setText(food.name);
                    expiryEdit.setText(food.expiryDate);
                    amountEdit.setText(Float.toString(food.amount));
                    amountTypeEdit.setText(food.amountType);
                    aliasesEdit.setText(food.aliases);
                    searchButtonToggle.setChecked(food.searchable);

                    Button editButton = findViewById(R.id.createButton);
                    editButton.setText(getString(R.string.change_text));

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Food food = new Food();

                            EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
                            EditText expiryEdit =(EditText) findViewById(R.id.expiryEdit);
                            EditText amountEdit = (EditText) findViewById(R.id.amountEdit);
                            EditText amountTypeEdit = (EditText) findViewById(R.id.amountType);
                            EditText aliasesEdit = (EditText) findViewById(R.id.aliasesText);
                            CheckBox searchButtonToggle = (CheckBox) findViewById(R.id.searchButtonToggle);

                            food.name = nameEdit.getText().toString();
                            food.expiryDate = expiryEdit.getText().toString();
                            food.amount = Integer.valueOf(amountEdit.getText().toString());
                            food.amountType = amountTypeEdit.getText().toString();
                            food.aliases = aliasesEdit.getText().toString();
                            food.searchable = searchButtonToggle.isChecked();
                            food.uid = foodId;

                            foodRepository.updateFood(food);
                            finish();
                        }
                    });
                }
            });
        } else {
            Button createButton = findViewById(R.id.createButton);

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Food food = new Food();

                    EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
                    EditText expiryEdit =(EditText) findViewById(R.id.expiryEdit);
                    EditText amountEdit = (EditText) findViewById(R.id.amountEdit);
                    EditText amountTypeEdit = (EditText) findViewById(R.id.amountType);
                    EditText aliasesEdit = (EditText) findViewById(R.id.aliasesText);
                    CheckBox searchButtonToggle = (CheckBox) findViewById(R.id.searchButtonToggle);

                    food.name = nameEdit.getText().toString();
                    food.expiryDate = expiryEdit.getText().toString();
                    food.amount = Integer.valueOf(amountEdit.getText().toString());
                    food.amountType = amountTypeEdit.getText().toString();
                    food.aliases = aliasesEdit.getText().toString();
                    food.searchable = searchButtonToggle.isChecked();

                    foodRepository.insertFood(food);
                    Log.i("Database", "Inserted Food");
                    finish();
                }
            });
        }

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Food food = new Food();
                food.uid = foodId;
                foodRepository.deleteFood(food);
                finish();
            }
        });


    }
}
