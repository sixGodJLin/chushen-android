package com.example.mytime.cooker.bean;

import java.io.Serializable;

/**
 * Created by My time on 2017/11/4
 */

public class ItemComment implements Serializable {
    private int id;
    private String content;
    private ItemComment itemCommentByCommentId;
    private User user;
    private int itemId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ItemComment getItemCommentByCommentId() {
        return itemCommentByCommentId;
    }

    public void setItemCommentByCommentId(ItemComment itemCommentByCommentId) {
        this.itemCommentByCommentId = itemCommentByCommentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemComment that = (ItemComment) o;

        if (id != that.id) return false;
        if (itemId != that.itemId) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (itemCommentByCommentId != null ? !itemCommentByCommentId.equals(that.itemCommentByCommentId) : that.itemCommentByCommentId != null)
            return false;
        return user != null ? user.equals(that.user) : that.user == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (itemCommentByCommentId != null ? itemCommentByCommentId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + itemId;
        return result;
    }

    @Override
    public String toString() {
        return "ItemComment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", itemCommentByCommentId=" + itemCommentByCommentId +
                ", user=" + user +
                ", itemId=" + itemId +
                '}';
    }
}
