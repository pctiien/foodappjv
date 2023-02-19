package com.example.foodordermyson.Activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodordermyson.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodordermyson.Adapter.FoodOrder;
import com.example.foodordermyson.Adapter.SearchList;
import com.example.foodordermyson.Adapter.SearchListAdapter;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {
    Boolean flag = true;
    AppCompatEditText edt_search;
    ImageView left_arrow;
    TextView loadmore ;
    RecyclerView recyclersearch;
    List<FoodOrder> searchLists;
    SearchListAdapter searchListAdapter;
    List<FoodOrder> searchLists_temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        anhXaDraw();
        loadmore.setVisibility(View.GONE);
        anhXaRecyclerView();
        edt_search.requestFocus();
        filterSearchView();
        setClickButtonBack();
        setClickTextViewLoadMore();
    }

    private void setClickTextViewLoadMore() {
        int MIN_SIZE = 5;
        String showlessString = getResources().getString(getResources().getIdentifier("showless", "string", getPackageName()));
        String loadmoreString = getResources().getString(getResources().getIdentifier("loadmore", "string", getPackageName()));
        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loadmore.getText().equals(loadmoreString)) {
                    searchListAdapter.filter(searchLists_temp);
                    loadmore.setText(showlessString);
                }else if(searchListAdapter.getItemCount()>MIN_SIZE){
                    List<FoodOrder> searchList2  = new ArrayList<>();
                    for(int i=0;i<MIN_SIZE;i++){
                        searchList2.add(searchListAdapter.getSearchLists().get(i));
                    }
                    searchListAdapter.filter(searchList2);
                    loadmore.setText(loadmoreString);
                }
            }
        });

    }


    private void filterSearchView() {
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String textChange = String.valueOf(edt_search.getText());
                String txtChanged = String.valueOf(charSequence);
                List<FoodOrder> searchLists1 = new ArrayList<>();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("category");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                            if (foodOrder.getName().toLowerCase().contains(txtChanged.toLowerCase())&&txtChanged.isEmpty()==false&&txtChanged.equals(" ")==false) {
                                if(foodOrder.getPoster().get(0)==null||foodOrder.getPoster().get(0).trim().equals("")){
                                    foodOrder.getPoster().remove(0);
                                }
                                searchLists1.add(foodOrder);
                            }
                        }
                        if (searchLists1.isEmpty() == false) {
                            searchLists_temp = searchLists1;
                            searchListAdapter.filter(searchLists1);
                        }else{
                            searchListAdapter.clearfilter();
                        }
                        //Set TextView Loadmore|Showless when TEXT_Changed
                        int MIN_SIZE =5;
                        if(searchListAdapter.getItemCount()<=MIN_SIZE){
                            loadmore.setVisibility(View.GONE);
                        }else {
                            loadmore.setVisibility(View.VISIBLE);
                            String loadmoreString = getResources().getString(getResources().getIdentifier("loadmore", "string", getPackageName()));
                            List<FoodOrder> searchList2  = new ArrayList<>();
                            for(int i=0;i<MIN_SIZE;i++){
                                searchList2.add(searchListAdapter.getSearchLists().get(i));
                            }
                            searchListAdapter.filter(searchList2);
                            loadmore.setText(loadmoreString);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void anhXaRecyclerView() {
        //AnhXa + Add Item_divider
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclersearch = (RecyclerView) findViewById(R.id.recycler_search);
        recyclersearch.addItemDecoration(itemDecoration);
        searchLists = new ArrayList<>();
        searchListAdapter = new SearchListAdapter(searchLists, searchActivity.this);
        recyclersearch.setAdapter(searchListAdapter);
    }

    private void setClickButtonBack() {
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void anhXaDraw() {
        loadmore = (TextView) findViewById(R.id.loadmore);
        edt_search = (AppCompatEditText) findViewById(R.id.edt_search);
        left_arrow = (ImageView) findViewById(R.id.back_button);
    }

}