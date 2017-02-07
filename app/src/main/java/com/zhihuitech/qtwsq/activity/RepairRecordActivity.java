package com.zhihuitech.qtwsq.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhihuitech.qtwsq.fragment.CommonRepairRecordFragment;
import com.zhihuitech.qtwsq.fragment.FamilyRepairRecordFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class RepairRecordActivity extends ActionBarActivity {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private ImageView ivBack;

    //Tab显示内容TextView
    private TextView tvFamilyRepair, tvCommonRepair;

    //Tab的那个引导线
    private View lineView;

    private FamilyRepairRecordFragment frrFragment;
    private CommonRepairRecordFragment crrFragment;

    private int currentIndex;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_record);
        initStatusBar();

        findViews();
        init();
        initTabLineWidth();
        addListeners();
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvFamilyRepair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });
        tvCommonRepair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_repair_record);
        tvFamilyRepair = (TextView) findViewById(R.id.tv_family_record_tab);
        tvCommonRepair = (TextView) findViewById(R.id.tv_common_record_tab);
        lineView = findViewById(R.id.v_line);
        viewPager = (ViewPager) findViewById(R.id.vp_repair_record);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void init() {
        tvFamilyRepair.setTextColor(getResources().getColor(R.color.title_bar_bg));
        frrFragment = new FamilyRepairRecordFragment();
        crrFragment = new CommonRepairRecordFragment();
        fragmentList.add(frrFragment);
        fragmentList.add(crrFragment);

        fragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lineView
                        .getLayoutParams();

                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置lineView的左边距 滑动场景： 记2个页面, 从左到右分别为0,1 0->1; 1->0;
                 */
                if (currentIndex == 0 && position == 0) {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + (screenWidth * 1.0 / 2 - screenWidth * 1.0 / 3) / 2 + currentIndex * screenWidth / 2);
                } else if (currentIndex == 1 && position == 0) {
                    lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 2) + (screenWidth * 1.0 / 2 - screenWidth * 1.0 / 3) / 2 + currentIndex * screenWidth / 2);
                } else if (currentIndex == 1 && position == 1) {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + (screenWidth * 1.0 / 2 - screenWidth * 1.0 / 3) / 2 + currentIndex * screenWidth / 2);
                }
                lineView.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        tvFamilyRepair.setTextColor(getResources().getColor(R.color.title_bar_bg));
                        break;
                    case 1:
                        tvCommonRepair.setTextColor(getResources().getColor(R.color.title_bar_bg));
                        break;
                }
                currentIndex = position;
            }
        });

    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        tvFamilyRepair.setTextColor(Color.BLACK);
        tvCommonRepair.setTextColor(Color.BLACK);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lineView
                .getLayoutParams();
        lp.width = screenWidth / 3;
        lineView.setLayoutParams(lp);
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
