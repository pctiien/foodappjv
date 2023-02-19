package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.Common.CurrentUser;
import com.example.foodordermyson.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Comment> list;
    Context context;
    iRespondClick iRespondClick;
    public interface iRespondClick{
        void deleteComment(Comment comment);
    }
    public CommentAdapter(List<Comment> list, Context context,iRespondClick iRespondClick) {
        this.list = list;
        this.context = context;
        this.iRespondClick = iRespondClick;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = list.get(position);
        if(comment!=null){
            if(comment.getUser().getUid().equals(CurrentUser.currentUser.getUid())){
                holder.aComment_delete.setVisibility(View.VISIBLE);
            }else{
                holder.aComment_delete.setVisibility(View.GONE);
            }
            holder.aComment_name.setText(comment.getUser().getName().trim());
            holder.aComment_comment.setText(comment.getComment().trim());
            Glide.with(context).load(comment.getUser().getAvatar()).into(holder.aComment_avatar);
            holder.aComment_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iRespondClick.deleteComment(comment);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView aComment_avatar;
        TextView aComment_name;
        TextView aComment_comment;
        TextView aComment_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            aComment_delete = (TextView) itemView.findViewById(R.id.aComment_delete);
            aComment_avatar = (RoundedImageView) itemView.findViewById(R.id.aComment_avatar);
            aComment_name = (TextView) itemView.findViewById(R.id.aComment_name);
            aComment_comment = (TextView) itemView.findViewById(R.id.aComment_comment);
        }
    }
}
