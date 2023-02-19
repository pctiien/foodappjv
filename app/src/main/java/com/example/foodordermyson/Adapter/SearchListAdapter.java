package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordermyson.Activity.detailsorderActivity;
import com.example.foodordermyson.Activity.mainscreenActivity;
import com.example.foodordermyson.R;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    List<FoodOrder> searchLists;
    Context context ;

    public List<FoodOrder> getSearchLists() {
        return searchLists;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void filter(List<FoodOrder> list){
        this.searchLists =list;
        notifyDataSetChanged();
    }
    public void clearfilter(){
        this.searchLists.clear();
        notifyDataSetChanged();
    }

    public SearchListAdapter(List<FoodOrder> searchLists, Context context) {
        this.searchLists = searchLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_listview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodOrder foodOrder= searchLists.get(position);
        if (holder!=null){
            holder.list_text.setText(foodOrder.getName());
            holder.layout_searchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (context, detailsorderActivity.class);
                    intent.putExtra("FoodOrder",foodOrder);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return searchLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView list_text ;
        RelativeLayout layout_searchlist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_text = (TextView) itemView.findViewById(R.id.list_text);
            layout_searchlist = (RelativeLayout) itemView.findViewById(R.id.layout_searchlist);
        }
    }
}
