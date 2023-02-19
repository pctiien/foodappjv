package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordermyson.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoodTagAdapter extends RecyclerView.Adapter<FoodTagAdapter.ViewHolder> {
    Context context;
    List<FoodTag> foodTagList ;
    iItemClickListener iItemClickListener;
    public interface iItemClickListener{
        void filterOrder(FoodTag foodTag);
        void unfilterOrder(FoodTag foodTag);
    };
    public FoodTagAdapter(Context context, List<FoodTag> foodTagList,iItemClickListener iItemClickListener) {
        this.context = context;
        this.foodTagList = foodTagList;
        this.iItemClickListener = iItemClickListener;
    }
    public void setAdapter(List<FoodTag> list){
        this.foodTagList =list;
        notifyDataSetChanged();
    }
    public void clearAdapter(){
        this.foodTagList.clear();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_foodtag,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] color = {"green","green_bold","orange","red","blue","grey","purple"};
        Integer[] colorInt = {R.color.green,R.color.green_bold,R.color.teal_200,R.color.purple_200,R.color.blue_sky,R.color.grey,R.color.orange};
        FoodTag foodTag = foodTagList.get(position);
        if(holder!=null){
            holder.img.setImageResource(foodTag.getImgSrc());
            holder.tag_txt.setText(foodTag.getName());
            //Random color background
            if(position<=colorInt.length-1)holder.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context,colorInt[position]));
            else {
                List<Integer> list = Arrays.asList(colorInt);
                Collections.shuffle(list);
                holder.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context,list.get(0)));
            }
            //State click
            holder.tag_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.tag_txt.getCurrentTextColor()==ContextCompat.getColor(context,R.color.white)){
                        holder.tag_txt.setTextColor(ContextCompat.getColor(context,R.color.black));
                        iItemClickListener.filterOrder(foodTag);
                    }else{
                        holder.tag_txt.setTextColor(ContextCompat.getColor(context,R.color.white));
                        iItemClickListener.unfilterOrder(foodTag);
                    }

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return foodTagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_txt;
        ImageView img;
        RelativeLayout tag_layout;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            img = (ImageView) itemView.findViewById(R.id.img);
            tag_layout = (RelativeLayout) itemView.findViewById(R.id.tag_layout);
            tag_txt = (TextView) itemView.findViewById(R.id.tag_txt);
        }
    }
}
