package com.zhihuitech.qtwsq.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhihuitech.qtwsq.activity.MyApplication;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.provider.DataProvider;

/**
 * Created by Administrator on 2016/7/30.
 */
public class CommonRepairRecordFragment extends Fragment {
    private final int COMMON_RECORD = 1;
    private MyApplication myApp;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_repair_record, container, false);
        myApp = (MyApplication) getActivity().getApplication();

        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.records(myApp.getUser().getId(), "2");
                Message msg = handler.obtainMessage();
                msg.what = COMMON_RECORD;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMMON_RECORD:
                    parseCommonRecordResult((String) msg.obj);
                    break;
            }
        }
    };

    private void parseCommonRecordResult(String result) {

    }
}
