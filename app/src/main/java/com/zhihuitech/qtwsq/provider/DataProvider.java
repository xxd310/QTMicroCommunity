package com.zhihuitech.qtwsq.provider;

import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.DropDownItem;
import com.zhihuitech.qtwsq.util.Constant;
import com.zhihuitech.qtwsq.util.HttpUtil;
import com.zhihuitech.qtwsq.util.ImageUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/4.
 */
public class DataProvider {

    public static String login(String tel, String password) {
        NameValuePair telPair = new BasicNameValuePair("tel", tel);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(telPair);
        pairs.add(passwordPair);
        String result = HttpUtil.sendPostRequest(Constant.LOGIN, pairs);
        System.out.println("login.result=" + result);
        return result;
    }

    public static String getSMS(String tel) {
        NameValuePair telPair = new BasicNameValuePair("tel", tel);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(telPair);
        String result = HttpUtil.sendPostRequest(Constant.GET_SMS, pairs);
        System.out.println("getSMS.result=" + result);
        return result;
    }

    public static String sendSMS(String tel) {
        NameValuePair telPair = new BasicNameValuePair("tel", tel);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(telPair);
        String result = HttpUtil.sendPostRequest(Constant.SEND_SMS, pairs);
        System.out.println("sendSMS.result=" + result);
        return result;
    }

    public static String getNext(String tel, String code) {
        NameValuePair telPair = new BasicNameValuePair("tel", tel);
        NameValuePair codePair = new BasicNameValuePair("code", code);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(telPair);
        pairs.add(codePair);
        String result = HttpUtil.sendPostRequest(Constant.GET_NEXT, pairs);
        System.out.println("getNext.result=" + result);
        return result;
    }

    public static String register(String tel, String password, String code, String realname) {
        NameValuePair telPair = new BasicNameValuePair("tel", tel);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        NameValuePair codePair = new BasicNameValuePair("code", code);
        NameValuePair realnamePair = new BasicNameValuePair("realname", realname);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(telPair);
        pairs.add(passwordPair);
        pairs.add(codePair);
        pairs.add(realnamePair);
        String result = HttpUtil.sendPostRequest(Constant.REGISTER, pairs);
        System.out.println("register.result=" + result);
        return result;
    }

    public static List<DropDownItem> getUserTypeList() {
        List<DropDownItem> list = new ArrayList<>();
        list.add(new DropDownItem("1", "成员"));
        list.add(new DropDownItem("2", "业主"));
        list.add(new DropDownItem("3", "游客"));
        return list;
    }

    public static String setPassword(String tel, String password) {
        NameValuePair telPair = new BasicNameValuePair("tel", tel);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(telPair);
        pairs.add(passwordPair);
        String result = HttpUtil.sendPostRequest(Constant.SET_PASSWORD, pairs);
        System.out.println("setPassword.result=" + result);
        return result;
    }

