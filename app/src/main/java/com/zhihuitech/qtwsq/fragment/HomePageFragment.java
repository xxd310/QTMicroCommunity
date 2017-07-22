package com.zhihuitech.qtwsq.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.activity.*;
import com.zhihuitech.qtwsq.adapter.HousePropertyNameListAdapter;
import com.zhihuitech.qtwsq.entity.*;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/30.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout rlTitleBar;
    // 报修对话框
    private LinearLayout llFamilyRepair;
    private LinearLayout llCommonRepair;
    private AlertDialog repairDialog;
    private TextView tvCannotChoose;
    private TextView tvQueryMyRepairRecord;
    private TextView tvCancel;
    // 联系物业选择对话框
    private AlertDialog contactPropertyDialog;

    private TextView tvHousePropertyName;
    private TextView tvUserType;
    private TextView tvUserName;
    private TextView tvTemperature;
    private TextView tvWeather;
    private ImageView ivWeatherIcon;

    private LinearLayout llMostBeautifulPhoto;
    private LinearLayout llMicroFruit;
    private LinearLayout llRepair;
    private LinearLayout llCarManagement;
    private LinearLayout llOnlinePayment;
    private LinearLayout llLifeService;
    private LinearLayout llNotice;
    private LinearLayout llExpress;
    private LinearLayout llContactProperty;

    private ImageView ivMostBeautifulPhoto;
    private ImageView ivMicroFruit;
    private ImageView ivRepair;
    private ImageView ivCarManagement;
    private ImageView ivOnlinePayment;
    private ImageView ivLifeService;
    private ImageView ivNotice;
    private ImageView ivExpress;
    private ImageView ivContactProperty;


    private RelativeLayout rlLatestNotice;
    private TextView tvLatestNoticeTitle;
    private LinearLayout llLatestNews;
    private TextView tvShowTime;
    private TextView tvCommunityName;
    private TextView tvNewsTitle;
    private ImageView ivNewsPic;

    private List<MyHouseProperty> housePropertyList = new ArrayList<>();
    private ListView lvHousePropertyNameList;
    private HousePropertyNameListAdapter housePropertyAdapter;
    private AlertDialog housePropertyListDialog;

    private NotificationAnnouncement notice;
    private News news;

    private View mainView;

    private List<Flash> flashList = new ArrayList<>();
    private ViewPager mViewPager;
    private MyAdapter mVpAdapter;
    // 用来存放图片控件
    private List<View> mViews;
    // 用来存放点点图片
    private ImageView[] dots;
    // 用来处理图片自动播放
    private Timer timer;
    private boolean isContinue = true;
    private boolean play = true;

    private static final int initPosition = 50000;
    private static int currentPosition = initPosition;

    private static final int NEWS = 0;
    private static final int GET_WEATHER = 1;
    private static final int FLASH = 2;

    private MyApplication myApp;

    private String defaultHousePropertyId = "";

    private PullRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myApp = (MyApplication) getActivity().getApplication();
        mainView = inflater.inflate(R.layout.home_page, container, false);

        findViews(mainView);
        addListeners();
