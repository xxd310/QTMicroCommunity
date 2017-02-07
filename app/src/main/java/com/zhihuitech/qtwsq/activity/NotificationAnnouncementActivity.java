package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.adapter.MyHousePropertyListAdapter;
import com.zhihuitech.qtwsq.adapter.NotificationAnnouncementListAdapter;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.entity.NotificationAnnouncement;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/7/30.
 */
public class NotificationAnnouncementActivity extends Activity {
    private LinearLayout llBack;
    private ListView lvNotificationAnnouncement;
    private NotificationAnnouncementListAdapter adapter;
    private List<NotificationAnnouncement> list;

    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_announcement);

        myApp = (MyApplication) this.getApplication();

        initStatusBar();
        findViews();
        addListeners();

        MyTask task = new MyTask();
        task.execute();
    }

    private void addListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvNotificationAnnouncement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotificationAnnouncementActivity.this, NotificationAnnouncementDetailActivity.class);
                intent.putExtra("notice", list.get(position));
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back_notification_announcement);
        lvNotificationAnnouncement = (ListView) findViewById(R.id.lv_notification_announcement);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//            llTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }

    class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = DataProvider.notices(myApp.getUser().getId());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseNoticesResult(s);
            if(list != null && list.size() > 0) {
                adapter = new NotificationAnnouncementListAdapter(NotificationAnnouncementActivity.this, list);
                lvNotificationAnnouncement.setAdapter(adapter);
            }
        }
    }

    private void parseNoticesResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 0) {
                    CustomViewUtil.createToast(NotificationAnnouncementActivity.this, resultObject.getString(""));
                } else if(resultObject.getInt("status") == 1) {
                    JSONArray noticeArray = resultObject.has("notice") ? resultObject.getJSONArray("notice") : null;
                    if(noticeArray != null && noticeArray.length() > 0) {
                        Gson gson = new Gson();
                        list = gson.fromJson(noticeArray.toString(), new TypeToken<List<NotificationAnnouncement>>() {}.getType());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
