package com.example.foodordermyson.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.R;
import com.example.foodordermyson.Common.CurrentUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<Cart> cartList;
    public interface iItemClickListener{
        void addClick(Cart cart);
        void subtractClick(Cart cart);
        void deleteClick(Cart cart);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }
    public void removeItem(int position){
        Cart cart = this.cartList.get(position);
        this.cartList.remove(position);
        notifyItemRemoved(position);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("cart");
        databaseReference.child(CurrentUser.currentUsername).child(cart.getIdfood()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
    }
    public void undoItem(Cart cart,int position){
        this.cartList.add(position,cart);
        notifyItemInserted(position);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("cart");
        databaseReference.child(CurrentUser.currentUsername).child(cart.getIdfood()).setValue(cart);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        Cart cart = cartList.get(position);
        boolean[] checkArr = new boolean[cartList.size()];
        if(holder!=null&&cart!=null){
            String poster = cart.getPoster();
            String price = cart.getPrice();
            String soluong = cart.getSoluong();
            String allprice = cart.getAllprice();
            Glide.with(context).load(poster).into(holder.imgfood_cart);
            holder.pricefood_cart.setText(price);
            holder.count.setText(soluong);
            holder.foodname_cart.setText(cart.getName());
            holder.pricefood_cart.setText(allprice);
            holder.plus_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String countString = (String) holder.count.getText();
                    int countInt = Integer.parseInt(countString)+1;
                    holder.count.setText(countInt+"");
                    String newAllPrice = String.valueOf(Integer.parseInt(cart.getPrice())*countInt);
                    holder.pricefood_cart.setText(newAllPrice);
                    cart.setAllprice(newAllPrice);
                    cart.setSoluong(String.valueOf(countInt));
                    pushToFirebase(cart);
                }
            });
            holder.subtract_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String countString =(String) holder.count.getText();
                    int countInt = Integer.parseInt(countString);
                    if(countInt>1){
                        countInt--;
                        holder.count.setText(countInt+"");
                        String newAllPrice = String.valueOf(Integer.parseInt(cart.getPrice())*countInt);
                        holder.pricefood_cart.setText(newAllPrice);
                        cart.setAllprice(newAllPrice);
                        cart.setSoluong(String.valueOf(countInt));
                        pushToFirebase(cart);
                    }
                }
            });
        }
    }
    private void pushToFirebase(Cart cart) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("cart");
        databaseReference.child(CurrentUser.currentUsername).child(cart.getIdfood()).updateChildren(cart.toMap());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cartlayout ;
        ImageView imgfood_cart;
        TextView count;
        TextView pricefood_cart;
        CardView plus_button;
        CardView subtract_button;
        TextView foodname_cart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartlayout = (LinearLayout) itemView.findViewById(R.id.cartlayout);
            plus_button = (CardView) itemView.findViewById(R.id.plus_button);
            subtract_button = (CardView) itemView.findViewById(R.id.subtract_button);
            foodname_cart = (TextView) itemView.findViewById(R.id.foodname_cart);
            imgfood_cart = (ImageView) itemView.findViewById(R.id.imgfood_cart);
            count = (TextView) itemView.findViewById(R.id.count);
            pricefood_cart = (TextView) itemView.findViewById(R.id.pricefood_cart);
        }
    }
}
