package com.harshit.indianstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ProductHistoryModel {
    String shop;
    HashMap<String , ArrayList<Object>> product;
    Boolean delivered;
    String timestamp;

    public ProductHistoryModel(){}

    public ProductHistoryModel(String shop, HashMap<String , ArrayList<Object>> product, Boolean delivered , String timestamp) {
        this.shop = shop;
        this.product = product;
        this.delivered = delivered;
        this.timestamp = timestamp;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public HashMap<String , ArrayList<Object>> getProduct() {
        return product;
    }

    public void setProduct(HashMap<String , ArrayList<Object>> product) {
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
