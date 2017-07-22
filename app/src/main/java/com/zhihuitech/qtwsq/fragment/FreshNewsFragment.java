package com.zhihuitech.qtwsq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.zhihuitech.qtwsq.activity.FreshNewsDetailActivity;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;

/**
 * Created by Administrator on 2016/7/30.
 */
public class FreshNewsFragment extends Fragment {
    private View rootView;
    private RelativeLayout rlTitleBar;
    private ImageView ivTopFreshNews;
    private ImageView ivInterestTribe;
    private ImageView ivSxLibrary;
    private ImageView ivQtTeaLeaf;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fresh_news, container, false);
        findViews();
        initTitleBar();
        addListeners();
        return rootView;
    }

    private void addListeners() {
        ivTopFreshNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ivInterestTribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ivSxLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ivQtTeaLeaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void findViews() {
        rlTitleBar = (RelativeLayout) rootView.findViewById(R.id.rl_title_bar_fresh_news);
        ivTopFreshNews = (ImageView) rootView.findViewById(R.id.iv_top_fresh_news);
        ivInterestTribe = (ImageView) rootView.findViewById(R.id.iv_interest_tribe_fresh_news);
        ivSxLibrary = (ImageView) rootView.findViewById(R.id.iv_sx_library_fresh_news);
        ivQtTeaLeaf = (ImageView) rootView.findViewById(R.id.iv_qt_tea_leaf_fresh_news);
    }

    private void initTitleBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        rlTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
    }
}