//        initTitleBar();
        initData();

        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.news(myApp.getUser().getId(), null);
                Message msg = mHandler.obtainMessage();
                msg.what = NEWS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.getWeather("绍兴", "8533d039ece9bd508ec6c09e152aa26d");
                Message msg = mHandler.obtainMessage();
                msg.what = GET_WEATHER;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        refreshLayout.setColor(getResources().getColor(R.color.title_bar_bg));
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUI(null);
            }
        });

        return mainView;
    }

    private void initData() {
        tvUserType.setText(myApp.getUser().getType().equals("1") ? "成员" : (myApp.getUser().getType().equals("2") ? "业主" : (myApp.getUser().getType().equals("3")) ? "租客" : "未知"));
        tvUserName.setText(myApp.getUser().getRealname());
        String isVerify = myApp.getUser().getIsverify();
        ivMostBeautifulPhoto.setImageResource(isVerify.equals("1") ? R.drawable.most_beautiful_photo : R.drawable.most_beautiful_photo_disabled);
        ivMicroFruit.setImageResource(isVerify.equals("1") ? R.drawable.micro_fruit : R.drawable.micro_fruit_disabled);
        ivRepair.setImageResource(isVerify.equals("1") ? R.drawable.need_repair : R.drawable.need_repair_disabled);
        ivCarManagement.setImageResource(isVerify.equals("1") ? R.drawable.car_management : R.drawable.car_management_disabled);
        ivOnlinePayment.setImageResource(isVerify.equals("1") ? R.drawable.online_payment : R.drawable.online_payment_disabled);
        ivLifeService.setImageResource(isVerify.equals("1") ? R.drawable.life_service : R.drawable.life_service_disabled);
        ivNotice.setImageResource(isVerify.equals("1") ? R.drawable.notice : R.drawable.notice_disabled);
        ivContactProperty.setImageResource(isVerify.equals("1") ? R.drawable.contact_property : R.drawable.contact_property_disabled);
        llMostBeautifulPhoto.setClickable(isVerify.equals("1") ? true : false);
        llMicroFruit.setClickable(isVerify.equals("1") ? true : false);
        llRepair.setClickable(isVerify.equals("1") ? true : false);
        llCarManagement.setClickable(isVerify.equals("1") ? true : false);
        llOnlinePayment.setClickable(isVerify.equals("1") ? true : false);
        llLifeService.setClickable(isVerify.equals("1") ? true : false);
        llNotice.setClickable(isVerify.equals("1") ? true : false);
        llContactProperty.setClickable(isVerify.equals("1") ? true : false);
    }

    // Handler对象更新UI
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEWS:
                    refreshLayout.setRefreshing(false);
                    parseNewsResult((String) msg.obj);
                    break;
                case GET_WEATHER:
                    parseGetWeatherResult((String) msg.obj);
                    break;
                case FLASH:
                    mViewPager.setCurrentItem(currentPosition);
                    break;
            }
        }

    };

    /**
     * 启动线程，监控是否要自动滑动
     */
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                while (play) {
                    if (isContinue) {
                        currentPosition++;
                        Message msg = mHandler.obtainMessage();
                        msg.what = FLASH;
                        mHandler.sendMessage(msg);
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 3000);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mViews = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < flashList.size(); i++) {
            ImageView mImageView = new ImageView(getActivity());
            mImageView.setLayoutParams(mParams);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageView.setId(i);
            mViews.add(mImageView);
            Glide.with(getActivity()).load(flashList.get(i).getImg()).asBitmap().into(mImageView);
            // 为图片设置监听事件
            mImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(!flashList.get(v.getId()).getUrl().equals("")) {
                        Intent intent = new Intent(getActivity(), FlashDetailActivity.class);
                        intent.putExtra("url", flashList.get(v.getId()).getUrl());
                        startActivity(intent);
                    }
                }
            });
        }
        mVpAdapter = new MyAdapter(mViews);
        mViewPager.setAdapter(mVpAdapter);
        mViewPager.setCurrentItem(currentPosition);
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setOnTouchListener(new MyTouchListener());
        initDots();
        // 解决第一次进入时第一张图片无法显示的bug
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
        // 开始自动切换图片的定时器
        startTimer();
    }

    /**
     * ViewPager的适配器类
     */
    class MyAdapter extends PagerAdapter {

        private String TAG = "MyAdapter";
        //界面列表
        private List<View> mViews;

        public MyAdapter(List<View> mViews) {
            this.mViews = mViews;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.v(TAG, "instantiateItem" + position);
            if (mViews.get(position % mViews.size()).getParent() == null) {
                container.addView(mViews.get(position % mViews.size()));
            } else {
                ((ViewGroup) mViews.get(position % mViews.size()).getParent()).removeView(mViews
                        .get(position % mViews.size()));
                container.addView(mViews.get(position % mViews.size()));
            }
            return mViews.get(position % mViews.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.v(TAG, "destroyItem" + position);
        }

    }

    /**
     * 初始化底部圆点
     */
    private void initDots() {
        ViewGroup dotGroup =(ViewGroup) mainView.findViewById(R.id.ll_dot);
        dots = new ImageView[flashList.size()];
        for (int i = 0; i < flashList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams;
            if(getActivity().getWindowManager().getDefaultDisplay().getWidth() <= 480) {
                layoutParams = new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(12, 12));
            } else if(getActivity().getWindowManager().getDefaultDisplay().getWidth() > 480 && getActivity().getWindowManager().getDefaultDisplay().getWidth() <= 720){
                layoutParams = new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(16, 16));
            } else {
                layoutParams = new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(20, 20));
            }
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            imageView.setLayoutParams(layoutParams);
            dots[i] = imageView;
            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.radiobutton_selected);
            } else {
                dots[i].setBackgroundResource(R.drawable.radiobutton_unselected);
            }
            dotGroup.addView(dots[i]);
        }
    }

    class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    isContinue = true;
                    break;
            }
            return false;
        }

    }

    /**
     * ViewPager的监听器
     */
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setCurrentDot(position % mViews.size());
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > flashList.size() - 1) {
            return;
        }
        for(int i = 0; i < dots.length; i++){
            if(i == position){
                dots[i].setBackgroundResource(R.drawable.radiobutton_selected);
            }else{
                dots[i].setBackgroundResource(R.drawable.radiobutton_unselected);
            }
        }

    }

    private void findViews(View view) {
        rlTitleBar = (RelativeLayout) view.findViewById(R.id.rl_title_bar_home_page);
        tvHousePropertyName = (TextView) view.findViewById(R.id.tv_house_property_name_home_page);
        tvUserType = (TextView) view.findViewById(R.id.tv_user_type_home_page);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name_home_page);
        tvTemperature = (TextView) view.findViewById(R.id.tv_temperature_home_page);
        tvWeather = (TextView) view.findViewById(R.id.tv_weather_home_page);
        ivWeatherIcon = (ImageView) view.findViewById(R.id.iv_weather_icon);
        llMostBeautifulPhoto = (LinearLayout) view.findViewById(R.id.ll_most_beautiful_photo_home_page);
        llMicroFruit = (LinearLayout) view.findViewById(R.id.ll_micro_fruit_home_page);
        llRepair = (LinearLayout) view.findViewById(R.id.ll_need_repair_home_page);
        llCarManagement = (LinearLayout) view.findViewById(R.id.ll_car_management_home_page);
        llOnlinePayment = (LinearLayout) view.findViewById(R.id.ll_online_payment_home_page);
        llLifeService = (LinearLayout) view.findViewById(R.id.ll_life_service_home_page);
        llNotice = (LinearLayout) view.findViewById(R.id.ll_notice_home_page);
        llExpress = (LinearLayout) view.findViewById(R.id.ll_express_home_page);
        llContactProperty = (LinearLayout) view.findViewById(R.id.ll_contact_property_home_page);
        ivMostBeautifulPhoto = (ImageView) view.findViewById(R.id.iv_most_beautiful_photo_home_page);
        ivMicroFruit = (ImageView) view.findViewById(R.id.iv_micro_fruit_home_page);
        ivRepair = (ImageView) view.findViewById(R.id.iv_need_repair_home_page);
        ivCarManagement = (ImageView) view.findViewById(R.id.iv_car_management_home_page);
        ivOnlinePayment = (ImageView) view.findViewById(R.id.iv_online_payment_home_page);
        ivLifeService = (ImageView) view.findViewById(R.id.iv_life_service_home_page);
        ivNotice = (ImageView) view.findViewById(R.id.iv_notice_home_page);
        ivExpress = (ImageView) view.findViewById(R.id.iv_query_express_home_page);
        ivContactProperty = (ImageView) view.findViewById(R.id.iv_contact_property_home_page);
        tvLatestNoticeTitle = (TextView) view.findViewById(R.id.tv_latest_notice_title);
        tvShowTime = (TextView) view.findViewById(R.id.tv_show_time_home_page);
        tvCommunityName = (TextView) view.findViewById(R.id.tv_community_name_home_page);
        tvNewsTitle = (TextView) view.findViewById(R.id.tv_news_title_home_page);
        ivNewsPic = (ImageView) view.findViewById(R.id.iv_news_pic_home_page);
        rlLatestNotice = (RelativeLayout) view.findViewById(R.id.rl_latest_notice);
        llLatestNews = (LinearLayout) view.findViewById(R.id.ll_latest_news);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_images);
        refreshLayout = (PullRefreshLayout) view.findViewById(R.id.refresh_layout_home_page);
    }

    private void addListeners() {
        tvHousePropertyName.setOnClickListener(this);
        llMostBeautifulPhoto.setOnClickListener(this);
        llMicroFruit.setOnClickListener(this);
        llRepair.setOnClickListener(this);
        llCarManagement.setOnClickListener(this);
        llOnlinePayment.setOnClickListener(this);
        llLifeService.setOnClickListener(this);
        llNotice.setOnClickListener(this);
        llExpress.setOnClickListener(this);
        llContactProperty.setOnClickListener(this);
        rlLatestNotice.setOnClickListener(this);
        llLatestNews.setOnClickListener(this);
    }

    private void initTitleBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        rlTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
    }

    private void showRepairDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.repair_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        repairDialog = builder.create();
        view.setAnimation(AnimationUtils.loadAnimation(
                getActivity(), R.anim.slide_bottom_to_top));
        repairDialog.show();
        // 去除dialog本身的背景色
        repairDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        findViewsInDialog(view);
        addListenersForDialog();
    }

    private void addListenersForDialog() {
        llFamilyRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairDialog.dismiss();
                Intent intent = new Intent(getActivity(), FamilyRepairActivity.class);
                startActivity(intent);
            }
        });
        llCommonRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairDialog.dismiss();
                Intent intent = new Intent(getActivity(), CommonRepairActivity.class);
                startActivity(intent);
            }
        });
        tvCannotChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "不会选择", Toast.LENGTH_SHORT).show();
            }
        });
        tvQueryMyRepairRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairDialog.dismiss();
                Intent intent = new Intent(getActivity(), RepairRecordActivity.class);
                startActivity(intent);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairDialog.dismiss();
            }
        });
    }

    private void findViewsInDialog(View dialog) {
        llFamilyRepair = (LinearLayout) dialog.findViewById(R.id.ll_family_repair_dialog);
        llCommonRepair = (LinearLayout) dialog.findViewById(R.id.ll_common_repair_dialog);
        tvCannotChoose = (TextView) dialog.findViewById(R.id.tv_cannot_choose_dialog);
        tvQueryMyRepairRecord = (TextView) dialog.findViewById(R.id.tv_query_my_repair_record);
        tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel_dialog);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_house_property_name_home_page:
                showHousePropertyNameList();
                break;
            case R.id.ll_most_beautiful_photo_home_page:
                intent = new Intent(getActivity(), MostBeautifulPhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_micro_fruit_home_page:
                CustomViewUtil.createToast(getActivity(), "暂无此功能");
                break;
            case R.id.ll_need_repair_home_page:
                showRepairDialog();
                break;
            case R.id.ll_car_management_home_page:
                intent = new Intent(getActivity(), CarManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_online_payment_home_page:
                intent = new Intent(getActivity(), OnlinePaymentActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_life_service_home_page:
                CustomViewUtil.createToast(getActivity(), "暂无此功能");
                break;
            case R.id.ll_notice_home_page:
                intent = new Intent(getActivity(), NotificationAnnouncementActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_express_home_page:
                intent = new Intent(getActivity(), Express100Activity.class);
                startActivity(intent);
                break;
            case R.id.ll_contact_property_home_page:
                showContactPropertyDialog();
                break;
            case R.id.rl_latest_notice:
                intent = new Intent(getActivity(), NotificationAnnouncementDetailActivity.class);
                intent.putExtra("notice", notice);
                startActivity(intent);
                break;
            case R.id.ll_latest_news:
                intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
                break;
        }
    }

    private void showHousePropertyNameList() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.house_property_list, null);
        lvHousePropertyNameList = (ListView) view.findViewById(R.id.lv_house_property_name_list_home_page);
        housePropertyAdapter = new HousePropertyNameListAdapter(getActivity(), housePropertyList);
        lvHousePropertyNameList.setAdapter(housePropertyAdapter);
        lvHousePropertyNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyHouseProperty tempMHP = housePropertyList.get(position);
                if(defaultHousePropertyId.equals(tempMHP.getId() == null ? "" : tempMHP.getId())) {
                    housePropertyListDialog.dismiss();
                    return;
                }
                housePropertyList.remove(position);
                housePropertyList.add(0, tempMHP);
                housePropertyAdapter.notifyDataSetChanged();
                housePropertyListDialog.dismiss();
                tvHousePropertyName.setText(tempMHP.getCommunity_name());
                refreshUI(tempMHP.getId() == null ? "" : tempMHP.getId());
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        housePropertyListDialog = builder.create();
        housePropertyListDialog.show();
        // 去除dialog本身的背景色
        housePropertyListDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }

    private void refreshUI(final String eid) {
        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.news(myApp.getUser().getId(), eid);
                Message msg = mHandler.obtainMessage();
                msg.what = NEWS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void showContactPropertyDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.contact_property_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        contactPropertyDialog = builder.create();
        contactPropertyDialog.show();
        // 去除dialog本身的背景色
        contactPropertyDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        TextView tvCall = (TextView) view.findViewById(R.id.tv_call_contact_property_dialog);
        TextView tvCreateContact = (TextView) view.findViewById(R.id.tv_create_contact_property_dialog);
        TextView tvAddToContact = (TextView) view.findViewById(R.id.tv_add_contact_property_dialog);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:057588058110"));
                startActivity(intent);
            }
        });
        tvCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.dir/person");
                intent.setType("vnd.android.cursor.dir/contact");
                intent.setType("vnd.android.cursor.dir/raw_contact");
                intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, "057588058110");
                startActivity(intent);
            }
        });
        tvAddToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, "057588058110");
                intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, 3);
                startActivity(intent);
            }
        });
    }

    private void parseNewsResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                Gson gson = new Gson();
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    // 小区公告
                    if(resultObject.getString("notice").equals("")) {
                        rlLatestNotice.setVisibility(View.GONE);
                    } else {
                        rlLatestNotice.setVisibility(View.VISIBLE);
                        JSONObject noticeObject = resultObject.getJSONObject("notice");
                        notice = gson.fromJson(noticeObject.toString(), new TypeToken<NotificationAnnouncement>() {}.getType());
                        tvLatestNoticeTitle.setText(notice.getTitle());
                    }
                    // 新闻
                    if(resultObject.getString("data").equals("")) {
                        llLatestNews.setVisibility(View.GONE);
                    } else {
                        llLatestNews.setVisibility(View.VISIBLE);
                        JSONObject newsObject = resultObject.getJSONObject("data");
                        news = gson.fromJson(newsObject.toString(), new TypeToken<News>() {}.getType());
                        tvShowTime.setText(news.getShowtime());
                        tvCommunityName.setText(news.getCommunity_name());
                        tvNewsTitle.setText(news.getTitle());
                        if(news.getPic() != null && !news.getPic().equals("")) {
                            llLatestNews.removeView(ivNewsPic);
                            ivNewsPic = new ImageView(getActivity());
                            ivNewsPic.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(10, 10, 10, 10);
                            ivNewsPic.setLayoutParams(lp);  //image的布局方式
                            ivNewsPic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            System.out.println("pic.url=" + news.getPic());
                            String imgUrl = news.getPic();
                            Glide.with(getActivity()).load(imgUrl).asBitmap().into(ivNewsPic);
                            llLatestNews.addView(ivNewsPic);
                        } else {
                            ivNewsPic.setVisibility(View.GONE);
                        }
                    }
                    if(resultObject.has("flash") && !resultObject.isNull("flash")) {
                        if(flashList.size() == 0) {
                            JSONArray flashArray = resultObject.getJSONArray("flash");
                            flashList = gson.fromJson(flashArray.toString(), new TypeToken<List<Flash>>() {}.getType());
                            initViewPager();
                        }
                    }
                    housePropertyList.clear();
                    if(resultObject.has("defaultaddress") && !resultObject.isNull("defaultaddress")) {
                        JSONObject defaultAddressObject = resultObject.getJSONObject("defaultaddress");
                        MyHouseProperty mhp = gson.fromJson(defaultAddressObject.toString(), new TypeToken<MyHouseProperty>() {}.getType());
                        housePropertyList.add(mhp);
                        if(mhp.getIsdefault().equals("1")) {
                            myApp.setDefaultHouseProperty(mhp);
                            tvHousePropertyName.setText(mhp.getCommunity_name());
                            defaultHousePropertyId = (mhp.getId() == null ? "" : mhp.getId());
                        }
                    }
                    if(resultObject.has("address") && !resultObject.isNull("address")) {
                        JSONArray addressArray = resultObject.getJSONArray("address");
                        if(addressArray != null && addressArray.length() > 0) {
                            List<MyHouseProperty> tempList = gson.fromJson(addressArray.toString(), new TypeToken<List<MyHouseProperty>>() {}.getType());
                            housePropertyList.addAll(tempList);
                        }
                    }
                    for(int i = 0; i < housePropertyList.size(); i++) {
                        if(housePropertyList.get(i).getIsdefault().equals("1")) {
                            MyHouseProperty tempMHP = housePropertyList.get(i);
                            housePropertyList.remove(i);
                            housePropertyList.add(0, tempMHP);
                            tvHousePropertyName.setText(tempMHP.getCommunity_name());
                            myApp.setDefaultHouseProperty(tempMHP);
                            defaultHousePropertyId = (tempMHP.getId() == null ? "" : tempMHP.getId());
                            break;
                        }
                    }
                } else {
                    CustomViewUtil.createToast(getActivity(), resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseGetWeatherResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                JSONObject rObject = resultObject.getJSONObject("result");
                JSONObject dataObject = rObject.getJSONObject("data");
                JSONObject realtimeObject = dataObject.getJSONObject("realtime");
                JSONObject weatherObject = realtimeObject.getJSONObject("weather");
                String temperature = weatherObject.getString("temperature");
                String info = weatherObject.getString("info");
                System.out.println("temperature=" + temperature + ", info=" + info);
                tvTemperature.setText(temperature);
                tvWeather.setText(info);
                ivWeatherIcon.setImageResource(info.contains("晴") ? R.drawable.sunny : (info.contains("雨") ? R.drawable.rainy : (info.contains("多云") ? R.drawable.cloudy_dy : (info.contains("雪") ? R.drawable.snowy : (info.contains("阴") ? R.drawable.cloudy_y : R.drawable.thunder)))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(myApp.getDefaultHouseProperty() != null) {
            if(!defaultHousePropertyId.equals(myApp.getDefaultHouseProperty().getId() == null ? "" : myApp.getDefaultHouseProperty().getId())) {
                refreshUI(null);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 停止轮播
        play = false;
        timer.cancel();
        timer = null;
    }
}
