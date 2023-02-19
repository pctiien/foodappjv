package com.example.foodordermyson.Model;

import java.util.List;

public class RequestMultiple {
    List<Request> requestList;

    public RequestMultiple(List<Request> requestList) {
        this.requestList = requestList;
    }

    public RequestMultiple() {
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }
}
