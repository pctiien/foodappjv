package com.example.foodordermyson.Activity;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodordermyson.Fragment.cartFragment;
import com.example.foodordermyson.Fragment.homeFragment;
import com.example.foodordermyson.Fragment.profileFragment;
import com.example.foodordermyson.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class mainscreenActivity extends AppCompatActivity {
    boolean isExit =false;
    public static BottomNavigationView bot_nav;
    FloatingActionButton floating_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_bot_nav,new homeFragment()).commit();
        bot_nav = (BottomNavigationView) findViewById(R.id.bot_nav);
        bot_nav.setBackground(null);
        setUpNavigation();
    }

    private void setUpNavigation() {
        bot_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                Fragment frag=null;
                switch (item.getItemId()){
                    case R.id.home:
                        frag = new homeFragment();
                        break;
                    case R.id.cart:
                        frag = new cartFragment();
                        break;
                    case R.id.profile:
                        frag = new profileFragment();
                        break;
                }
                trans.replace(R.id.container_bot_nav,frag).commit();
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(isExit ==true){
            super.onBackPressed();
            isExit = false;

        }else {
            Toast.makeText(this, "Press back one more time to exit", Toast.LENGTH_SHORT).show();
            isExit =true;
        }
    }

}