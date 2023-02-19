package com.example.foodordermyson.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.foodordermyson.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodordermyson.Common.CurrentUser;
import com.example.foodordermyson.Model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.Current;
import org.w3c.dom.Text;

public class signinActivity extends AppCompatActivity {
    ProgressBar progressBar ;
    TextInputLayout username_signin;
    TextInputLayout password_signin;
    AppCompatButton button_signin;
    TextView toSignUp;
    TextView warning_txt;
    LinearLayout warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        anhXa();
        signinListener();
        goToSignUp();
    }

    private void goToSignUp() {
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinActivity.this.startActivity(new Intent(signinActivity.this,signupActivity.class));
                finish();
            }
        });
    }

    private void signinListener() {
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference data_user = FirebaseDatabase.getInstance().getReference("user");
                FirebaseApp.initializeApp(signinActivity.this);
                String username = username_signin.getEditText().getText().toString();
                String password = password_signin.getEditText().getText().toString();
                //Add on textChange for Edittext username
                username_signin.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        progressBar.setVisibility(View.GONE);
                        warning.setVisibility(View.GONE);
                        username_signin.setHint("Phone number, username or email");
                        username_signin.getEditText().setHintTextColor(ContextCompat.getColor(signinActivity.this,R.color.grey));

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                password_signin.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        warning.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        password_signin.setHint("Password");
                        password_signin.getEditText().setHintTextColor(ContextCompat.getColor(signinActivity.this,R.color.grey));
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                data_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(profileActivity.isSignin == true) return ;
                        if(username.isEmpty()||password.isEmpty()){
                            warning.setVisibility(View.VISIBLE);
                            if(username.isEmpty()||snapshot.child(username.trim()).exists()==false){
                                progressBar.setVisibility(View.GONE);
                                if(username.isEmpty())warning_txt.setText(getString(R.string.empty_input));
                                else
                                    warning_txt.setText(getString(R.string.wrong_username));
                                //left , top , right , bot - position of drawable
                            }
                            if(password.isEmpty()){
                                progressBar.setVisibility(View.GONE);
                                //left , top , right , bot - position of drawable
                                warning_txt.setText(getString(R.string.empty_input));
                            }
                        }else if(snapshot.child(username.trim()).exists()){
                            User user = snapshot.child(username).getValue(User.class);
                            progressBar.setVisibility(View.GONE);
                            if(user.getPassword().equals(password)){
                                CurrentUser.currentUsername=username;
                                CurrentUser.currentUser = user;
                                Intent intent = new Intent(signinActivity.this,mainscreenActivity.class);
                                startActivity(intent);
                            }else{
                                warning.setVisibility(View.VISIBLE);
                                warning_txt.setText(getString(R.string.wrong_password));
                            }
                        }else{
                            warning.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            warning_txt.setText(getString(R.string.wrong_username));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(signinActivity.this, "Have a error in connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void anhXa() {
        warning_txt = (TextView) findViewById(R.id.warning_txt);
        warning= (LinearLayout) findViewById(R.id.warning);
        toSignUp = (TextView) findViewById(R.id.toSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressbar) ;
        username_signin = (TextInputLayout) findViewById(R.id.username_signin);
        password_signin = (TextInputLayout) findViewById(R.id.password_signin);
        button_signin = (AppCompatButton) findViewById(R.id.button_signin);
    }
}