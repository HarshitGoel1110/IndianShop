package com.harshit.indianstore;

class AllShopsModel {

    String name;
    String city;
    String email;
    String mobile;
    String pincode;
    String address;

    public AllShopsModel() {

    }

    public AllShopsModel(String name, String city, String email, String mobile, String pincode, String address) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.mobile = mobile;
        this.pincode = pincode;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
