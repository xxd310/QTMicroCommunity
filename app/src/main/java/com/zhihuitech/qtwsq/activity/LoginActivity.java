package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.entity.DropDownItem;
import com.zhihuitech.qtwsq.entity.User;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.R.attr.data;
import static com.zhihuitech.qtwsq.activity.R.string.username;
import static com.zhihuitech.qtwsq.util.Constant.LOGIN;
import static com.zhihuitech.qtwsq.util.Constant.SET_PASSWORD;

/**
 * Created by Administrator on 2016/7/30.
 */
public class LoginActivity extends Activity {
    private Button btnLogin;
    private Button btnRegister;
    private TextView tvForgetPassword;
    private EditText etUsername;
    private EditText etPassword;
    private String tel;
    private String password;

    private MyApplication myApp;

    private CheckBox cbAutoLogin;
    // 定义一个SharedPreferences用于保存数据
    private SharedPreferences pref = null;
    private static final String PREF_NAME = "qtwsq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        myApp = (MyApplication) this.getApplication();
        // 沉浸式状态栏
        initStatusBar();
        // 根据id获取相应的控件对象
        findViews();
        // 为控件设置监听事件
        addListeners();
        // 根据Intent的来源(1. 启动应用 2. 设置->退出当前账号)执行清数据或自动登录操作
        Intent intent = getIntent();
        if(intent != null) {
            // 来源二
            if(intent.hasExtra("exit") && intent.getBooleanExtra("exit", false)) {
                clearData();
            }
            // 来源一
            else {
                checkAutoLogin();
            }
        }
    }

    /**
     * 清除SharedPreference中保存的密码和时间戳
     */
    private void clearData() {
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        tel = pref.getString("tel", "");
        etUsername.setText(tel);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("password", "");
        edit.putLong("save_time", 0);
        edit.commit();
    }

    /**
     *
     */
    private void checkAutoLogin() {
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // 已经超过15天
        if(System.currentTimeMillis() - pref.getLong("save_time" , 0) > 15 * 24 * 60 * 60 * 1000) {
            tel = pref.getString("tel", "");
            etUsername.setText(tel);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("password", "");
            edit.putLong("save_time", 0);
            edit.commit();
        } else {
            tel = pref.getString("tel", "");
            password = pref.getString("password", "");
            etUsername.setText(tel);
            etPassword.setText(password);
            cbAutoLogin.setChecked(true);
            MyTask task = new MyTask();
            task.execute();
        }
    }

    private void addListeners() {
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ValidatePhoneActivity.class);
                intent.putExtra("source", getResources().getString(R.string.forget_password));
                startActivity(intent);
                myApp.addActivity(LoginActivity.this);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = etUsername.getText().toString();
                password = etPassword.getText().toString();
                if(tel.equals("")) {
                    CustomViewUtil.createToast(LoginActivity.this, getResources().getString(R.string.null_tel_tip));
                } else {
                    MyTask loginTask = new MyTask();
                    loginTask.execute();
                }

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnRegister = (Button) findViewById(R.id.btn_register_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password_login);
        etUsername = (EditText) findViewById(R.id.et_username_login);
        etPassword = (EditText) findViewById(R.id.et_password_login);
        cbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login_login);
    }

    class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomViewUtil.createProgressDialog(LoginActivity.this, getResources().getString(R.string.validating_information));
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = DataProvider.login(tel, password);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            CustomViewUtil.dismissDialog();
            parseLoginResult(result);
        }
    }

    private void parseLoginResult(String result) {
        try {
            if (result != null && !result.equals("")) {
                if(result.contains("超时")) {
                    CustomViewUtil.createToast(LoginActivity.this, result);
                    return;
                }
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 0) {
                    CustomViewUtil.createToast(LoginActivity.this, resultObject.getString("sign"));
                } else if(resultObject.getInt("status") == 1) {
                    JSONObject userObject = resultObject.getJSONObject("user");
                    Gson gson = new Gson();
                    User user =  gson.fromJson(userObject.toString(), new TypeToken<User>() {}.getType());
                    myApp.setUser(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    saveDataForAutoLogin();
                } else if(resultObject.getInt("status") == 2) {
                    Intent intent = new Intent(LoginActivity.this, ValidatePhoneActivity.class);
                    intent.putExtra("source", getResources().getString(R.string.set_password));
                    startActivity(intent);
                    myApp.addActivity(LoginActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 如果自动登录被选择，保存用户名和密码到SharedPreference
     */
    private void saveDataForAutoLogin() {
        if(cbAutoLogin.isChecked()) {
            pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("tel", tel);
            edit.putString("password", password);
            edit.putLong("save_time", System.currentTimeMillis());
            edit.commit();
        }
    }

}
