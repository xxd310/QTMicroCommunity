package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.width;

/**
 * Created by Administrator on 2016/8/1.
 */
public class RegisterActivity extends Activity {
    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private ImageView ivBg;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etPhone;
    private EditText etCode;
    private Button btnRegister;
    private int screenWidth;
    private TextView tvGetCode;
    // 获取验证码计时器
    private TimeCount time;

    private String realName;
    private String password;
    private String confirmPassword;
    private String tel;
    private String code;

    private final int GET_CODE = 1;
    private final int REGISTER = 2;
    private int currentOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initStatusBar();

        findViews();
        addListeners();
        getScreenWidth();
        setBackground();

        setLeftDrawableForEditText(etUsername, R.drawable.user_name);
        setLeftDrawableForEditText(etPassword, R.drawable.password);
        setLeftDrawableForEditText(etConfirmPassword, R.drawable.password);
        setLeftDrawableForEditText(etPhone, R.drawable.phone);
        setLeftDrawableForEditText(etCode, R.drawable.code);

        // 初始化获取验证码的计时器
        time = new TimeCount(60000, 1000);
    }

    /**
     *  获取屏幕宽度
     */
    private void getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = etPhone.getText().toString();
                if(tel.equals("")) {
                    CustomViewUtil.createToast(RegisterActivity.this, getResources().getString(R.string.null_tel_tip));
                } else {
                    Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                    Matcher m = p.matcher(tel);
                    if(!m.matches()) {
                        CustomViewUtil.createToast(RegisterActivity.this, getResources().getString(R.string.wrong_format_of_tel));
                    } else {
                        time.start();
                        currentOperation = GET_CODE;
                        MyTask task = new MyTask();
                        task.execute();
                    }
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realName = etUsername.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                code = etCode.getText().toString();
                tel = etPhone.getText().toString();
                if(realName.equals("") || password.equals("") || confirmPassword.equals("") || code.equals("") || tel.equals("")) {
                    CustomViewUtil.createToast(RegisterActivity.this, getResources().getString(R.string.uncompleted_information));
                } else {
                    if(!password.equals(confirmPassword)) {
                        CustomViewUtil.createToast(RegisterActivity.this, getResources().getString(R.string.different_password));
                        return;
                    }
                    Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                    Matcher m = p.matcher(tel);
                    if(!m.matches()) {
                        CustomViewUtil.createToast(RegisterActivity.this, getResources().getString(R.string.wrong_format_of_tel));
                        return;
                    }
                    currentOperation = REGISTER;
                    MyTask task = new MyTask();
                    task.execute();
                }
            }
        });
    }

    private void findViews() {
        rlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar_register);
        ivBack = (ImageView) findViewById(R.id.iv_back_register);
        ivBg = (ImageView) findViewById(R.id.iv_register_bg);
        etUsername = (EditText) findViewById(R.id.et_username_register);
        etPassword = (EditText) findViewById(R.id.et_password_register);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password_register);
        etPhone = (EditText) findViewById(R.id.et_phone_register);
        etCode = (EditText) findViewById(R.id.et_code_register);
        tvGetCode = (TextView) findViewById(R.id.tv_retrieve_code);
        btnRegister = (Button) findViewById(R.id.btn_register_register);
    }

    class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(currentOperation == REGISTER) {
                CustomViewUtil.createProgressDialog(RegisterActivity.this, getResources().getString(R.string.submitting_information));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            if(currentOperation == GET_CODE) {
                result = DataProvider.getSMS(tel);
            } else if(currentOperation == REGISTER) {
                result = DataProvider.register(tel, password, code, realName);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            CustomViewUtil.dismissDialog();
            if(currentOperation == GET_CODE) {
                parseGetCodeResult(result);
            } else if(currentOperation == REGISTER) {
                parseRegisterResult(result);
            }
        }
    }

    private void parseGetCodeResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 0) {
                    CustomViewUtil.createToast(RegisterActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseRegisterResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 0) {
                    CustomViewUtil.createToast(RegisterActivity.this, resultObject.getString("sign"));
                } else {
                    Intent intent = new Intent(RegisterActivity.this, OwnerAuthenticationActivity.class);
                    startActivity(intent);
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

    /**
     * 获取验证码计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setClickable(false);
            tvGetCode.setText(millisUntilFinished / 1000
                    + getResources().getString(R.string.retrieve_again));
            tvGetCode.setTextColor(Color.GRAY);
        }

        @Override
        public void onFinish() {
            tvGetCode.setText(getResources().getString(R.string.retrieve_code));
            tvGetCode.setClickable(true);
            tvGetCode.setTextColor(getResources().getColor(R.color.code_text));
        }
    }

    private void setBackground() {
        ViewGroup.LayoutParams lp = ivBg.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ivBg.setLayoutParams(lp);
        ivBg.setMaxWidth(screenWidth);
        ivBg.setMaxHeight(screenWidth * 5);
    }

    private void setLeftDrawableForEditText(EditText et, int id) {
        Drawable drawable = getResources().getDrawable(id);
        if(screenWidth >= 320 && screenWidth <= 720) {
            drawable.setBounds(0, 0, 35, 35);
        } else if(screenWidth > 720) {
            drawable.setBounds(0, 0, 60, 60);
        } else {
            drawable.setBounds(0, 0, 25, 25);
        }
        et.setCompoundDrawables(drawable, null, null, null);//只放左边
    }
}
