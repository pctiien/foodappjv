package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordermyson.R;

import java.util.List;

public class MoreInfoAdapter extends RecyclerView.Adapter<MoreInfoAdapter.ViewHolder> {
    List<MoreInfo> moreInfoList ;
    Context context;
    iItemClickListener iItemClickListener;
    public interface iItemClickListener{
        void goToCartFragment();
    }

    public MoreInfoAdapter(List<MoreInfo> moreInfoList, Context context,iItemClickListener iItemClickListener) {
        this.moreInfoList = moreInfoList;
        this.context = context;
        this.iItemClickListener = iItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_profile,parent,false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull MoreInfoAdapter.ViewHolder holder, int position) {
        MoreInfo moreInfo = moreInfoList.get(position);
        if(holder!=null){
            holder.textView.setText(moreInfo.getInfo());
        }

        holder.layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     switch (holder.getAdapterPosition()){
                         case 0:
                         {
                             iItemClickListener.goToCartFragment();
                             break;
                         }
                         default: break;
                     }
                     return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView ;
        LinearLayoutCompat layoutCompat ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.infoName);
            layoutCompat = (LinearLayoutCompat) itemView.findViewById(R.id.adapter_ProfileLayout);
        }
    }
}
