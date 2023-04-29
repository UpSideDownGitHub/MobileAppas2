package com.example.mobileappas2.admin_ui.order;

public class OrderData {
    private String orderDate;
    private String orderUpdateDate;
    private String orderProducts;
    private String orderTotalPrice;
    private String orderStatus;
    private int orderID;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderUpdateDate() {
        return orderUpdateDate;
    }

    public void setOrderUpdateDate(String orderUpdateDate) {
        this.orderUpdateDate = orderUpdateDate;
    }

    public String getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(String orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderID() {return orderID;}
    public void setOrderID(int orderID) {this.orderID = orderID;}
}
