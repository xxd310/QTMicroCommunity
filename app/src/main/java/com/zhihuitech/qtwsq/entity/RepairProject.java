package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class RepairProject implements Serializable{
    private String id;
    private String name;
    private String remark;
    private String sort;
    private String status;
    private String pid;
    private String level;
    private String unit;
    private String price;

    public RepairProject() {
    }

    public RepairProject(String id, String name, String remark, String sort, String status, String pid, String level, String unit, String price) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.sort = sort;
        this.status = status;
        this.pid = pid;
        this.level = level;
        this.unit = unit;
        this.price = price;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "RepairProject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", sort='" + sort + '\'' +
                ", status='" + status + '\'' +
                ", pid='" + pid + '\'' +
                ", level='" + level + '\'' +
                ", unit='" + unit + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
