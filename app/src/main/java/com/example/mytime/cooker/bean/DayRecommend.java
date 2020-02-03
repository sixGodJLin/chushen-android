package com.example.mytime.cooker.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by My time on 2017/10/21
 */

public class DayRecommend implements Serializable {
    private int id;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private Item item;
    private String account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        this.dateTo = dateTo;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "DayRecommend{" +
                "id=" + id +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", item=" + item +
                ", account='" + account + '\'' +
                '}';
    }
}
