package com.example.foodordermyson.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodordermyson.Common.CurrentCart;
import com.example.foodordermyson.Common.CurrentUser;
import com.example.foodordermyson.Model.Request;
import com.example.foodordermyson.Model.RequestMultiple;
import com.example.foodordermyson.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class checkoutActivity extends AppCompatActivity {
    ImageView checkout_LeftArrow ;
    AppCompatButton checkout_Proceed ;
    TextView checkout_Total ;
    CheckBox checkout_CheckBoxCard;
    CheckBox checkout_CheckBoxBankAccount;
    CheckBox checkout_CheckBoxDoor;
    CheckBox checkout_CheckBoxPickup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initDraw();
        setClickBack();
        setClickProceed();
        setClickCheckBox();
    }
    private void pushRequestToFirebase(Request request){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("requests");
        String random_uid = UUID.randomUUID().toString();
        request.setRequest_uid(random_uid);
        databaseReference.child(random_uid).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(checkoutActivity.this, "Your purchase was successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(checkoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setClickCheckBox() {
        checkout_CheckBoxDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkout_CheckBoxDoor.isChecked())checkout_CheckBoxPickup.setChecked(false);
            }
        });
        checkout_CheckBoxPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkout_CheckBoxPickup.isChecked())checkout_CheckBoxDoor.setChecked(false);
            }
        });
        checkout_CheckBoxCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkout_CheckBoxCard.isChecked())  checkout_CheckBoxBankAccount.setChecked(false);
            }
        });
        checkout_CheckBoxBankAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkout_CheckBoxBankAccount.isChecked()) checkout_CheckBoxCard.setChecked(false);
            }
        });
    }

    private void showCheckoutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.checkout_dialog);
        Window window = dialog.getWindow();
        if(window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        dialog.show();
        TextView checkout_DialogWarning = (TextView) dialog.findViewById(R.id.checkout_DialogWarning);
        EditText checkout_DialogEdt = (EditText) dialog.findViewById(R.id.checkout_DialogEdt);
        TextView checkout_DialogCancel = (TextView) dialog.findViewById(R.id.checkout_DialogCancel);
        AppCompatButton checkout_DialogProceed = (AppCompatButton) dialog.findViewById(R.id.checkout_DialogProceed);
        checkout_DialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        checkout_DialogProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When info of this account is not full
                //Recommend user go to Profile activity or not
                if(CurrentUser.currentUser.getName().trim().equals("")||CurrentUser.currentUser.getPhonenumber().trim().equals("")
                ||CurrentUser.currentUser.getAddress().trim().equals("")||CurrentUser.currentUser.getName().trim().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(checkoutActivity.this);
                    alertDialog.setMessage("Your profile is not full, please go to fill your profile ?");
                    alertDialog.setPositiveButton("FILL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(checkoutActivity.this,profileActivity.class);
                        checkoutActivity.this.startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("ANOTHER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }else if((checkout_DialogEdt.getText().toString().trim().equals("")||checkout_DialogEdt.getText().toString()==null)){
                    checkout_DialogWarning.setVisibility(View.VISIBLE);
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(checkoutActivity.this);
                    alertDialog.setMessage("Are you sure ?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Request request = new Request();
                            Date date= new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String order_time = simpleDateFormat.format(date).toString();
                            request.setPrice(CurrentCart.ALL_PRICE+"");
                            request.setAddress(String.valueOf(checkout_DialogEdt.getText()));
                            request.setDeliveryMethod(deliveryMethod());
                            request.setPaymentMethod(paymentMethod());
                            request.setPhoneNumber(CurrentUser.currentUser.getPhonenumber());//Doing after
                            request.setFoodRequest(cartActivity.cartList);
                            request.setOrder_time(order_time);
                            pushRequestToFirebase(request);
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        checkout_DialogEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkout_DialogWarning.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setClickProceed() {
        checkout_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!checkout_CheckBoxCard.isChecked()&&!checkout_CheckBoxBankAccount.isChecked()) ||(!checkout_CheckBoxDoor.isChecked()&&!checkout_CheckBoxPickup.isChecked())){
                    Toast.makeText(checkoutActivity.this, "Please choose delivery and payment method", Toast.LENGTH_SHORT).show();
                }else showCheckoutDialog();
            }
        });
    }
    private String paymentMethod(){
        if(checkout_CheckBoxCard.isChecked()) return "Banking";
        return "Pay when receive";
    }
    private String deliveryMethod(){
        if(checkout_CheckBoxPickup.isChecked()) return "Pick up".toString();
        return "Door delivery".toString();
    }

    private void initDraw() {
        checkout_Proceed = (AppCompatButton)findViewById(R.id.checkout_Proceed);
        checkout_LeftArrow = (ImageView) findViewById(R.id.back_button_checkout);
        checkout_Total = (TextView) findViewById(R.id.checkout_Total);
        checkout_Total.setText(formatMoney(CurrentCart.ALL_PRICE));
        checkout_CheckBoxCard = (CheckBox) findViewById(R.id.checkout_CheckBoxCard);
        checkout_CheckBoxBankAccount = (CheckBox) findViewById(R.id.checkout_CheckBoxBankAccount);
        checkout_CheckBoxDoor = (CheckBox) findViewById(R.id.checkout_CheckBoxDoor);
        checkout_CheckBoxPickup = (CheckBox) findViewById(R.id.checkout_CheckBoxPickup);
    }
    private String formatMoney(double money){
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(money);
    }

    private void setClickBack() {
        checkout_LeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}