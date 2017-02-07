package com.zhihuitech.qtwsq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.util.SystemBarTintManager;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ServiceFragment extends Fragment {

    private RelativeLayout rlTitleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service, container, false);
        rlTitleBar = (RelativeLayout) view.findViewById(R.id.rl_title_bar_service);
        initTitleBar();
        return view;
    }

    private void initTitleBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        rlTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
    }
}
