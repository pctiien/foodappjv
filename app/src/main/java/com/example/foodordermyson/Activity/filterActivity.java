package com.example.foodordermyson.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodordermyson.Adapter.FilterAdapter;
import com.example.foodordermyson.Adapter.FoodOrder;
import com.example.foodordermyson.Adapter.FoodTag;
import com.example.foodordermyson.R;

import java.util.ArrayList;
import java.util.List;

public class filterActivity extends AppCompatActivity {
    RecyclerView filter_recyc;
    List<FoodOrder> foodOrderList = new ArrayList<>();
    FilterAdapter filterAdapter ;
    TextView filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        anhXa();
        loadFood();
    }

    private void loadFood() {
        FoodTag foodTag = (FoodTag) getIntent().getSerializableExtra("foodTag");
        filter.setText("Here are "+"'"+foodTag.getName()+"'");
        List<FoodOrder> foodOrderList_get = new ArrayList<>();
        foodOrderList_get = (List<FoodOrder>)getIntent().getSerializableExtra("listFoodOrder");
        for(int i=0;i<foodOrderList_get.size();i++){
            if(foodOrderList_get.get(i).getCategory().toLowerCase().trim().equals(foodTag.getName().toLowerCase().trim())){
                foodOrderList.add(foodOrderList_get.get(i));
            }
        }
        filterAdapter = new FilterAdapter(this,foodOrderList);
        filter_recyc.setAdapter(filterAdapter);
    }

    private void anhXa() {
        filter = findViewById(R.id.filter);
        filter_recyc = (RecyclerView) findViewById(R.id.filter_recyc);
    }
}