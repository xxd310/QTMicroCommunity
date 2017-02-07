package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Car implements Serializable{
    private String id;
    private String car_no;
    private String car_type;
    private String owner_id;
    private String token;
    private String wecha_id;
    private String brand;
    private String brand_en;
    private String img1;
    private String img2;
    private String status;
    private String comment;
    private String addtime;
    private String verify_time;
    private String community_id;
    private String uid;
    private String url;

    public Car() {
    }

    public Car(String id, String car_no, String car_type, String owner_id, String token, String wecha_id, String brand, String brand_en, String img1, String img2, String status, String comment, String addtime, String verify_time, String community_id, String uid, String url) {
        this.id = id;
        this.car_no = car_no;
        this.car_type = car_type;
        this.owner_id = owner_id;
        this.token = token;
        this.wecha_id = wecha_id;
        this.brand = brand;
        this.brand_en = brand_en;
        this.img1 = img1;
        this.img2 = img2;
        this.status = status;
        this.comment = comment;
        this.addtime = addtime;
        this.verify_time = verify_time;
        this.community_id = community_id;
        this.uid = uid;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWecha_id() {
        return wecha_id;
    }

    public void setWecha_id(String wecha_id) {
        this.wecha_id = wecha_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand_en() {
        return brand_en;
    }

    public void setBrand_en(String brand_en) {
        this.brand_en = brand_en;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getVerify_time() {
        return verify_time;
    }

    public void setVerify_time(String verify_time) {
        this.verify_time = verify_time;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
