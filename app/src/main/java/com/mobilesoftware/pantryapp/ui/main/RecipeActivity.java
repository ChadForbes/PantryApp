package com.mobilesoftware.pantryapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.mobilesoftware.pantryapp.R;
import com.mobilesoftware.pantryapp.database.Food;
import com.mobilesoftware.pantryapp.database.FoodRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeActivity extends AppCompatActivity {

    private int numberOfCheckBoxes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final Button mySearchButton = findViewById(R.id.button_search);
        final EditText myEditText = findViewById(R.id.editText_search);
        final FoodRepository foodRepository;
        final List allSearchableFoods = new ArrayList();
        foodRepository = new FoodRepository(getBaseContext());

        final LinearLayout myAddToHere = findViewById(R.id.linAddTo);

        foodRepository.getSearchableFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
//                System.out.println(foods.get(0).name);
//                allSearchableFoods.add(foods.get(0).name);
//                System.out.println(allSearchableFoods);

                System.out.println("Printing all available items");

                myAddToHere.removeAllViews();

                for (int i = 0; i < foods.size(); i++) {
                    System.out.println(foods.get(i).name);
                    addItemToScreen(myAddToHere, foods.get(i).name);


                }
            }
        });

        mySearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String toAdd = myEditText.getText().toString();

                for (int i = 0; i < myAddToHere.getChildCount(); i++) {
                    CheckBox cb = (CheckBox) myAddToHere.getChildAt(i);
                    if (cb.isChecked()) {
                        toAdd += "," + cb.getText();

                    }
                }

                System.out.println("Searching for...");
                System.out.println(toAdd);


                OkHttpClient client = new OkHttpClient();
                String url = "https://www.food2fork.com/api/search?key=1e57f850de5ad1ed723a26fdb35ca897&q=" + toAdd + "&count=1";
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(myResponse);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.backmenu:
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void addItemToScreen(LinearLayout rowAddingTo, String name) {
        CheckBox cb = new CheckBox(this.getBaseContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cb.setLayoutParams(p);
        cb.setText(name);
        cb.setId(numberOfCheckBoxes + 1);
        rowAddingTo.addView(cb);
        numberOfCheckBoxes++;

    }
}
