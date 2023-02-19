package com.example.foodordermyson.Model;

import java.util.HashMap;
import java.util.Map;

public class User {
    String uid;
    String name;
    String password;
    String age ;
    String address;
    String role;
    String phonenumber;
    String email;
    String avatar ;

    public User(String name, String password, String age, String address, String role, String phonenumber, String email,String avatar) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.address = address;
        this.role = role;
        this.phonenumber = phonenumber;
        this.email = email;
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }
    public User(String uid,String password) {
        this.uid = uid;
        this.password = password;
        this.role = "user";
        this.age ="";
        this.address ="";
        this.name="";
        this.email="";
        this.phonenumber="";
        this.avatar = "";
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("uid",this.getUid());
        result.put("name",this.getName());
        result.put("password",this.getPassword());
        result.put("age",this.getAge());
        result.put("address",this.getAddress());
        result.put("role",this.getRole());
        result.put("phonenumber",this.getPhonenumber());
        result.put("email",this.getEmail());
        return result;
    }
}
