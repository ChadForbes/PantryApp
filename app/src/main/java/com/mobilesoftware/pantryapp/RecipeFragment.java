package com.mobilesoftware.pantryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RecipeFragment extends Fragment {

    View v;

    public RecipeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recipe, container, false);

        final Button mySearchButton = v.findViewById(R.id.button_search);
        final EditText myEditText = v.findViewById(R.id.editText_search);
        final FoodRepository foodRepository;
        foodRepository = new FoodRepository(getActivity().getApplicationContext());




        mySearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                String toAdd = myEditText.getText().toString();
//                System.out.println(toAdd);


                OkHttpClient client = new OkHttpClient();
                String url = "https://www.food2fork.com/api/search?key=1e57f850de5ad1ed723a26fdb35ca897&q=" + toAdd +"&count=1";
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
                        if(response.isSuccessful()) {
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
}
