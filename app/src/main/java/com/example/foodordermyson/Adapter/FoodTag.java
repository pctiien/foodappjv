package com.example.foodordermyson.Adapter;

import java.io.Serializable;

public class FoodTag implements Serializable {
    String name;
    int ImgSrc;

    public int getImgSrc() {
        return ImgSrc;
    }

    public void setImgSrc(int imgSrc) {
        ImgSrc = imgSrc;
    }

    public FoodTag(String name, int imgSrc) {
        this.name = name;
        ImgSrc = imgSrc;
    }

    public FoodTag(String name) {
        this.name = name;
    }

    public FoodTag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
