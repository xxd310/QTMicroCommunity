package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class CarBrand implements Serializable{
    private String id;
    private String name;
    private String pid;
    private String pinyin;
    private String szm;
    private String url;
    private String img;

    public CarBrand() {
    }

    public CarBrand(String id, String name, String pid, String pinyin, String szm, String url, String img) {
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.pinyin = pinyin;
        this.szm = szm;
        this.url = url;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSzm() {
        return szm;
    }

    public void setSzm(String szm) {
        this.szm = szm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
