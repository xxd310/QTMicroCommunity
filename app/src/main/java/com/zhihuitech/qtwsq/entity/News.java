package com.zhihuitech.qtwsq.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/4.
 */
public class News implements Serializable{
    private String id;
    private String uid;
    private String uname;
    private String keyword;
    private String precisions;
    private String text;
    private String community_id;
    private String community_name;
    private String pic;
    private String showPic;
    private String info;
    private String url;
    private String createtime;
    private String updatetime;
    private String click;
    private String token;
    private String title;
    private String usort;
    private String longitude;
    private String latitude;
    private String type;
    private String writer;
    private String texttype;
    private String usorts;
    private String is_focus;
    private String pinglun;
    private String praise;
    private String recommend;
    private String showtime;

    public News() {
    }

    public News(String id, String uid, String uname, String keyword, String precisions, String text, String community_id, String community_name, String pic, String showPic, String info, String url, String createtime, String updatetime, String click, String token, String title, String usort, String longitude, String latitude, String type, String writer, String texttype, String usorts, String is_focus, String pinglun, String praise, String recommend, String showtime) {
        this.id = id;
        this.uid = uid;
        this.uname = uname;
        this.keyword = keyword;
        this.precisions = precisions;
        this.text = text;
        this.community_id = community_id;
        this.community_name = community_name;
        this.pic = pic;
        this.showPic = showPic;
        this.info = info;
        this.url = url;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.click = click;
        this.token = token;
        this.title = title;
        this.usort = usort;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.writer = writer;
        this.texttype = texttype;
        this.usorts = usorts;
        this.is_focus = is_focus;
        this.pinglun = pinglun;
        this.praise = praise;
        this.recommend = recommend;
        this.showtime = showtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPrecisions() {
        return precisions;
    }

    public void setPrecisions(String precisions) {
        this.precisions = precisions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getShowPic() {
        return showPic;
    }

    public void setShowPic(String showPic) {
        this.showPic = showPic;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsort() {
        return usort;
    }

    public void setUsort(String usort) {
        this.usort = usort;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTexttype() {
        return texttype;
    }

    public void setTexttype(String texttype) {
        this.texttype = texttype;
    }

    public String getUsorts() {
        return usorts;
    }

    public void setUsorts(String usorts) {
        this.usorts = usorts;
    }

    public String getIs_focus() {
        return is_focus;
    }

    public void setIs_focus(String is_focus) {
        this.is_focus = is_focus;
    }

    public String getPinglun() {
        return pinglun;
    }

    public void setPinglun(String pinglun) {
        this.pinglun = pinglun;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", uname='" + uname + '\'' +
                ", keyword='" + keyword + '\'' +
                ", precisions='" + precisions + '\'' +
                ", text='" + text + '\'' +
                ", community_id='" + community_id + '\'' +
                ", community_name='" + community_name + '\'' +
                ", pic='" + pic + '\'' +
                ", showPic='" + showPic + '\'' +
                ", info='" + info + '\'' +
                ", url='" + url + '\'' +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", click='" + click + '\'' +
                ", token='" + token + '\'' +
                ", title='" + title + '\'' +
                ", usort='" + usort + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", type='" + type + '\'' +
                ", writer='" + writer + '\'' +
                ", texttype='" + texttype + '\'' +
                ", usorts='" + usorts + '\'' +
                ", is_focus='" + is_focus + '\'' +
                ", pinglun='" + pinglun + '\'' +
                ", praise='" + praise + '\'' +
                ", recommend='" + recommend + '\'' +
                ", showtime='" + showtime + '\'' +
                '}';
    }
}
