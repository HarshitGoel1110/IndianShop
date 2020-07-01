package com.harshit.indianstore;

class ProductsModel {

    //for displaying the products this can be used by shop owners and customers as well
    String image;
    String description;
    String name;
    String price;

    // this is for firebase
    public ProductsModel() {

    }

    public ProductsModel(String description, String name, String price , String image) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
