package com.harshit.indianstore;

class ProductsModel {
    String description;
    String name;
    String price;

    // this is for firebase
    public ProductsModel() {

    }

    public ProductsModel(String description, String name, String price) {
        this.description = description;
        this.name = name;
        this.price = price;
    }



    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
