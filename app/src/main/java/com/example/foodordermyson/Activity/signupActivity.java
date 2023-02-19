package com.example.foodordermyson.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.foodordermyson.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodordermyson.Model.User;

public class signupActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference data_user = firebaseDatabase.getReference("user");
    MaterialButton button_signup;
    TextInputLayout username_signup;
    TextInputLayout password_signup;
    TextInputLayout re_repassword_signup;
    TextView toLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        anhXa();
        signupListener();
        goToLogIn();
    }

    private void goToLogIn() {
        toLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupActivity.this.startActivity(new Intent(signupActivity.this,signinActivity.class));
                finish();
            }
        });
    }

    private void signupListener() {
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_signup.getEditText().getText().toString();
                String password = password_signup.getEditText().getText().toString();
                String re_password = re_repassword_signup.getEditText().getText().toString();
                data_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (username.length() < 8 || password.length() < 8) {
                            Toast.makeText(signupActivity.this, "Username and password need more 8 characters", Toast.LENGTH_SHORT).show();
                        } else {
                            if (snapshot.child(username).exists()) {
                                Toast.makeText(signupActivity.this, "This user is already in use. Please try another one", Toast.LENGTH_SHORT).show();
                            } else {
                                if (password.equals(re_password)) {
                                    User user = new User(username,password);
                                    data_user.child(username).setValue(user);
                                    Toast.makeText(signupActivity.this, "User created", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(signupActivity.this, "Password and confirm password does not match", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }

    private void anhXa() {
        toLogIn = (TextView) findViewById(R.id.toLogIn);
        username_signup = (TextInputLayout) findViewById(R.id.username_signup);
        password_signup = (TextInputLayout) findViewById(R.id.password_signup);
        button_signup = (MaterialButton) findViewById(R.id.button_signup);
        re_repassword_signup = (TextInputLayout) findViewById(R.id.re_password_signup);
    }
}