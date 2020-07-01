package com.harshit.indianstore;

import java.util.ArrayList;
import java.util.HashMap;

class ShopHistoryModel {

    String user;
    HashMap<String , ArrayList<Object>> product;
    Boolean delivered;
    String timestamp;

    public ShopHistoryModel(){}

    public ShopHistoryModel(String user, HashMap<String, ArrayList<Object>> product, Boolean delivered, String timestamp) {
        this.user = user;
        this.product = product;
        this.delivered = delivered;
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public HashMap<String, ArrayList<Object>> getProduct() {
        return product;
    }

    public void setProduct(HashMap<String, ArrayList<Object>> product) {
        this.product = product;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
