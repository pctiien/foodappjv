package com.example.foodordermyson.Adapter;

import java.io.Serializable;
import java.util.List;

public class FoodOrder implements Serializable {
    String foodid;
    String name;
    String price;
    String votes;
//    String poster;
    List<String> poster;
    String category;
    String des;
    public  FoodOrder(){};
    public FoodOrder(String id, String name, String price, String votes, List<String> poster,String category,String des) {
        this.foodid = id;
        this.name = name;
        this.price = price;
        this.votes = votes;
        this.poster = poster;
        this.category = category;
        this.des = des;
    }

    public void setPoster(List<String> poster) {
        this.poster = poster;
    }

    public List<String> getPoster() {
        return poster;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return this.foodid;
    }

    public void setId(String id) {
        this.foodid = id;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

//    public String getPoster() {
//        return poster;
//    }
//
//    public void setPoster(String poster) {
//        this.poster = poster;
//    }
}
