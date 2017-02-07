package com.zhihuitech.qtwsq.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zhihuitech.qtwsq.activity.*;
import com.zhihuitech.qtwsq.entity.User;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;

import static com.zhihuitech.qtwsq.activity.R.drawable.add;

/**
 * Created by Administrator on 2016/7/30.
 */
public class PersonalCenterFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout rlTitleBar;
    private ImageView ivPortrait;
    private TextView tvRealName;
    private TextView tvAddress;
    private TextView tvNotAuthenticated;
    private TextView tvGoToAuthentication;

    private LinearLayout llMyInformation;
    private LinearLayout llMyHouseProperty;
    private LinearLayout llWallet;
    private LinearLayout llScore;
    private LinearLayout llPayRecord;
    private LinearLayout llShoppingList;
    private LinearLayout llSuggestion;
    private LinearLayout llSetting;
    private LinearLayout llContactUs;

    private MyApplication myApp;
    private User user;
    private Drawable sexDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myApp = (MyApplication) getActivity().getApplication();
        user = myApp.getUser();
        View view = inflater.inflate(R.layout.personal_center, container, false);
        findViews(view);
        addListeners();
        initTitleBar();
        initData();
        return view;
    }

    private void initData() {
        Glide.with(getActivity()).load(user.getPortrait()).asBitmap().into(ivPortrait);
        tvRealName.setText(user.getRealname());
        tvAddress.setText(user.getAddress());
        if(user.getIsverify().equals("1")) {
            tvNotAuthenticated.setVisibility(View.GONE);
            tvGoToAuthentication.setVisibility(View.GONE);
        } else if(user.getIsverify().equals("0")) {
            tvAddress.setVisibility(View.GONE);
        } else if(user.getIsverify().equals("-1")){
            tvAddress.setVisibility(View.GONE);
            tvGoToAuthentication.setVisibility(View.GONE);
        }
        if(user.getSex().equals("1")) {
            sexDrawable = getResources().getDrawable(R.drawable.male);
        } else {
            sexDrawable = getResources().getDrawable(R.drawable.female);
        }
        sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
        tvRealName.setCompoundDrawables(null, null, sexDrawable, null);
        tvRealName.setCompoundDrawablePadding(10);
    }

    private void findViews(View parent) {
        rlTitleBar = (RelativeLayout) parent.findViewById(R.id.rl_title_bar_personal_center);
        ivPortrait = (ImageView) parent.findViewById(R.id.iv_portrait_personal_center);
        tvRealName = (TextView) parent.findViewById(R.id.tv_realname_personal_center);
        tvAddress = (TextView) parent.findViewById(R.id.tv_address_personal_center);
        tvNotAuthenticated = (TextView) parent.findViewById(R.id.tv_not_authenticate_personal_center);
        tvGoToAuthentication = (TextView) parent.findViewById(R.id.tv_go_to_authenticate_personal_center);
        llMyInformation = (LinearLayout) parent.findViewById(R.id.ll_my_information_personal_center);
        llMyHouseProperty = (LinearLayout) parent.findViewById(R.id.ll_my_house_property_personal_center);
        llWallet = (LinearLayout) parent.findViewById(R.id.ll_wallet_personal_center);
        llScore = (LinearLayout) parent.findViewById(R.id.ll_score_personal_center);
        llPayRecord = (LinearLayout) parent.findViewById(R.id.ll_pay_record_personal_center);
        llShoppingList = (LinearLayout) parent.findViewById(R.id.ll_shopping_list_personal_center);
        llSuggestion = (LinearLayout) parent.findViewById(R.id.ll_suggestion_personal_center);
        llSetting = (LinearLayout) parent.findViewById(R.id.ll_setting_personal_center);
        llContactUs = (LinearLayout) parent.findViewById(R.id.ll_contact_us_personal_center);
    }

    private void addListeners() {
        tvGoToAuthentication.setOnClickListener(this);
        llMyInformation.setOnClickListener(this);
        llMyHouseProperty.setOnClickListener(this);
        llWallet.setOnClickListener(this);
        llScore.setOnClickListener(this);
        llPayRecord.setOnClickListener(this);
        llShoppingList.setOnClickListener(this);
        llSuggestion.setOnClickListener(this);
        llSetting.setOnClickListener(this);
        llContactUs.setOnClickListener(this);
    }

    private void initTitleBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        rlTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_go_to_authenticate_personal_center:
                intent = new Intent(getActivity(), UserAuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_house_property_personal_center:
                intent = new Intent(getActivity(), MyHousePropertyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting_personal_center:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                myApp.addActivity(getActivity());
                break;
            case R.id.ll_contact_us_personal_center:
                intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
