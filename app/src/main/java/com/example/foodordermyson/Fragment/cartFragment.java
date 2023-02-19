package com.example.foodordermyson.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordermyson.Activity.checkoutActivity;
import com.example.foodordermyson.Adapter.ItemTouchHelperListener;
import com.example.foodordermyson.Adapter.RecyclerViewItemTouchHelper;
import com.example.foodordermyson.Common.CurrentCart;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.checkerframework.checker.units.qual.Current;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cartFragment extends Fragment implements ItemTouchHelperListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cartFragment newInstance(String param1, String param2) {
        cartFragment fragment = new cartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    ImageView back;
    AppCompatButton cartActiviy_payment;
    TextView soluong;
    TextView itemtotal;
    static TextView allprice;
    ScrollView rootView ;
    RelativeLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior ;
    //Recycler
    RecyclerView recyclerView;
    List<Cart> cartList2 = new ArrayList<>();
    static List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cart,container,false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyc_cart);
        cartAdapter = new CartAdapter(getContext(),cartList);
        recyclerView.setAdapter(cartAdapter);
        back = (ImageView) view.findViewById(R.id.back_button);
        rootView = (ScrollView) view.findViewById(R.id.rootView);
        bottomSheet = (RelativeLayout) view.findViewById(R.id.navigation);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        anhXa(view);
        loadCart();
        goToCheckoutActivity();
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        return view;
    }
    private void goToCheckoutActivity() {
        cartActiviy_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), checkoutActivity.class);
                getActivity().startActivity(intent);
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
                    if(cartList.get(i).getIdfood().equals(cart.getIdfood())){
                        isExist = true;
                        cartList.remove(i);
                        cartList.add(cart);
                        cartAdapter.notifyDataSetChanged();
                    }
                }
                if(isExist==false){
                    cartList.add(cart);
                    cartAdapter.notifyDataSetChanged();
                }
                double allprice_now = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    allprice_now = allprice_now + Integer.parseInt(cartList.get(i).getAllprice());
                }
                CurrentCart.ALL_PRICE =allprice_now;
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
                if (cart == null) return;
                if (cartList == null || cartList.isEmpty()) return;
                for (int i = 0; i < cartList.size(); i++) {
                        if (cart.getIdfood() == cartList.get(i).getIdfood()) {
                            cartList.set(i, cart);
                            cartAdapter.notifyItemChanged(i);
                            break;
                        }
                }
                //Set text for TextView (allprice)
                double allprice_now = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    allprice_now = allprice_now + Integer.parseInt(cartList.get(i).getAllprice());
                }
                CurrentCart.ALL_PRICE =allprice_now;
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                allprice.setText(numberFormat.format(allprice_now));
                itemtotal.setText(numberFormat.format(allprice_now));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                //Set text for TextView (amount + allprice)
                double allprice_now = 0;
                for (int i = 0; i < cartList.size(); i++) {
                    allprice_now = allprice_now + Integer.parseInt(cartList.get(i).getAllprice());
                }
                CurrentCart.ALL_PRICE =allprice_now;
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


    private void anhXa(View view) {
        itemtotal = (TextView) view.findViewById(R.id.itemtotal);
        soluong = (TextView) view.findViewById(R.id.soluong);
        allprice = (TextView) view.findViewById(R.id.allprice);
        cartActiviy_payment = (AppCompatButton) view.findViewById(R.id.cartActivity_payment);
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
            snackbar.setActionTextColor(ContextCompat.getColor(getContext(),R.color.orange));
            snackbar.show();
        }
    }
}