package com.mobilesoftware.pantryapp.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;
import com.mobilesoftware.pantryapp.R;
import com.mobilesoftware.pantryapp.ShakeListener;
import com.mobilesoftware.pantryapp.database.Food;
import com.mobilesoftware.pantryapp.database.FoodRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeActivity extends AppCompatActivity{

    private int numberOfCheckBoxes = 0;
    private ShakeListener mShaker;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final Button mySearchButton = findViewById(R.id.button_search);
        final EditText myEditText = findViewById(R.id.editText_search);
        final FoodRepository foodRepository;
        final List allSearchableFoods = new ArrayList();
        foodRepository = new FoodRepository(getBaseContext());

        mShaker = new ShakeListener(this);
        mShaker.pause();

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
            public void onClick(final View v) {

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
                String url = "https://api.edamam.com/search?q="+toAdd+"&from=0&to=1&app_id=11303608&app_key=667a2f173322f8361bf1fd4a1597bcb3";
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
//                                    System.out.println(myResponse);

                                    try {

                                        // parse json data
                                        JSONObject jsonObj = new JSONObject(myResponse);

                                        JSONArray hits = jsonObj.getJSONArray("hits");

                                        JSONObject h = hits.getJSONObject(0);

                                        JSONObject recipe = h.getJSONObject("recipe");

                                        String label = recipe.getString("label");

                                        JSONArray ingredientLines = recipe.getJSONArray("ingredientLines");



//                                        System.out.println(recipe);
//                                        System.out.println(label);



                                        // create popup
                                        // inflate the layout of the popup window
                                        LayoutInflater inflater = (LayoutInflater)
                                                getSystemService(LAYOUT_INFLATER_SERVICE);
                                        View popupView = inflater.inflate(R.layout.popup_window, null);

                                        // create the popup window
                                        int width = LinearLayout.LayoutParams.MATCH_PARENT;
                                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                        boolean focusable = true; // lets taps outside the popup also dismiss it
                                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                                        // show the popup window
                                        // which view you pass in doesn't matter, it is only used for the window tolken
                                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                                        TextView recipeName = (TextView) popupView.findViewById(R.id.recipeName);
                                        recipeName.setText(label);

                                        TextView ingreds = (TextView) popupView.findViewById(R.id.ingreds);


                                        for(int i = 0; i < ingredientLines.length(); i++){
                                            String item = ingredientLines.getString(i);
                                            System.out.println(item);
                                            ingreds.setText(ingreds.getText() + "\n" + item);
                                        }

//

                                        // dismiss the popup window when touched
                                        popupView.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                popupWindow.dismiss();
                                                return true;
                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

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
