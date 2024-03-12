package com.ye13sh.OrderService.Model;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "productName")
    private String productName;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "totalPrice")
    private double totalPrice;
    @Column(name = "productID")
    private String productID;
    @Column(name = "orderID")
    private String orderID;
    @Column(name = "transactionID")
    private String transactionID;
    @Column(name = "dateOfPurchase")
    private LocalDateTime dateOfPurchase;
    @Column(name = "orderStatus")
    private String orderStatus;
    @Column(name = "orderConfirm")
    private Boolean orderConfirm;

    public Order() {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.productID = productID;
        this.orderID = orderID;
        this.transactionID = transactionID;
        this.orderConfirm = orderConfirm;
        this.dateOfPurchase= dateOfPurchase;
        this.orderStatus = orderStatus;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Boolean getOrderConfirm() {
        return orderConfirm;
    }

    public void setOrderConfirm(Boolean orderConfirm) {
        this.orderConfirm = orderConfirm;
    }

    public LocalDateTime getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDateTime dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
