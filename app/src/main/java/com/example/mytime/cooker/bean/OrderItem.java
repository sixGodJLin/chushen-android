package com.example.mytime.cooker.bean;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int id;
    private int itemNum;
    private Item item;
    private int orderId;
    private Order order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", itemNum=" + itemNum +
                ", item=" + item +
                ", orderId=" + orderId +
                ", order=" + order +
                '}';
    }
}
