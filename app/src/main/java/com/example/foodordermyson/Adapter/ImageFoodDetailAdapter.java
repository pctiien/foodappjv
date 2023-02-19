package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.R;

import java.util.List;

public class ImageFoodDetailAdapter extends RecyclerView.Adapter<ImageFoodDetailAdapter.ViewHolder> {
    List<String> list;
    Context context;

    public List<String> getList() {
        return list;
    }

    public ImageFoodDetailAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void replaceList(List<String> listString){
        this.list = listString;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageFoodDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_detailsimg,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFoodDetailAdapter.ViewHolder holder, int position) {
        String imgLink = list.get(position);
        if(imgLink!=null&&!imgLink.trim().equals(" ")){
            Glide.with(context).load(imgLink).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.adapter_detailsimgfood);
        }
    }
}
