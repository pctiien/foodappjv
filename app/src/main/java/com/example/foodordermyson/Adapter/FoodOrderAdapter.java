package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.R;
import com.example.foodordermyson.Common.CurrentUser;
import com.example.foodordermyson.Activity.detailsorderActivity;
import com.example.foodordermyson.Common.CurrentUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.ViewHolder> {
    Context context;
    List<FoodOrder> foodOrderList ;

    public void filter(List<FoodOrder> list){
        this.foodOrderList = list;
        notifyDataSetChanged();
    }
    public void multifilter(List<FoodOrder> list){
        int countItem_now = this.foodOrderList.size();
        int countItem_full = CurrentUser.currentFoodOrder.size();
        if(countItem_now < countItem_full){
            for(int i=0;i<list.size();i++){
                this.foodOrderList.add(list.get(i));
            }
            Collections.sort(this.foodOrderList, new Comparator<FoodOrder>() {
                @Override
                public int compare(FoodOrder f1, FoodOrder f2) {
                    return f1.getName().compareTo(f2.getName());
                }
            });
        }else{
            this.foodOrderList = list;
        }
        notifyDataSetChanged();
    }
    public void unmultifilter(List<FoodOrder> list){
        for(int i=0;i<list.size();i++){
            this.foodOrderList.remove(list.get(i));
        }
        if(this.foodOrderList.size()==0){
            this.foodOrderList = CurrentUser.currentFoodOrder;
        }
        Collections.sort(this.foodOrderList, new Comparator<FoodOrder>() {
            @Override
            public int compare(FoodOrder f1, FoodOrder f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
        notifyDataSetChanged();
    }
    public FoodOrderAdapter(Context context, List<FoodOrder> foodOrderList) {
        this.context = context;
        this.foodOrderList = foodOrderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodorder_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodOrder foodOrder = foodOrderList.get(position);
        if(holder!=null){
            Glide.with(context).load(foodOrder.getPoster().get(0)).into(holder.img);
            holder.name.setText(foodOrder.getName());
            holder.price.setText(foodOrder.getPrice());
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detailsorderActivity.class);
                intent.putExtra("FoodOrder",foodOrder);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout ;
        ImageView img;
        TextView name;
        TextView price;
        TextView votes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
            img = (ImageView) itemView.findViewById(R.id.img_food);
            name = (TextView) itemView.findViewById(R.id.foodname);
            price = (TextView) itemView.findViewById(R.id.pricefood);
        }
    }
}
