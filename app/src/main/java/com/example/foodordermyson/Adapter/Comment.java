package com.example.foodordermyson.Adapter;

import com.example.foodordermyson.Model.User;

public class Comment {
    String uid_comment;
    String uid_food;
    User user;
    String comment;

    public Comment(String uid_comment,String uid_food, User user, String comment) {
        this.uid_comment = uid_comment;
        this.user = user;
        this.comment = comment;
        this.uid_food = uid_food;
    }

    public String getUid_food() {
        return uid_food;
    }

    public void setUid_food(String uid_food) {
        this.uid_food = uid_food;
    }

    public Comment(String comment, String uid_comment) {
        this.comment = comment;
        this.uid_comment = uid_comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid_comment() {
        return uid_comment;
    }

    public void setUid_comment(String uid_comment) {
        this.uid_comment = uid_comment;
    }
}
