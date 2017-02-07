package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.*;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;

/**
 * Created by Administrator on 2016/7/29.
 */
public class OwnerAuthenticationActivity extends Activity {
    private RelativeLayout llTitleBar;
    private LinearLayout llBack;
    private TextView tvTip = null;
    private SpannableString msp = null;
    private Button btnOwnerAuthentication;
    private Button btnScanFirst;
    private ImageView ivBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_authentication);

        findViews();
        addListener();
        initContent();
        setBackground();
    }

    private void findViews() {
        llTitleBar = (RelativeLayout) findViewById(R.id.ll_title_bar_owner_authentication);
        llBack = (LinearLayout) findViewById(R.id.ll_back_owner_authentication);
        tvTip = (TextView)findViewById(R.id.tv_tip_owner_authentication);
        btnOwnerAuthentication = (Button) findViewById(R.id.btn_owner_authentication);
        btnScanFirst = (Button) findViewById(R.id.btn_scan_first);
        ivBg = (ImageView) findViewById(R.id.iv_owner_authentication_bg);
    }

    private void addListener() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOwnerAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerAuthenticationActivity.this, UserAuthenticationActivity.class);
                startActivity(intent);
            }
        });
        btnScanFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initContent() {
        initStatusBar();
        //创建一个 SpannableString对象
        msp = new SpannableString(getResources().getString(R.string.suggest_user_authentication));
        //设置字体前景色
        msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        tvTip.setText(msp);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            llTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }

    private void setBackground() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams lp = ivBg.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ivBg.setLayoutParams(lp);
        ivBg.setMaxWidth(screenWidth);
        ivBg.setMaxHeight(screenWidth * 2);
    }
}
