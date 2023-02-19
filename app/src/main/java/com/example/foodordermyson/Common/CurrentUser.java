package com.example.foodordermyson.Common;

import com.example.foodordermyson.Adapter.FoodOrder;
import com.example.foodordermyson.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CurrentUser {
    public static String currentUsername;
    public static List<FoodOrder> currentFoodOrder;
    public static User currentUser;
    public static int currentIDCategory;
}
