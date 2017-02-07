package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/4.
 */
public class HouseProperty implements Serializable{
    private String id;
    private String community_name;
    private String isdefault;

    public HouseProperty() {
    }

    public HouseProperty(String id, String community_name, String isdefault) {
        this.id = id;
        this.community_name = community_name;
        this.isdefault = isdefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }
}
