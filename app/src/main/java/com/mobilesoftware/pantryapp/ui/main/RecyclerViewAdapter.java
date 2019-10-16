package com.mobilesoftware.pantryapp.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilesoftware.pantryapp.Food;
import com.mobilesoftware.pantryapp.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        Context mContext;
        List<Food> mData;

    public RecyclerViewAdapter(Context mContext, List<Food> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position).name);
        holder.tv_count.setText(Float.toString(mData.get(position).amount));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_count;

        public MyViewHolder(View itemView){
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.nametv);
            tv_count = (TextView) itemView.findViewById(R.id.counttv);


        }
    }
}
