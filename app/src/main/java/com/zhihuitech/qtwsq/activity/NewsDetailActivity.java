package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhihuitech.qtwsq.entity.News;
import com.zhihuitech.qtwsq.entity.NotificationAnnouncement;
import com.zhihuitech.qtwsq.provider.DataProvider;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/30.
 */
public class NewsDetailActivity extends Activity {
    private News news;
    private LinearLayout llBack;
    private String id;
    private WebView wv;
    private TextView tvTitle;
    private TextView tvCreateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        initStatusBar();

        findViews();
        addListeners();

        Intent intent = getIntent();
        news = (News) intent.getSerializableExtra("news");
        wv.loadData(news.getInfo().replace("<img", "<img style=\"max-width: 100%\""), "text/html; charset=UTF-8", null);

        tvTitle.setText(news.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        tvCreateTime.setText(format.format(new Date(Long.parseLong(news.getCreatetime()) * 1000)));
    }

    private void addListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back_news_detail);
        tvTitle = (TextView) findViewById(R.id.tv_news_title_news_detail);
        tvCreateTime = (TextView) findViewById(R.id.tv_news_create_time_news_detail);
        wv = (WebView) findViewById(R.id.wv_news_info);
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

}
