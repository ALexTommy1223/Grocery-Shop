package com.example.dodientu.model;

public class MyOrderModel {
    private String orderId ,date,orderNums, orderPrice,orderProduct,orderCheck;

    public MyOrderModel() {
    }

    public MyOrderModel(String orderId, String date, String orderNums, String orderPrice, String orderProduct, String orderCheck) {
        this.orderId = orderId;
        this.date = date;
        this.orderNums = orderNums;
        this.orderPrice = orderPrice;
        this.orderProduct = orderProduct;
        this.orderCheck = orderCheck;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderNums() {
        return orderNums;
    }

    public void setOrderNums(String orderNums) {
        this.orderNums = orderNums;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(String orderProduct) {
        this.orderProduct = orderProduct;
    }

    public String getOrderCheck() {
        return orderCheck;
    }

    public void setOrderCheck(String orderCheck) {
        this.orderCheck = orderCheck;
    }
}
