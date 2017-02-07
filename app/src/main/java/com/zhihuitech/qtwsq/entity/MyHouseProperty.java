package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/4.
 */
public class MyHouseProperty implements Serializable{
    private String id;
    private String wecha_id;
    private String address;
    private String community_name;
    private String room_id;
    private String type;
    private String isdefault;
    private String isverify;
    private String isdelete;
    private String createtime;
    private String uid;
    private String realname;
    private String tel;

    public MyHouseProperty() {
    }

    public MyHouseProperty(String id, String wecha_id, String address, String community_name, String room_id, String type, String isdefault, String isverify, String isdelete, String createtime, String uid, String realname, String tel) {
        this.id = id;
        this.wecha_id = wecha_id;
        this.address = address;
        this.community_name = community_name;
        this.room_id = room_id;
        this.type = type;
        this.isdefault = isdefault;
        this.isverify = isverify;
        this.isdelete = isdelete;
        this.createtime = createtime;
        this.uid = uid;
        this.realname = realname;
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWecha_id() {
        return wecha_id;
    }

    public void setWecha_id(String wecha_id) {
        this.wecha_id = wecha_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getIsverify() {
        return isverify;
    }

    public void setIsverify(String isverify) {
        this.isverify = isverify;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "MyHouseProperty{" +
                "id='" + id + '\'' +
                ", wecha_id='" + wecha_id + '\'' +
                ", address='" + address + '\'' +
                ", community_name='" + community_name + '\'' +
                ", room_id='" + room_id + '\'' +
                ", type='" + type + '\'' +
                ", isdefault='" + isdefault + '\'' +
                ", isverify='" + isverify + '\'' +
                ", isdelete='" + isdelete + '\'' +
                ", createtime='" + createtime + '\'' +
                ", uid='" + uid + '\'' +
                ", realname='" + realname + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
