package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;

/**
 * Created by Administrator on 2016/8/3.
 */
public class NavActivity extends Activity implements View.OnClickListener{
    private Button btnToMain;
    private Button btnToOwnerAuthentication;
    private Button btnToLogin;
    private Button btnToOnlinePayment;
    private Button btnToNotificationAnnouncement;
    private Button btnToCarManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav);

        findViews();
        addListeners();
    }

    private void addListeners() {
        btnToMain.setOnClickListener(this);
        btnToOwnerAuthentication.setOnClickListener(this);
        btnToLogin.setOnClickListener(this);
        btnToOnlinePayment.setOnClickListener(this);
        btnToNotificationAnnouncement.setOnClickListener(this);
        btnToCarManagement.setOnClickListener(this);
    }

    private void findViews() {
        btnToMain = (Button) findViewById(R.id.btn_to_main);
        btnToOwnerAuthentication = (Button) findViewById(R.id.btn_to_owner_authentication);
        btnToLogin = (Button) findViewById(R.id.btn_to_login);
        btnToOnlinePayment = (Button) findViewById(R.id.btn_to_online_payment);
        btnToNotificationAnnouncement = (Button) findViewById(R.id.btn_to_notification_announcement);
        btnToCarManagement = (Button) findViewById(R.id.btn_to_car_management);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_to_main:
                intent = new Intent(NavActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_owner_authentication:
                intent = new Intent(NavActivity.this, OwnerAuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_login:
                intent = new Intent(NavActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_online_payment:
                intent = new Intent(NavActivity.this, OnlinePaymentActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_notification_announcement:
                intent = new Intent(NavActivity.this, NotificationAnnouncementActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_to_car_management:
                intent = new Intent(NavActivity.this, CarManagementActivity.class);
                startActivity(intent);
                break;
        }
    }
}
