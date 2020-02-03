package com.example.mytime.cooker.bean;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String tel;
    private String nickname;
    private String headImgUrl;
    private Double assets;
    private Boolean enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String paassword) {
        this.password = paassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Double getAssets() {
        return assets;
    }

    public void setAssets(Double assets) {
        this.assets = assets;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

//    public Collection<Address> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(Collection<Address> addresses) {
//        this.addresses = addresses;
//    }
//
//    public Collection<Cart> getCarts() {
//        return carts;
//    }
//
//    public void setCarts(Collection<Cart> carts) {
//        this.carts = carts;
//    }
//
//    public Collection<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(Collection<Order> orders) {
//        this.orders = orders;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tel='" + tel + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", assets=" + assets +
                ", enable=" + enable +
//                ", addresses=" + addresses +
//                ", carts=" + carts +
//                ", orders=" + orders +
                '}';
    }
}
