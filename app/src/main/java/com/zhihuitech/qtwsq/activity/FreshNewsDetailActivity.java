package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    }
}
