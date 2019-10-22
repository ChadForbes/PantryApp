package com.mobilesoftware.pantryapp.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilesoftware.pantryapp.CreateItemActivity;
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
    RecyclerViewAdapter recyclerAdapter;
    Intent editIntent;

    public MyPantryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editIntent = new Intent(getContext(), CreateItemActivity.class);
        foodList = new ArrayList<>();

        foodRepository = new FoodRepository(getContext());
        foodRepository.getFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable final List<Food> foods) {
                foodList.addAll(foods);

                recyclerViewSetup();

                recyclerAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        editIntent.putExtra("mode", "edit");
                        editIntent.putExtra("itemId", foodList.get(position).uid);
                        startActivity(editIntent);
                    }
                });
            }

        });
        final LifecycleOwner owner = this;

        IntentFilter filter = new IntentFilter("com.send.broadcast.OUTGOING BROADCAST");
        BroadcastReceiver listener = new BroadcastReceiver() {
            @Override

            public void onReceive(Context context, Intent intet) {

                if (intet.getExtras().getBoolean("update")) {
                    foodRepository.getFoods().observe(owner, new Observer<List<Food>>() {
                        @Override
                        public void onChanged(@Nullable final List<Food> foods) {
                           foodList=foods;
                            Log.d("TEST","HERE");
                            recyclerAdapter.updatelst(foodList);
                            recyclerAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }
        };
        getActivity().registerReceiver(listener,filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_pantry, container, false);
        return v;
    }

    public void recyclerViewSetup() {
        mrecyclerView = (RecyclerView) v.findViewById(R.id.foodlst);
        recyclerAdapter = new RecyclerViewAdapter(getContext(), foodList);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrecyclerView.setAdapter(recyclerAdapter);
    }
}