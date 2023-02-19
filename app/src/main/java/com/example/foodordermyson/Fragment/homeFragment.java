package com.example.foodordermyson.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.Activity.filterActivity;
import com.example.foodordermyson.Activity.searchActivity;
import com.example.foodordermyson.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodordermyson.Adapter.FoodOrder;
import com.example.foodordermyson.Adapter.FoodOrderAdapter;
import com.example.foodordermyson.Adapter.FoodTag;
import com.example.foodordermyson.Adapter.FoodTagAdapter;
import com.example.foodordermyson.Common.CurrentUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
    boolean isExit =false;
    LinearLayout fakesearch_layout;
    androidx.appcompat.widget.SearchView search_mainscreen;
    List<FoodOrder> foodOrderList = new ArrayList<>();
    FoodOrderAdapter foodOrderAdapter;
    RecyclerView recyclerViewFoodOrder;
    ImageView avt ;
    TextView name;
    TextView address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fakesearch_layout = (LinearLayout) view.findViewById(R.id.fakesearchbar);
        avt = view.findViewById(R.id.avt);
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        Glide.with(getContext()).load(CurrentUser.currentUser.getAvatar()).into(avt);
        name.setText(CurrentUser.currentUser.getName());
        anhXaMainScreen(view);
        // anhXaTagline_Recycler();
        gotoSearchActivity();
        return view;
    }

    private void gotoSearchActivity() {
        fakesearch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), searchActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    public void anhXaMainScreen(View view) {
        //Anh xa Recycler Food List
        RecyclerView recyclerViewFoodOrder = (RecyclerView) view.findViewById(R.id.recyc_foodorder);
        FoodOrderAdapter foodOrderAdapter = new FoodOrderAdapter(getContext(), foodOrderList);
        recyclerViewFoodOrder.setAdapter(foodOrderAdapter);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxID =0;
                foodOrderList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                    int currentID = Integer.parseInt(foodOrder.getFoodid().trim());
                    if(currentID>maxID) maxID = currentID;
                    if(foodOrder.getPoster().get(0)==null) foodOrder.getPoster().remove(0);
                    foodOrder.setId(dataSnapshot.child("foodid").getValue().toString());
                    foodOrderList.add(foodOrder);
                }
                Collections.sort(foodOrderList, new Comparator<FoodOrder>() {
                    @Override
                    public int compare(FoodOrder f1, FoodOrder f2) {
                        return f1.getName().compareTo(f2.getName());
                    }
                });
                CurrentUser.currentIDCategory = maxID;
                foodOrderAdapter.notifyDataSetChanged();
                CurrentUser.currentFoodOrder= foodOrderList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //AnhXa Recycler_TagLine + Set onClick(interface) for TagLineItem
        RecyclerView recyclerViewTagLine = (RecyclerView) view.findViewById(R.id.recyc_screen);
        List<FoodTag> foodTagList = new ArrayList<>();
        FoodTagAdapter foodTagAdapter = new FoodTagAdapter(getContext(), foodTagList, new FoodTagAdapter.iItemClickListener() {
            @Override
            public void filterOrder(FoodTag foodTag) {
                String filterTag = foodTag.getName();
                List<FoodOrder> foodOrderList1 = new ArrayList<>();
                for(int i=0;i<CurrentUser.currentFoodOrder.size();i++){
                    if(filterTag.toLowerCase().trim().equals(CurrentUser.currentFoodOrder.get(i).getCategory().toLowerCase().trim())){
                        FoodOrder foodOrder = CurrentUser.currentFoodOrder.get(i);
                        foodOrderList1.add(foodOrder);
                    }
                }
                if(foodOrderList1.isEmpty()==false){
                    foodOrderAdapter.multifilter(foodOrderList1);
                }else{
                    foodOrderAdapter.filter(CurrentUser.currentFoodOrder);
                }
            }

            @Override
            public void unfilterOrder(FoodTag foodTag) {
                String filterTag = foodTag.getName();
                List<FoodOrder> foodOrderList1 = new ArrayList<>();
                for(int i=0;i<CurrentUser.currentFoodOrder.size();i++){
                    if(filterTag.toLowerCase().trim().equals(CurrentUser.currentFoodOrder.get(i).getCategory().toLowerCase().trim())){
                        FoodOrder foodOrder = CurrentUser.currentFoodOrder.get(i);
                        foodOrderList1.add(foodOrder);
                    }
                }
                foodOrderAdapter.unmultifilter(foodOrderList1);
            }
        });
        recyclerViewTagLine.setAdapter(foodTagAdapter);
        foodTagList.add(new FoodTag("Fast food",R.drawable.icon_hotdog));
        foodTagList.add(new FoodTag("Soup",R.drawable.icon_soup));
        foodTagList.add(new FoodTag("Coffee",R.drawable.icons_coffee));
        foodTagList.add(new FoodTag("Juice",R.drawable.icon_juice));
        Collections.sort(foodTagList, new Comparator<FoodTag>() {
            @Override
            public int compare(FoodTag f1, FoodTag f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
        foodTagAdapter.notifyDataSetChanged();
    }
}