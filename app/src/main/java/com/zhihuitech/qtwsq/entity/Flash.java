package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

import static com.zhihuitech.qtwsq.activity.R.string.tip;

/**
 * Created by Administrator on 2016/8/27.
 */
public class Flash implements Serializable {
    private String id;
    private String token;
    private String img;
    private String url;
    private String info;
    private String tip;
    private String did;
    private String fid;
    private String sort;

    public Flash() {
    }

    public Flash(String id, String token, String img, String url, String info, String tip, String did, String fid, String sort) {
        this.id = id;
        this.token = token;
        this.img = img;
        this.url = url;
        this.info = info;
        this.tip = tip;
        this.did = did;
        this.fid = fid;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Flash{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", img='" + img + '\'' +
                ", url='" + url + '\'' +
                ", info='" + info + '\'' +
                ", tip='" + tip + '\'' +
                ", did='" + did + '\'' +
                ", fid='" + fid + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
