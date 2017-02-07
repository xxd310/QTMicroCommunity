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
import android.widget.*;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.adapter.CarListAdapter;
import com.zhihuitech.qtwsq.entity.Car;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import com.zhihuitech.qtwsq.util.MyListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.os.FileObserver.DELETE;
import static com.zhihuitech.qtwsq.activity.R.drawable.car;

/**
 * Created by Administrator on 2016/7/30.
 */
public class CarManagementActivity extends Activity {
    private LinearLayout llBack;
    private TextView tvExit;
    private LinearLayout llAddCar;
    private LinearLayout llNoCar;
    private MyListView lvCar;
    private List<Car> list = new ArrayList<>();
    private CarListAdapter adapter;

    private MyApplication myApp;

    private AlertDialog deleteConfirmDialog;

    private final int GET_CAR_LIST = 0;
    private final int DELETE = 1;

    private PullRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_management);
        initStatusBar();

        myApp = (MyApplication) getApplication();

        findViews();
        addListeners();

        new Thread(){
            @Override
            public void run() {
                String result = DataProvider.getCarList(myApp.getUser().getId());
                Message msg = handler.obtainMessage();
                msg.obj = result;
                msg.what = GET_CAR_LIST;
                handler.sendMessage(msg);
            }
        }.start();

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        refreshLayout.setColor(getResources().getColor(R.color.title_bar_bg));
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run() {
                        String result = DataProvider.getCarList(myApp.getUser().getId());
                        Message msg = handler.obtainMessage();
                        msg.obj = result;
                        msg.what = GET_CAR_LIST;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    private void addListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarManagementActivity.this, AddCarActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void findViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back_car_management);
        tvExit = (TextView) findViewById(R.id.tv_exit_car_management);
        llAddCar = (LinearLayout) findViewById(R.id.ll_add_car);
        llNoCar = (LinearLayout) findViewById(R.id.ll_no_car_car_management);
        lvCar = (MyListView) findViewById(R.id.lv_car_list_car_management);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.refresh_layout_car_management);
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

    public void showDeleteConfirmDialog(final int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.operation_confirm_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        deleteConfirmDialog = builder.create();
        deleteConfirmDialog.show();
        // 去除dialog本身的背景色
        deleteConfirmDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        TextView tvTip = (TextView) view.findViewById(R.id.tv_confirm_tip_confirm_dialog);
        tvTip.setText("确认删除？");
        Button btnConfirm = (Button) view.findViewById(R.id.btn_delete_confirm_dialog);
        Button btnCancel = (Button) view.findViewById(R.id.btn_delete_cancel_dialog);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmDialog.dismiss();
                CustomViewUtil.createProgressDialog(CarManagementActivity.this, getResources().getString(R.string.submitting_information));
                new Thread(){
                    @Override
                    public void run() {
                        String result = DataProvider.carDel(myApp.getUser().getId(), list.get(index).getId());
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
            CustomViewUtil.dismissDialog();
            switch (msg.what) {
                case GET_CAR_LIST:
                    refreshLayout.setRefreshing(false);
                    parseGetCarListResult((String) msg.obj);
                    break;
                case DELETE:
                    parseCarDelResult((String) msg.obj, msg.arg1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                new Thread(){
                    @Override
                    public void run() {
                        String result = DataProvider.getCarList(myApp.getUser().getId());
                        Message msg = handler.obtainMessage();
                        msg.obj = result;
                        msg.what = GET_CAR_LIST;
                        handler.sendMessage(msg);
                    }
                }.start();
                break;
        }
    }

    private void parseGetCarListResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {

                    JSONArray dataArray = resultObject.getJSONArray("data");
                    Gson gson = new Gson();
                    list = gson.fromJson(dataArray.toString(), new TypeToken<List<Car>>() {}.getType());
                    if(list != null && list.size() > 0) {
                        llNoCar.setVisibility(View.GONE);
                        adapter = new CarListAdapter(CarManagementActivity.this, list);
                        lvCar.setAdapter(adapter);
                    }
                } else {
                    llNoCar.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseCarDelResult(String result, int deleteIndex) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    list.remove(deleteIndex);
                    adapter.notifyDataSetChanged();
                    if(list.size() == 0) {
                        llNoCar.setVisibility(View.VISIBLE);
                    }
                } else {
                    CustomViewUtil.createToast(CarManagementActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
