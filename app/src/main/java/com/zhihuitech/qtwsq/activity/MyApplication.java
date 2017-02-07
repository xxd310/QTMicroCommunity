package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MyApplication extends Application {
    private User user;
    private MyHouseProperty defaultHouseProperty;
    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        // the following line is important
        Fresco.initialize(getApplicationContext());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MyHouseProperty getDefaultHouseProperty() {
        return defaultHouseProperty;
    }

    public void setDefaultHouseProperty(MyHouseProperty defaultHouseProperty) {
        this.defaultHouseProperty = defaultHouseProperty;
    }

    // 在Activity的OnCreate方法中调用,添加Activity实例
    public void addActivity(Activity act) {
        if (activities == null) {
            activities = new ArrayList<Activity>();
        }
        activities.add(act);
    }

    // 退出程序时调用，调用所有Activity的finish方法
    public void finishAll() {
        if(activities == null) {
            return;
        }
        for (Activity act : activities) {
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        activities = null;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
