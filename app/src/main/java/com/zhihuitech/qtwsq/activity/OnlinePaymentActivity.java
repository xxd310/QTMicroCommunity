package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

/**
 * Created by Administrator on 2016/8/1.
 */
public class OnlinePaymentActivity extends Activity {
    // 物业费缴费
    private LinearLayout llPropertyCostPayment;
    // 车位缴纳
    private LinearLayout llParkingSpacePayment;
    // 自助缴费
    private LinearLayout llSelfPayment;
    // 我的缴费记录
    private LinearLayout llMyPaymentRecord;
    // 返回
    private ImageView ivBack;
    // 退出
    private TextView tvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_payment);

        initStatusBar();
        findViews();
        addListeners();
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

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_online_payment);
        tvExit = (TextView) findViewById(R.id.tv_exit_online_payment);
        llPropertyCostPayment = (LinearLayout) findViewById(R.id.ll_property_cost_payment);
        llParkingSpacePayment = (LinearLayout) findViewById(R.id.ll_parking_space_payment);
        llSelfPayment = (LinearLayout) findViewById(R.id.ll_self_payment);
        llMyPaymentRecord = (LinearLayout) findViewById(R.id.ll_my_payment_record);
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
        llPropertyCostPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnlinePaymentActivity.this, PayPropertyCostActivity.class);
                startActivity(intent);
            }
        });
        llParkingSpacePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        llSelfPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        llMyPaymentRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}
