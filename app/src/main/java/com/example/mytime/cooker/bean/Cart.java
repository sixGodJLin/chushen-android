package com.example.mytime.cooker.bean;

import java.io.Serializable;

public class Cart implements Serializable{
    private int id;
    private int buyNum;
    private User user;
    private Item item;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", buyNum=" + buyNum +
                ", user=" + user +
                ", item=" + item +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        if (id != cart.id) return false;
        if (buyNum != cart.buyNum) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + buyNum;
        return result;
    }
}
