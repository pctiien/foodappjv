package com.example.foodordermyson.Model;

import com.example.foodordermyson.Adapter.Cart;

import java.util.Date;
import java.util.List;

public class Request {
    String request_uid;
    List<Cart> foodRequest;
    String address;
    String phoneNumber;
    String deliveryMethod;
    String paymentMethod;
    String price;
    String order_time;

    public String getRequest_uid() {
        return request_uid;
    }

    public void setRequest_uid(String request_uid) {
        this.request_uid = request_uid;
    }

    public Request(String order_time,String request_uid,List<Cart> foodRequest, String address, String phoneNumber, String deliveryMethod, String paymentMethod, String price) {
        this.foodRequest = foodRequest;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.deliveryMethod = deliveryMethod;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.request_uid= request_uid;
        this.order_time = order_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public Request() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<Cart> getFoodRequest() {
        return foodRequest;
    }

    public void setFoodRequest(List<Cart> foodRequest) {
        this.foodRequest = foodRequest;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
