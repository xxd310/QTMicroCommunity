package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ResetPasswordActivity extends Activity {
    private ImageView ivBack;
    private TextView tvTitle;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnSubmit;

    private String tel;
    private String newPassword;
    private String confirmPassword;

    private MyApplication myApp;

    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        myApp = (MyApplication) getApplication();

        initStatusBar();

        findViews();
        addListeners();

        Intent intent = getIntent();
        tel = intent.getStringExtra("tel");
        source = intent.getStringExtra("source");
        tvTitle.setText(source);
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = etNewPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                if(newPassword.equals("") || confirmPassword.equals("")) {
                    CustomViewUtil.createToast(ResetPasswordActivity.this, getResources().getString(R.string.null_password_tip));
                } else {
                    if(!newPassword.equals(confirmPassword)) {
                        CustomViewUtil.createToast(ResetPasswordActivity.this, getResources().getString(R.string.different_password));
                    } else {
                        if(newPassword.length() < 6) {
                            CustomViewUtil.createToast(ResetPasswordActivity.this, getResources().getString(R.string.wrong_password_length_tip));
                        } else {
                            MyTask task = new MyTask();
                            task.execute();
                        }
                    }
                }
            }
        });
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_activity_reset_password);
        tvTitle = (TextView) findViewById(R.id.tv_title_activity_reset_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password_activity_reset_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password_activity_reset_password);
        btnSubmit = (Button) findViewById(R.id.btn_submit_activity_reset_password);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomViewUtil.createProgressDialog(ResetPasswordActivity.this, getResources().getString(R.string.submitting_information));
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = DataProvider.setPassword(tel, newPassword);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CustomViewUtil.dismissDialog();
            parseSetPasswordResult(s);
        }
    }

    private void parseSetPasswordResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                if(result.contains("超时")) {
                    CustomViewUtil.createToast(ResetPasswordActivity.this, result);
                    return;
                }
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    ResetPasswordActivity.this.finish();
                    myApp.getActivities().get(1).finish();
                    myApp.getActivities().remove(1);
                } else {
                    CustomViewUtil.createToast(ResetPasswordActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
