package com.mobilesoftware.pantryapp.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesoftware.pantryapp.Food;
import com.mobilesoftware.pantryapp.FoodRepository;
import com.mobilesoftware.pantryapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyPantryFragment extends Fragment {

    View v;
    private RecyclerView mrecyclerView;
    private List<Food> foodList;
    private FoodRepository foodRepository;

    public MyPantryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodList = new ArrayList<>();
        foodRepository = new FoodRepository(getContext());
        foodRepository.getFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable final List<Food> foods) {
                foodList.addAll(foods);
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.fragment_my_pantry, container, false);
            mrecyclerView = (RecyclerView) v.findViewById(R.id.foodlst);
            RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getContext(), foodList);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mrecyclerView.setAdapter(recyclerAdapter);
            return v;
    }
}