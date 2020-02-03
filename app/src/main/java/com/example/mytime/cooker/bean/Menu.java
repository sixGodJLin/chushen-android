package com.example.mytime.cooker.bean;

import java.io.Serializable;

public class Menu implements Serializable {
    private int id;
    private String menuName;
    private String imgUrl;
    private String videoUrl;
    private String mainStuff;
    private String assistStuff;
    private String method;
    private String tips;
    private String score;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getMainStuff() {
        return mainStuff;
    }

    public void setMainStuff(String mainStuff) {
        this.mainStuff = mainStuff;
    }

    public String getAssistStuff() {
        return assistStuff;
    }

    public void setAssistStuff(String assistStuff) {
        this.assistStuff = assistStuff;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", menuName='" + menuName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", mainStuff='" + mainStuff + '\'' +
                ", assistStuff='" + assistStuff + '\'' +
                ", method='" + method + '\'' +
                ", tips='" + tips + '\'' +
                ", score=" + score +
                ", type='" + type + '\'' +
                '}';
    }
}
