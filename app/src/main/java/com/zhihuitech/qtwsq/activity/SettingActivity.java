package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/7/30.
 */
public class SettingActivity extends Activity {
    private Button btnExit;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        myApp = (MyApplication) getApplication();

        findViews();
        addListeners();
    }

    private void addListeners() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.putExtra("exit", true);
                startActivity(intent);
                finish();
                myApp.finishAll();
            }
        });
    }

    private void findViews() {
        btnExit = (Button) findViewById(R.id.btn_exit_current_account_setting);
    }

}
