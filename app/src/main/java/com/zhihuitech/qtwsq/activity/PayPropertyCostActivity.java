package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;

/**
 * Created by Administrator on 2016/8/3.
 */
public class PayPropertyCostActivity extends Activity {
    private ImageView ivBack;
    private TextView tvExit;
    private RelativeLayout rlTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_property_cost);

        findViews();
        addListeners();
        initStatusBar();
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        rlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar_pay_property_cost);
        ivBack = (ImageView) findViewById(R.id.iv_back_pay_property_cost);
        tvExit = (TextView) findViewById(R.id.tv_exit_pay_property_cost);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏颜色
            tintManager.setTintColor(getResources().getColor(R.color.title_bar_bg));
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            rlTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }
}
