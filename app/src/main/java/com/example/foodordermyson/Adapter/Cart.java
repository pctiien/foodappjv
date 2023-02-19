package com.example.foodordermyson.Adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    String idfood;
    String name;
    String poster;
    String size;
    String price;
    String soluong;
    String allprice;



    public String getIdfood() {
        return idfood;
    }

    public void setIdfood(String idfood) {
        this.idfood = idfood;
    }

    public Cart(String id, String name, String poster, String size, String price, String soluong, String allprice) {
        this.idfood = id;
        this.name = name;
        this.poster = poster;
        this.size = size;
        this.price = price;
        this.soluong = soluong;
        this.allprice = allprice;
    }

    public Cart(){
       this.idfood = "null";
       this.name = "null";
       this.poster= "null";
       this.size = "null";
       this.price = "null";
       this.soluong = "null";
       this.allprice = "null";

   }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cart(String name, String poster, String size, String price, String soluong, String allprice) {
        this.name = name;
        this.poster = poster;
        this.size = size;
        this.price = price;
        this.soluong = soluong;
        this.allprice = allprice;
    }

    public Cart(String poster, String size, String price, String soluong, String allprice) {
        this.poster = poster;
        this.size = size;
        this.price = price;
        this.soluong = soluong;
        this.allprice = allprice;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getAllprice() {
        return allprice;
    }

    public void setAllprice(String allprice) {
        this.allprice = allprice;
    }
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("idfood",this.getIdfood());
        result.put("name",this.getName());
        result.put("poster",this.getPoster());
        result.put("size",this.getSize());
        result.put("soluong",this.getSoluong());
        result.put("price",this.getPrice());
        result.put("allprice",this.getAllprice());
        return result;
    }


}
