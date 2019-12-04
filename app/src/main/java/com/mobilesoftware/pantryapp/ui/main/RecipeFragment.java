package com.mobilesoftware.pantryapp.ui.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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


public class RecipeFragment extends Fragment {

    View v;
    private int numberOfCheckBoxes = 0;

    public RecipeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recipe, container, false);

        final Button mySearchButton = v.findViewById(R.id.button_search);
        final EditText myEditText = v.findViewById(R.id.editText_search);
        final FoodRepository foodRepository;
        final List allSearchableFoods = new ArrayList();
        foodRepository = new FoodRepository(getActivity());

        final LinearLayout myAddToHere = v.findViewById(R.id.linAddTo);

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

                            getActivity().runOnUiThread(new Runnable() {
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


        return v;
    }

    public void addItemToScreen(LinearLayout rowAddingTo, String name) {
        CheckBox cb = new CheckBox(this.getActivity());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cb.setLayoutParams(p);
        cb.setText(name);
        cb.setId(numberOfCheckBoxes + 1);
        rowAddingTo.addView(cb);
        numberOfCheckBoxes++;

    }


}
