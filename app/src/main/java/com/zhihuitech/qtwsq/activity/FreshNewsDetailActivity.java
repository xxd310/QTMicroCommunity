package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class FreshNewsDetailActivity extends Activity {
    private WebView wv;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fresh_news_detail);

        url = getIntent().getStringExtra("url");

        wv = (WebView) findViewById(R.id.wv_fresh_news_detail);
        wv.loadUrl(url);
        wv.getSettings().setJavaScriptEnabled(true);//启用js
        wv.getSettings().setBlockNetworkImage(false);//解决图片不显示

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

    }
}
