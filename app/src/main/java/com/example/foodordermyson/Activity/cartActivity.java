package com.example.foodordermyson.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordermyson.Adapter.ItemTouchHelperListener;
import com.example.foodordermyson.Adapter.RecyclerViewItemTouchHelper;
import com.example.foodordermyson.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.foodordermyson.Adapter.Cart;
import com.example.foodordermyson.Adapter.CartAdapter;
import com.example.foodordermyson.Common.CurrentUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class cartActivity extends AppCompatActivity implements ItemTouchHelperListener {
    ImageView back;
    AppCompatButton cartActiviy_payment;
    TextView soluong;
    TextView itemtotal;
    public static TextView allprice;
    ScrollView rootView ;
    RelativeLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior ;
    //Recycler
    RecyclerView recyclerView;
    List<Cart> cartList2 = new ArrayList<>();
    static List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter = new CartAdapter(cartActivity.this, cartList);
    static double allprice_now = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = (RecyclerView) findViewById(R.id.recyc_cart);
        recyclerView.setAdapter(cartAdapter);
        back = (ImageView) findViewById(R.id.back_button);
        rootView = (ScrollView) findViewById(R.id.rootView);
        bottomSheet = (RelativeLayout) findViewById(R.id.navigation);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Toast.makeText(this, "don don", Toast.LENGTH_SHORT).show();
        goBack();
        anhXa();
        loadCart();
        goToCheckoutActivity();
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void goToCheckoutActivity() {
        cartActiviy_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cartActivity.this,checkoutActivity.class);
                cartActivity.this.startActivity(intent);
            }
        });
    }


    private void loadCart() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("cart").child(CurrentUser.currentUsername);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                boolean isExist = false;
                Cart cart = snapshot.getValue(Cart.class);
                if(cart==null) return;
                for(int i=0;i<cartList.size();i++){
                    if(cartList.get(i).getIdfood().equals(cart.getIdfood())==true){
                        isExist = true;
                        cartList.remove(i);
                        cartAdapter.notifyItemRemoved(i);
                        cartList.add(cart);
                        cartAdapter.notifyItemInserted(0);
                    }
                }
                if(cart!=null&&isExist==false){
                    cartList.add(0,cart);
                    cartAdapter.notifyItemInserted(0);
                    //cartAdapter.notifyDataSetChanged();
                }
                allprice_now = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    allprice_now = allprice_now + Integer.parseInt(cartList.get(i).getAllprice());
                }
                //Set text for TextView (amount+allprice)
                soluong.setText(String.valueOf(cartList.size()));
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                allprice.setText(numberFormat.format(allprice_now));
                itemtotal.setText(numberFormat.format(allprice_now));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Cart cart = snapshot.getValue(Cart.class);
                if (cartList == null || cartList.isEmpty()) return;
                for (int i = 0; i < cartList.size(); i++) {
                    if (cart == null) continue;
                    else {
                        if (cart.getIdfood() == cartList.get(i).getIdfood()) {
                            cartList.set(i, cart);
                            break;
                        }
                    }
                }
                cartAdapter.notifyDataSetChanged();
                //Set text for TextView (allprice)
                allprice_now = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    allprice_now = allprice_now + Integer.parseInt(cartList.get(i).getAllprice());
                }
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                allprice.setText(numberFormat.format(allprice_now));
                itemtotal.setText(numberFormat.format(allprice_now));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Cart cart = snapshot.getValue(Cart.class);
                //Set text for TextView (amount + allprice)
                allprice_now = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    allprice_now = allprice_now + Integer.parseInt(cartList.get(i).getAllprice());
                }
                soluong.setText(String.valueOf(cartList.size()));
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                allprice.setText(numberFormat.format(allprice_now));
                itemtotal.setText(numberFormat.format(allprice_now));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void anhXa() {
        itemtotal = (TextView) findViewById(R.id.itemtotal);
        soluong = (TextView) findViewById(R.id.soluong);
        allprice = (TextView) findViewById(R.id.allprice);
        cartActiviy_payment = (AppCompatButton) findViewById(R.id.cartActivity_payment);
    }

    private void goBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof CartAdapter.ViewHolder&&cartAdapter.getCartList().size()!=0){
            final int position = viewHolder.getAdapterPosition();
            String foodname = cartList.get(position).getName();
            Cart cart = cartList.get(position);
            cartAdapter.removeItem(position);
            Snackbar snackbar = Snackbar.make(rootView,foodname + " removed",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.undoItem(cart,position);
                    if(position==0||position==cartList.size()-1){
                        recyclerView.scrollToPosition(position);
                    }
                }
            });
            snackbar.setActionTextColor(ContextCompat.getColor(cartActivity.this,R.color.orange));
            snackbar.show();
        }
    }
}