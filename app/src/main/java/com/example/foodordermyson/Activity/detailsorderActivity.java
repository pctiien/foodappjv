package com.example.foodordermyson.Activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.Adapter.Comment;
import com.example.foodordermyson.Adapter.CommentAdapter;
import com.example.foodordermyson.Adapter.ImageFoodDetailAdapter;
import com.example.foodordermyson.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.foodordermyson.Adapter.Cart;
import com.example.foodordermyson.Adapter.FoodOrder;
import com.example.foodordermyson.Common.CurrentUser;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.checkerframework.checker.units.qual.Current;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class detailsorderActivity extends AppCompatActivity {
    //Firebase
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference comment_Database= firebaseDatabase.getReference("comments");
    //
    CardView addtocart_button;
    ImageView back_button;
    RelativeLayout favorite_button;
    TextView add_button;
    TextView remove_button;
    TextView txt_amount;
    TextView fooddetail_price;
    TextView fooddetail_name;
    ViewPager2 imgfood_detail;
    TextView sloganfood_detail;
    //Comment
    ImageButton details_sendComment;
    RoundedImageView detail_avatarComment;
    EditText detail_inputComment;
    LinearLayout details_successComment;
    //Bottom
    TextView soluong ;
    TextView price;
    //Comment
    RecyclerView details_commentRecycler;
    CommentAdapter commentAdapter ;
    List<Comment> commentList = new ArrayList<>();
    //
    FoodOrder food_Order ;
    //Viewpager2
    ViewPager2 viewPager2 ;
    List<String> listImgLink = new ArrayList<>();
    ImageFoodDetailAdapter adapterImgFoodDetail ;
    ImageView vpg_previous;
    ImageView vpg_next;
    TextView currentPos;
    TextView totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsorder);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        anhXa();
        getBundleExtra();
        setButton();
        getComment();
        setInputComment();
        setViewPager2();
    }

    private void setViewPager2() {
        totalItem.setText(adapterImgFoodDetail.getItemCount()+"");
        currentPos.setText(viewPager2.getCurrentItem()+1+"");
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPos.setText(viewPager2.getCurrentItem()+1+"");
                if(position==0){
                    vpg_previous.setVisibility(View.GONE);
                }else{
                    vpg_previous.setVisibility(View.VISIBLE);
                }
                if(position==adapterImgFoodDetail.getItemCount()-1){
                    vpg_next.setVisibility(View.GONE);
                }else{
                    vpg_next.setVisibility(View.VISIBLE);
                }

            }
        });
        vpg_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = viewPager2.getCurrentItem();
                if(currentPosition ==0) {
                    return;
                }
                else {
                    viewPager2.setCurrentItem(currentPosition-1);
                }
            }
        });
        vpg_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = viewPager2.getCurrentItem();
                if(currentPosition == adapterImgFoodDetail.getItemCount()-1) {
                    return;
                }
                else {
                    viewPager2.setCurrentItem(currentPosition+1);
                }
            }
        });
    }

    private void getComment() {
        comment_Database.child(food_Order.getId().trim()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment snapshop_add = snapshot.getValue(Comment.class);
                if(snapshop_add!=null){
                    commentList.add(snapshop_add);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Comment snapshot_remove = snapshot.getValue(Comment.class);
                for(int i=0;i<commentList.size();i++){
                    if(snapshot_remove.getUid_comment().equals(commentList.get(i).getUid_comment())){
                        commentList.remove(i);
                        commentAdapter.notifyItemRemoved(i);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setInputComment() {
        String temp ="";
        detail_inputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                details_sendComment.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().equals("")){
                    details_sendComment.setVisibility(View.GONE);
                }else{
                    details_sendComment.setVisibility(View.VISIBLE);
                }
                details_sendComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment_UID = UUID.randomUUID().toString();
                        Comment comment = new Comment(comment_UID,food_Order.getId(), CurrentUser.currentUser,charSequence.toString().trim());
                        comment_Database.child(food_Order.getId().trim()).child(comment_UID.trim()).setValue(comment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                detail_inputComment.getText().clear();
                                detail_inputComment.clearFocus();
                                details_commentRecycler.scrollToPosition(0);
                                CountDownTimer countDownTimer = new CountDownTimer(10000,1000) {
                                    @Override
                                    public void onTick(long l) {
                                        details_successComment.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onFinish() {
                                        details_successComment.setVisibility(View.GONE);
                                    }
                                }.start();
                            }
                        });
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }



    private void setButton() {
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        Bundle bundle = getIntent().getExtras();
        FoodOrder foodOrder =(FoodOrder) bundle.getSerializable("FoodOrder");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(foodOrder.getDes().isEmpty()==false&&foodOrder.getDes()!=null){
            sloganfood_detail.setText(foodOrder.getDes());
        }
        txt_amount.setText(1+"");
        price.setText(numberFormat.format(Integer.parseInt(foodOrder.getPrice())));
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String costString = foodOrder.getPrice();
                int costInt = Integer.parseInt(costString);
                String amountString = (String) txt_amount.getText();
                int amountInt=Integer.parseInt(amountString);
                amountInt++;
                txt_amount.setText(amountInt+"");
                price.setText(numberFormat.format(amountInt*costInt));
            }
        });
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String costString = foodOrder.getPrice();
                int costInt = Integer.parseInt(costString);
                String amountString = (String) txt_amount.getText();
                int amountInt=Integer.parseInt(amountString);
                if(amountInt==1) txt_amount.setText(1+"");
                else{
                    amountInt--;
                    txt_amount.setText(amountInt+"");
                }
                price.setText(numberFormat.format(amountInt*costInt));
            }
        });
        addtocart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = new Cart();
                cart.setIdfood(foodOrder.getId());
                cart.setPoster(foodOrder.getPoster().get(0));
                cart.setPrice(foodOrder.getPrice());
                cart.setSoluong((String)txt_amount.getText());
                int allPriceInt = Integer.parseInt((String)txt_amount.getText())*Integer.parseInt((String)foodOrder.getPrice());
                String allPriceString = String.valueOf(allPriceInt);
                cart.setAllprice(allPriceString);
                cart.setSize("M");
                cart.setName(foodOrder.getName());
                int soluong =Integer.parseInt((String)txt_amount.getText());
                String count = String.valueOf(soluong);
                int gia = Integer.parseInt(foodOrder.getPrice());
                int tongcong = soluong*gia;
                String tongcongString = String.valueOf(tongcong);
                cart.setAllprice(tongcongString);
                cart.setSoluong(count);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("cart");
                if(cart.getIdfood()!=null){
                    databaseReference.child(CurrentUser.currentUsername).child(cart.getIdfood()).setValue(cart);
                    Toast.makeText(detailsorderActivity.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getBundleExtra() {
        Bundle bundle = getIntent().getExtras();
        FoodOrder foodOrder =(FoodOrder) bundle.getSerializable("FoodOrder");
        food_Order = foodOrder;
        String foodname = foodOrder.getName();
        List<String> poster = foodOrder.getPoster();
        String price = foodOrder.getPrice();
        String votes = foodOrder.getVotes();
        //Glide.with(this).load(poster).into(imgfood_detail);
        if(foodOrder.getPoster()!=null&&!foodOrder.getPoster().isEmpty()){
            adapterImgFoodDetail.replaceList(poster);
        }
        fooddetail_name.setText(foodname);
        fooddetail_price.setText(price);
    }


    private void anhXa() {
        //Viewpager2
        viewPager2 = (ViewPager2)findViewById(R.id.imgfood_detail);
        adapterImgFoodDetail = new ImageFoodDetailAdapter(listImgLink,this);
        viewPager2.setAdapter(adapterImgFoodDetail);
        //
        details_commentRecycler = (RecyclerView)findViewById(R.id.details_commentRecycler);
        commentAdapter = new CommentAdapter(commentList, this, new CommentAdapter.iRespondClick() {
            @Override
            public void deleteComment(Comment comment) {
                if(CurrentUser.currentUser.getUid().equals(comment.getUser().getUid()))
                comment_Database.child(food_Order.getId().trim()).child(comment.getUid_comment().trim()).removeValue();
                else return;
            }
        });
        details_commentRecycler.setAdapter(commentAdapter);
        details_successComment = (LinearLayout) findViewById(R.id.details_successComment);
        details_sendComment = (ImageButton) findViewById(R.id.details_sendComment);
        detail_avatarComment = (RoundedImageView) findViewById(R.id.details_avatarComment);
        Glide.with(this).load(CurrentUser.currentUser.getAvatar().trim()).into(detail_avatarComment);
        detail_inputComment = (EditText) findViewById(R.id.details_inputComment);
        addtocart_button = (CardView) findViewById(R.id.addtocart_button);
        price = (TextView) findViewById(R.id.allprice);
        back_button = (ImageView) findViewById(R.id.back_button);
        add_button = (TextView) findViewById(R.id.add_button);
        remove_button = (TextView) findViewById(R.id.remove_button);
        txt_amount = (TextView) findViewById(R.id.txt_amount);
        fooddetail_name = (TextView) findViewById(R.id.fooddetail_name);
        sloganfood_detail = (TextView) findViewById(R.id.sloganfood_detail);
        imgfood_detail = (ViewPager2) findViewById(R.id.imgfood_detail);
        vpg_previous = (ImageView) findViewById(R.id.detail_previousVPG);
        vpg_next = (ImageView) findViewById(R.id.detail_nextVPG);
        currentPos = (TextView) findViewById(R.id.currentPos);
        totalItem = (TextView) findViewById(R.id.totalItem);
        fooddetail_price = (TextView) findViewById(R.id.fooddetail_price);
    }
}