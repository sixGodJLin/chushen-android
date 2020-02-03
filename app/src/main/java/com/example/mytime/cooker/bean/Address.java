package com.example.mytime.cooker.bean;


import java.io.Serializable;

public class Address implements Serializable{
    private int id;
    private int byUserid;
    private String receiverName;
    private String addressInfo;
    private int postCode;
    private String tel;
    private boolean isDefault;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getByUserid() {
        return byUserid;
    }

    public void setByUserid(int byUserid) {
        this.byUserid = byUserid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", byUserid=" + byUserid +
                ", receiverName='" + receiverName + '\'' +
                ", addressInfo='" + addressInfo + '\'' +
                ", postCode=" + postCode +
                ", tel='" + tel + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
