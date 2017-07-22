package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ValidatePhoneActivity extends Activity {
    private ImageView ivBack;
    private TextView tvTitle;
    private EditText etTel;
    private EditText etCode;
    private TextView tvGetCode;
    private Button btnNextStep;

    private String tel;
    private String code;

    // 获取验证码计时器
    private TimeCount time;

    private final static int GET_CODE = 0;
    private final static int GET_NEXT = 1;

    private MyApplication myApp;

    private String source;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_phone);
        myApp = (MyApplication) getApplication();

        initStatusBar();

        findViews();
        addListeners();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("source")) {
            source = intent.getStringExtra("source");
        } else {
            source = getResources().getString(R.string.forget_password);
        }
        tvTitle.setText(source);

        // 初始化获取验证码的计时器
        time = new TimeCount(60000, 1000);
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myApp.getActivities().contains(ValidatePhoneActivity.this)) {
                    myApp.getActivities().remove(ValidatePhoneActivity.this);
                }
                finish();
            }
        });
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = etTel.getText().toString();
                if(!tel.equals("")) {
                    Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                    Matcher m = p.matcher(tel);
                    if(!m.matches()) {
                        CustomViewUtil.createToast(ValidatePhoneActivity.this, getResources().getString(R.string.wrong_format_of_tel));
                    } else {
                        time.start();
                        new Thread() {
                            @Override
                            public void run() {
                                String result = DataProvider.sendSMS(tel);
                                Message msg = handler.obtainMessage();
                                msg.what = GET_CODE;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        }.start();
                    }
                } else {
                    CustomViewUtil.createToast(ValidatePhoneActivity.this, getResources().getString(R.string.null_tel_tip));
                }
            }
        });
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = etTel.getText().toString();
                code = etCode.getText().toString();
                if(tel.equals("") || code.equals("")) {
                    CustomViewUtil.createToast(ValidatePhoneActivity.this, getResources().getString(R.string.no_tel_code_tip));
                } else {
                    Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                    Matcher m = p.matcher(tel);
                    if(!m.matches()) {
                        CustomViewUtil.createToast(ValidatePhoneActivity.this, getResources().getString(R.string.wrong_format_of_tel));
                    } else {
                        CustomViewUtil.createProgressDialog(ValidatePhoneActivity.this, getResources().getString(R.string.submitting_information));
                        new Thread() {
                            @Override
                            public void run() {
                                String result = DataProvider.getNext(tel, code);
                                Message msg = handler.obtainMessage();
                                msg.what = GET_NEXT;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        }.start();
                    }
                }
            }
        });
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_activity_validate_phone);
        tvTitle = (TextView) findViewById(R.id.tv_title_activity_validate_phone);
        etTel = (EditText) findViewById(R.id.et_tel_activity_validate_phone);
        etCode = (EditText) findViewById(R.id.et_code_activity_validate_phone);
        btnNextStep = (Button) findViewById(R.id.btn_next_step_activity_validate_phone);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code_activity_validate_phone);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CustomViewUtil.dismissDialog();
            switch (msg.what) {
                case GET_CODE:
                    parseGetCodeResult((String)msg.obj);
                    break;
                case GET_NEXT:
                    parseGetNextResult((String)msg.obj);
                    break;
            }
        }
    };

    private void parseGetCodeResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                if(result.contains("超时")) {
                    CustomViewUtil.createToast(ValidatePhoneActivity.this, result);
                    return;
                }
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 0) {
                    CustomViewUtil.createToast(ValidatePhoneActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseGetNextResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                if(result.contains("超时")) {
                    CustomViewUtil.createToast(ValidatePhoneActivity.this, result);
                    return;
                }
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 0) {
                    CustomViewUtil.createToast(ValidatePhoneActivity.this, resultObject.getString("sign"));
                } else if(resultObject.getInt("status") == 1) {
                    Intent intent = new Intent(ValidatePhoneActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("tel", tel);
                    intent.putExtra("source", source);
                    startActivity(intent);
                    myApp.addActivity(ValidatePhoneActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

}
