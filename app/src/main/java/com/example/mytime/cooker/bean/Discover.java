package com.example.mytime.cooker.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Mr.Lin on 2018/3/10
 */

public class Discover implements Serializable {
    private int id;
    private User user;
    private String content;
    private Timestamp createTime;
    private String imageUrls;
    private String videoUrl;
    private int upvote;
    private int commentNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commnetNum) {
        this.commentNum = commnetNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discover discover = (Discover) o;

        if (id != discover.id) return false;
        if (upvote != discover.upvote) return false;
        if (commentNum != discover.commentNum) return false;
        if (user != null ? !user.equals(discover.user) : discover.user != null) return false;
        if (content != null ? !content.equals(discover.content) : discover.content != null)
            return false;
        if (createTime != null ? !createTime.equals(discover.createTime) : discover.createTime != null)
            return false;
        if (imageUrls != null ? !imageUrls.equals(discover.imageUrls) : discover.imageUrls != null)
            return false;
        return videoUrl != null ? videoUrl.equals(discover.videoUrl) : discover.videoUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (imageUrls != null ? imageUrls.hashCode() : 0);
        result = 31 * result + (videoUrl != null ? videoUrl.hashCode() : 0);
        result = 31 * result + upvote;
        result = 31 * result + commentNum;
        return result;
    }

    @Override
    public String toString() {
        return "Discover{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", imageUrls='" + imageUrls + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", upvote=" + upvote +
                ", commnetNum=" + commentNum +
                '}';
    }
}
