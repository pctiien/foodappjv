package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder>{
    Context context;
    List<FoodOrder> list ;

    public FilterAdapter(Context context, List<FoodOrder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_filter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        FoodOrder foodOrder = list.get(position);
        if(holder!=null){
            Glide.with(context).load(foodOrder.getPoster().get(0)).into(holder.filter_img);
            holder.filter_price.setText(foodOrder.getPrice()+" vnd");
            holder.filter_name.setText(foodOrder.getName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView filter_img ;
        TextView filter_name;
        TextView filter_price;
        TextView filter_rating;
        CardView filter_sub;
        CardView filter_add;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filter_add = itemView.findViewById(R.id.filter_add);
            filter_sub = itemView.findViewById(R.id.filter_sub);
            filter_name = itemView.findViewById(R.id.filter_name);
            filter_img =(RoundedImageView) itemView.findViewById(R.id.filter_img);
            filter_price = itemView.findViewById(R.id.filter_price);
            filter_rating = itemView.findViewById(R.id.filter_rating);
        }
    }
}
