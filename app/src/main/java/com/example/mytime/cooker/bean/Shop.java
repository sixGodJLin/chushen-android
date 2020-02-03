package com.example.mytime.cooker.bean;

import java.io.Serializable;
import java.util.Collection;

public class Shop implements Serializable {
    private int id;
    private String name;
//    private Collection<Item> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        if (id != shop.id) return false;
        if (name != null ? !name.equals(shop.name) : shop.name != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", items=" + items +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

//    public Collection<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(Collection<Item> items) {
//        this.items = items;
//    }
}
