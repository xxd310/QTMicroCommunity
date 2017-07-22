package com.zhihuitech.qtwsq.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.zhihuitech.qtwsq.fragment.FreshNewsFragment;
import com.zhihuitech.qtwsq.fragment.HomePageFragment;
import com.zhihuitech.qtwsq.fragment.PersonalCenterFragment;
import com.zhihuitech.qtwsq.fragment.ActivityFragment;
import com.zhihuitech.qtwsq.util.CustomViewUtil;

/**
 * Created by Administrator on 2016/7/30.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    // 定义4个Fragment对象
    private HomePageFragment homePageFragment;
    private FreshNewsFragment freshNewsFragment;
    private ActivityFragment activityFragment;
    private PersonalCenterFragment personalCenterFragment;

    // 定义每个选项中的相关控件
    private LinearLayout llHomePage;
    private LinearLayout llFreshNews;
    private LinearLayout llActivity;
    private LinearLayout llPersonalCenter;

    private ImageView ivHomePage;
    private ImageView ivFreshNews;
    private ImageView ivActivity;
    private ImageView ivPersonalCenter;

    private TextView tvHomePage;
    private TextView tvFreshNews;
    private TextView tvActivity;
    private TextView tvPersonalCenter;

    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;

    private static boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatusBar();

        fragmentManager = getSupportFragmentManager();
        findViews(); // 初始化界面控件
        addListeners();
        // 初始化页面加载时显示第一个选项卡
        setChoiceItem(0);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void findViews() {
        // 初始化底部导航栏的控件
        ivHomePage = (ImageView) findViewById(R.id.iv_home_page_activity_main);
        ivFreshNews = (ImageView) findViewById(R.id.iv_fresh_news_activity_main);
        ivActivity = (ImageView) findViewById(R.id.iv_activity_activity_main);
        ivPersonalCenter = (ImageView) findViewById(R.id.iv_personal_center_activity_main);

        tvHomePage = (TextView) findViewById(R.id.tv_home_page_activity_main);
        tvFreshNews = (TextView) findViewById(R.id.tv_fresh_news_activity_main);
        tvActivity = (TextView) findViewById(R.id.tv_activity_activity_main);
        tvPersonalCenter = (TextView) findViewById(R.id.tv_personal_center_activity_main);

        llHomePage = (LinearLayout) findViewById(R.id.ll_home_page_activity_main);
        llFreshNews = (LinearLayout) findViewById(R.id.ll_fresh_news_activity_main);
        llActivity = (LinearLayout) findViewById(R.id.ll_activity_activity_main);
        llPersonalCenter = (LinearLayout) findViewById(R.id.ll_personal_center_activity_main);
    }

    private void addListeners() {
        llHomePage.setOnClickListener(MainActivity.this);
        llFreshNews.setOnClickListener(MainActivity.this);
        llActivity.setOnClickListener(MainActivity.this);
        llPersonalCenter.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home_page_activity_main:
                setChoiceItem(0);
                break;
            case R.id.ll_fresh_news_activity_main:
                setChoiceItem(1);
                break;
            case R.id.ll_activity_activity_main:
                setChoiceItem(2);
                break;
            case R.id.ll_personal_center_activity_main:
                setChoiceItem(3);
                break;
            default:
                break;
        }

    }

    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setChoiceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 清空, 重置选项, 隐藏所有Fragment
        clearChoice();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                ivHomePage.setImageResource(R.drawable.home_page_selected);
                tvHomePage.setTextColor(getResources().getColor(R.color.title_bar_bg));
                // 如果homePageFragment为空，则创建一个并添加到界面上
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    fragmentTransaction.add(R.id.content_activity_main, homePageFragment);
                } else {
                    fragmentTransaction.show(homePageFragment);
                }
                break;
            case 1:
                ivFreshNews.setImageResource(R.drawable.fresh_news_selected);
                tvFreshNews.setTextColor(getResources().getColor(R.color.title_bar_bg));

                if (freshNewsFragment == null) {
                    freshNewsFragment = new FreshNewsFragment();
                    fragmentTransaction.add(R.id.content_activity_main, freshNewsFragment);
                } else {
                    fragmentTransaction.show(freshNewsFragment);
                }
                break;
            case 2:
                ivActivity.setImageResource(R.drawable.activity_selected);
                tvActivity.setTextColor(getResources().getColor(R.color.title_bar_bg));
                if (activityFragment == null) {
                    activityFragment = new ActivityFragment();
                    fragmentTransaction.add(R.id.content_activity_main, activityFragment);
                } else {
                    fragmentTransaction.show(activityFragment);
                }
                break;
            case 3:
                ivPersonalCenter.setImageResource(R.drawable.personal_center_selected);
                tvPersonalCenter.setTextColor(getResources().getColor(R.color.title_bar_bg));
                if (personalCenterFragment == null) {
                    personalCenterFragment = new PersonalCenterFragment();
                    fragmentTransaction.add(R.id.content_activity_main, personalCenterFragment);
                } else {
                    fragmentTransaction.show(personalCenterFragment);
                }
                break;
        }
        fragmentTransaction.commit();   // 提交
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChoice() {
        ivHomePage.setImageResource(R.drawable.home_page_unselected);
        tvHomePage.setTextColor(Color.LTGRAY);
        llHomePage.setBackgroundColor(Color.WHITE);

        ivFreshNews.setImageResource(R.drawable.fresh_news_unselected);
        tvFreshNews.setTextColor(Color.LTGRAY);
        llFreshNews.setBackgroundColor(Color.WHITE);

        ivActivity.setImageResource(R.drawable.activity_unselected);
        tvActivity.setTextColor(Color.LTGRAY);
        llActivity.setBackgroundColor(Color.WHITE);

        ivPersonalCenter.setImageResource(R.drawable.personal_center_unselected);
        tvPersonalCenter.setTextColor(Color.LTGRAY);
        llPersonalCenter.setBackgroundColor(Color.WHITE);
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (homePageFragment != null) {
            fragmentTransaction.hide(homePageFragment);
        }

        if (freshNewsFragment != null) {
            fragmentTransaction.hide(freshNewsFragment);
        }

        if (activityFragment != null) {
            fragmentTransaction.hide(activityFragment);
        }

        if (personalCenterFragment != null) {
            fragmentTransaction.hide(personalCenterFragment);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 标记用户不退出状态
            isExit = false;
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断用户是否单击的是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果isExit标记为false，提示用户再次按键
            if (!isExit) {
                isExit = true;
                CustomViewUtil.createToast(MainActivity.this, getResources().getString(R.string.press_again_to_exit_program));
                // 如果用户没有在2秒内再次按返回键的话，就发送消息标记用户为不退出状态
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                // 退出程序
                finish();
                System.exit(0);
            }
        }
        return false;
    }
}