    public static String notices(String id) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.NOTICES, pairs);
        System.out.println("notices.result=" + result);
        return result;
    }

    public static String noticesDetail(String id) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.NOTICES_DETAIL, pairs);
        System.out.println("noticesDetail.result=" + result);
        return result;
    }

    public static String news(String id, String eid) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        if(eid != null) {
            NameValuePair eidPair = new BasicNameValuePair("eid", eid);
            pairs.add(eidPair);
        }
        String result = HttpUtil.sendPostRequest(Constant.NEWS, pairs);
        System.out.println("news.result=" + result);
        return result;
    }

    public static String userEstate(String uid) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        String result = HttpUtil.sendPostRequest(Constant.USER_ESTATE, pairs);
        System.out.println("userEstate.result=" + result);
        return result;
    }

    public static String userEstateSetDefault(String uid, String id) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.USER_ESTATE, pairs);
        System.out.println("userEstateSetDefault.result=" + result);
        return result;
    }

    public static String userEstateDelete(String uid, String id) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.USER_ESTATE_DELETE, pairs);
        System.out.println("userEstateDelete.result=" + result);
        return result;
    }

    public static String userEstateAdd(String uid, String community_id, String address, String room_id, String type) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        NameValuePair communityIdPair = new BasicNameValuePair("community_id", community_id);
        NameValuePair addressPair = new BasicNameValuePair("address", address);
        NameValuePair roomIdPair = new BasicNameValuePair("room_id", room_id);
        NameValuePair typePair = new BasicNameValuePair("type", type);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        pairs.add(communityIdPair);
        pairs.add(addressPair);
        pairs.add(roomIdPair);
        pairs.add(typePair);
        String result = HttpUtil.sendPostRequest(Constant.USER_ESTATE_ADD, pairs);
        System.out.println("userEstateAdd.result=" + result);
        return result;
    }

    public static String getWeather(String cityName, String key) {
//        String result = HttpUtil.sendGetRequest("http://op.juhe.cn/onebox/weather/query?cityname=绍兴&key=8533d039ece9bd508ec6c09e152aa26d");
        NameValuePair citynamePair = new BasicNameValuePair("cityname", cityName);
        NameValuePair keyPair = new BasicNameValuePair("key", key);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(citynamePair);
        pairs.add(keyPair);
        String result = HttpUtil.sendPostRequest("http://op.juhe.cn/onebox/weather/query", pairs);
        System.out.println("getWeather.result=" + result);
        return result;
    }

    public static String getCarList(String uid) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        String result = HttpUtil.sendPostRequest(Constant.CAR, pairs);
        System.out.println("car.result=" + result);
        return result;
    }

    public static String getCarBrand() {
        String result = HttpUtil.sendGetRequest(Constant.GET_CAR_BRAND);
        System.out.println("getCarBrand.result=" + result);
        return result;
    }

    public static String getUserInfo(String uid) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        String result = HttpUtil.sendPostRequest(Constant.USER_INFO, pairs);
        System.out.println("getUserInfo.result=" + result);
        return result;
    }

    public static String carAdd(String uid, String brandId, String carNo, String img1, String img2) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        NameValuePair brandIdPair = new BasicNameValuePair("brand_id", brandId);
        NameValuePair carNoPair = new BasicNameValuePair("car_no", carNo);
        NameValuePair img1Pair = new BasicNameValuePair("img1", ImageUtils.encode(img1));
        NameValuePair img2Pair = new BasicNameValuePair("img2", ImageUtils.encode(img2));
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        pairs.add(brandIdPair);
        pairs.add(carNoPair);
        pairs.add(img1Pair);
        pairs.add(img2Pair);
        String result = HttpUtil.sendPostRequest(Constant.CAR_ADD, pairs);
        System.out.println("carAdd.result=" + result);
        return result;
    }

    public static String carDel(String uid, String id) {
        NameValuePair uidPair = new BasicNameValuePair("uid", uid);
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(uidPair);
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.CAR_DEL, pairs);
        System.out.println("carDel.result=" + result);
        return result;
    }

    public static String uploadCarImg(String filePath) {
        String encodeString = ImageUtils.encode(filePath);
        NameValuePair imgurlPair = new BasicNameValuePair("imgurl", encodeString);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(imgurlPair);
        String result = HttpUtil.sendPostRequest("http://smart.wx91go.com/index.php?g=Home&m=Apiapp&a=addCarImg", pairs);
        System.out.println("uploadCarImg.result=" + result);
        return result;
    }

    public static String getDialogContent(String id, String step) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        NameValuePair stepPair = new BasicNameValuePair("step", step);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        pairs.add(stepPair);
        String result = HttpUtil.sendPostRequest(Constant.VERIFICATION_ISOWNER, pairs);
        System.out.println("getDialogContent.result=" + result);
        return result;
    }

    public static String verification_isOwner(String id, String community_id, String zone_id, String building_id, String unit_id, String room_id, String type) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        NameValuePair communityIdPair = new BasicNameValuePair("community_id", community_id);
        NameValuePair zoneIdPair = new BasicNameValuePair("zone_id", zone_id);
        NameValuePair buildingIdPair = new BasicNameValuePair("building_id", building_id);
        NameValuePair unitIdPair = new BasicNameValuePair("unit_id", unit_id);
        NameValuePair roomIdPair = new BasicNameValuePair("room_id", room_id);
        NameValuePair typePair = new BasicNameValuePair("type", type);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        pairs.add(communityIdPair);
        pairs.add(zoneIdPair);
        pairs.add(buildingIdPair);
        pairs.add(unitIdPair);
        pairs.add(roomIdPair);
        pairs.add(typePair);
        String result = HttpUtil.sendPostRequest(Constant.VERIFICATION_ISOWNER, pairs);
        System.out.println("verification_isOwner.result=" + result);
        return result;
    }

    public static Map<String, Integer> getWeatherNameIcon() {
        Map<String, Integer> map = new HashMap<>();
        map.put("晴", R.drawable.sunny);
        map.put("阴", R.drawable.cloudy_y);
        map.put("多云", R.drawable.cloudy_dy);
        map.put("小雨", R.drawable.rainy);
        map.put("中雨", R.drawable.rainy);
        map.put("大雨", R.drawable.rainy);
        map.put("暴雨", R.drawable.rainy);
        map.put("小雪", R.drawable.snowy);
        map.put("中雪", R.drawable.snowy);
        map.put("大雪", R.drawable.snowy);
        map.put("暴雪", R.drawable.snowy);
        map.put("雷阵雨", R.drawable.thunder);
        return map;
    }

    public static String records(String id, String type) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        NameValuePair typePair = new BasicNameValuePair("type", type);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        pairs.add(typePair);
        String result = HttpUtil.sendPostRequest(Constant.RECORDS, pairs);
        System.out.println("records.result=" + result);
        return result;
    }

    public static String family(String id) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.FAMILY, pairs);
        System.out.println("family.result=" + result);
        return result;
    }

    public static String common() {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        String result = HttpUtil.sendPostRequest(Constant.COMMON, pairs);
        System.out.println("common.result=" + result);
        return result;
    }

    public static String getRepair2(String id) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        String result = HttpUtil.sendPostRequest(Constant.GET_REPAIR2, pairs);
        System.out.println("getRepair2.result=" + result);
        return result;
    }

    public static String serviceSubmit(String id, String type, String entry_id, String entry_name, String entry_id2, String entry_name2, String booktime, String xiaoqu, String address, String info, String imgurl) {
        NameValuePair idPair = new BasicNameValuePair("id", id);
        NameValuePair typePair = new BasicNameValuePair("type", type);
        NameValuePair entryIdPair = new BasicNameValuePair("entry_id", entry_id);
        NameValuePair entryNamePair = new BasicNameValuePair("entry_name", entry_name);
        NameValuePair entryId2Pair = new BasicNameValuePair("entry_id2", entry_id2);
        NameValuePair entryName2Pair = new BasicNameValuePair("entry_name2", entry_name2);
        NameValuePair bookTimePair = new BasicNameValuePair("booktime", booktime);
//        NameValuePair xiaoquPair = new BasicNameValuePair("xiaoqu", xiaoqu);
        NameValuePair addressPair = new BasicNameValuePair("address", address);
        NameValuePair infoPair = new BasicNameValuePair("info", info);
//        NameValuePair imgUrlPair = new BasicNameValuePair("imgurl", imgurl);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(idPair);
        pairs.add(typePair);
        pairs.add(entryIdPair);
        pairs.add(entryNamePair);
        pairs.add(entryId2Pair);
        pairs.add(entryName2Pair);
        pairs.add(bookTimePair);
//        pairs.add(xiaoquPair);
        pairs.add(addressPair);
        pairs.add(infoPair);
//        pairs.add(imgUrlPair);
        String result = HttpUtil.sendPostRequest(Constant.SERVICE_SUBMIT, pairs);
        System.out.println("serviceSubmit.result=" + result);
        return result;
    }
}
