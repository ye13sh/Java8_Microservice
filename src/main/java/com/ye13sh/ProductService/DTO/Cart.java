package com.ye13sh.ProductService.DTO;

public class Cart {
    private String productName;
    private int quantity;
    private double price;
    private String productID;

    public Cart() {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
