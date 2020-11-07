package com.ehabahmed.ecommerce.model;

public class Product {

    private  String pname,description,price,image,category,pid,data,time,productState;

public Product(){}

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public Product(String pName, String description, String price, String image, String category, String productState, String pid, String data, String time) {
        this.pname = pName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.data = data;
        this.time = time;
        this.productState=productState;
    }

    public String getpName() {
        return pname;
    }

    public void setpName(String pName) {
        this.pname = pName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getpId() {
        return pid;
    }

    public void setpId(String pid) {
        this.pid = pid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
