package com.example.mytime.cooker.utils;

import com.example.mytime.cooker.bean.Address;
import com.example.mytime.cooker.bean.Cart;
import com.example.mytime.cooker.bean.Item;
import com.example.mytime.cooker.bean.ItemComment;
import com.example.mytime.cooker.bean.Menu;
import com.example.mytime.cooker.bean.User;

import java.util.List;

/**
 * Created by My time on 2017/10/28
 */

public class BusUtilBean {
    private User user;
    private Item item;
    private Menu menu;
    private Address address;
    private ItemComment comment;
    private Integer[] ids;
    private int type;
    private double price;
    private String buyNum;
    private List<Cart> carts;


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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public ItemComment getComment() {
        return comment;
    }

    public void setComment(ItemComment comment) {
        this.comment = comment;
    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    private CallBack setPriceCallBack;
    public void registerSetPriceCallBack(CallBack setPriceCallBack){
        this.setPriceCallBack = setPriceCallBack;
    }
    public void setPrice(double price) {
        this.price = price;
        if (setPriceCallBack != null){
            setPriceCallBack.call();
        }
    }
}
