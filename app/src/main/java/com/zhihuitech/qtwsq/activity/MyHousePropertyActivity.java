package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.adapter.MyHousePropertyListAdapter;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class MyHousePropertyActivity extends Activity {
    private ListView lvMyHouseProperty;
    private MyHousePropertyListAdapter adapter;
    private List<MyHouseProperty> list = new ArrayList<>();
    private ImageView ivBack;
    private TextView tvAdd;
    private String uid;
    private MyApplication myApp;

    private final static int USER_ESTATE = 0;
    private final static int DELETE = 1;
    private final static int SET_DEFAULT = 2;

    private AlertDialog deleteConfirmDialog;
    private AlertDialog setDefaultConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_house_preperty);
        myApp = (MyApplication) this.getApplication();
        uid = myApp.getUser().getId();
        initStatusBar();
        findViews();
        addListeners();

        new Thread(){
            @Override
            public void run() {
                String result = DataProvider.userEstate(uid);
                Message msg = handler.obtainMessage();
                msg.obj = result;
                msg.what = USER_ESTATE;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHousePropertyActivity.this, AddHousePropertyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews(){
        lvMyHouseProperty = (ListView) findViewById(R.id.lv_my_house_property);
        ivBack = (ImageView) findViewById(R.id.iv_back_my_house_property);
        tvAdd = (TextView) findViewById(R.id.tv_add_my_house_property);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//            llTitleBar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        }
    }

    public void showSetDefaultConfirmDialog(final int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.operation_confirm_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        setDefaultConfirmDialog = builder.create();
        setDefaultConfirmDialog.show();
        // 去除dialog本身的背景色
        setDefaultConfirmDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        TextView tvTip = (TextView) view.findViewById(R.id.tv_confirm_tip_confirm_dialog);
        tvTip.setText(getResources().getString(R.string.set_default_address));
        Button btnConfirm = (Button) view.findViewById(R.id.btn_delete_confirm_dialog);
        Button btnCancel = (Button) view.findViewById(R.id.btn_delete_cancel_dialog);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultConfirmDialog.dismiss();
                CustomViewUtil.createProgressDialog(MyHousePropertyActivity.this, getResources().getString(R.string.submitting_information));
                new Thread(){
                    @Override
                    public void run() {
                        String result = DataProvider.userEstateSetDefault(uid, list.get(index).getId() == null ? "" : list.get(index).getId());
                        Message msg = handler.obtainMessage();
                        msg.obj = result;
                        msg.what = SET_DEFAULT;
                        msg.arg1 = index;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultConfirmDialog.dismiss();
            }
        });
    }

    public void showDeleteConfirmDialog(final int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.operation_confirm_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        deleteConfirmDialog = builder.create();
        deleteConfirmDialog.show();
        // 去除dialog本身的背景色
        deleteConfirmDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        TextView tvTip = (TextView) view.findViewById(R.id.tv_confirm_tip_confirm_dialog);
        tvTip.setText(getResources().getString(R.string.delete_house_property));
        Button btnConfirm = (Button) view.findViewById(R.id.btn_delete_confirm_dialog);
        Button btnCancel = (Button) view.findViewById(R.id.btn_delete_cancel_dialog);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmDialog.dismiss();
                CustomViewUtil.createProgressDialog(MyHousePropertyActivity.this, getResources().getString(R.string.submitting_information));
                new Thread(){
                    @Override
                    public void run() {
                        String result = DataProvider.userEstateDelete(uid, list.get(index).getId());
                        Message msg = handler.obtainMessage();
                        msg.obj = result;
                        msg.what = DELETE;
                        msg.arg1 = index;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmDialog.dismiss();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CustomViewUtil.dismissDialog();
            switch (msg.what) {
                case USER_ESTATE:
                    parseUserEstateResult((String) msg.obj);
                    break;
                case DELETE:
                    parseDeleteResult((String) msg.obj, msg.arg1);
                    break;
                case SET_DEFAULT:
                    parseSetDefaultResult((String) msg.obj, msg.arg1);
                    break;
            }
        }
    };

    private void parseSetDefaultResult(String result, int position) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    for (int i = 0; i < list.size(); i++) {
                        if(i == position) {
                            myApp.setDefaultHouseProperty(list.get(i));
                            list.get(i).setIsdefault("1");
                        } else {
                            list.get(i).setIsdefault("0");
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    CustomViewUtil.createToast(getApplicationContext(), resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseDeleteResult(String result, int position) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    CustomViewUtil.createToast(getApplicationContext(), resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseUserEstateResult(String result) {
        try {
            Gson gson = new Gson();
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    if(resultObject.has("mainaddress") && !resultObject.isNull("mainaddress")) {
                        JSONObject mainAddressObject = resultObject.getJSONObject("mainaddress");
                        MyHouseProperty mhp = gson.fromJson(mainAddressObject.toString(), new TypeToken<MyHouseProperty>() {}.getType());
                        list.add(mhp);
                    }
                    if(resultObject.has("address") && !resultObject.isNull("address")) {
                        JSONArray addressArray = resultObject.getJSONArray("address");
                        List<MyHouseProperty> tempList = gson.fromJson(addressArray.toString(), new TypeToken<List<MyHouseProperty>>() {}.getType());
                        list.addAll(tempList);
                    }
                    adapter = new MyHousePropertyListAdapter(MyHousePropertyActivity.this, list);
                    lvMyHouseProperty.setAdapter(adapter);
                } else {
                    CustomViewUtil.createToast(getApplicationContext(), resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
