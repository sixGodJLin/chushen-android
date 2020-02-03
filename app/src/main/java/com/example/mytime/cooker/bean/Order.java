package com.example.mytime.cooker.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private int addressId;
    private int userId;
    private Timestamp createTime;
    private double totalPrice;
    private byte isFinished;
    private byte isPayed;
    private byte isDeliver;
    private byte isReceive;
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(int id) {
        this.id = id;
    }

    public Order(int userId, int addressId, double totalPrice, Timestamp createTime, byte isDeliver, byte isPayed, byte isFinished, byte isReceived) {
        this.userId = userId;
        this.addressId = addressId;
        this.totalPrice = totalPrice;
        this.createTime = createTime;
        this.isDeliver = isDeliver;
        this.isPayed = isPayed;
        this.isFinished = isFinished;
        this.isReceive = isReceived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public byte getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(byte isFinished) {
        this.isFinished = isFinished;
    }

    public byte getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(byte isPayed) {
        this.isPayed = isPayed;
    }

    public byte getIsDeliver() {
        return isDeliver;
    }

    public void setIsDeliver(byte isDeliver) {
        this.isDeliver = isDeliver;
    }

    public byte getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(byte isReceive) {
        this.isReceive = isReceive;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", addressId=" + addressId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", totalPrice=" + totalPrice +
                ", isFinished=" + isFinished +
                ", isPayed=" + isPayed +
                ", isDeliver=" + isDeliver +
                ", isReceive=" + isReceive +
                ", orderItems=" + orderItems +
                '}';
    }
}


