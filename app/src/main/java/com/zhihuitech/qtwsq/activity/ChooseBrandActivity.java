package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.adapter.CarBrandListAdapter;
import com.zhihuitech.qtwsq.entity.Car;
import com.zhihuitech.qtwsq.entity.CarBrand;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class ChooseBrandActivity extends Activity {
    private ImageView ivBack;
    private TextView tvExit;
    private ExpandableListView elvBrand;
    private CarBrandListAdapter adapter;
    private ArrayList<String> groupList = new ArrayList<>();
    private ArrayList<ArrayList<CarBrand>> childList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_brand);
        initStatusBar();
        
        findViews();

        addListeners();

        MyTask task = new MyTask();
        task.execute();
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
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
    }

    private void setContentForList() {
        // 去掉默认的箭头
        elvBrand.setGroupIndicator(null);
        // 屏蔽group的点击事件，使组始终展开
        elvBrand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        adapter = new CarBrandListAdapter(groupList, childList, ChooseBrandActivity.this);
        elvBrand.setAdapter(adapter);
        // 默认展开所有的组
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            elvBrand.expandGroup(i);
        }
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_choose_brand);
        tvExit = (TextView) findViewById(R.id.tv_exit_choose_brand);
        elvBrand = (ExpandableListView) findViewById(R.id.elv_brand_list);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void returnResult(CarBrand selectedCarBrand) {
        Intent intent = getIntent();
        intent.putExtra("brand", selectedCarBrand);
        setResult(RESULT_OK, intent);
        finish();
    }

    class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = DataProvider.getCarBrand();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parseGetCarBrandResult(result);
        }
    }

    private void parseGetCarBrandResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                Gson gson = new Gson();
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    JSONArray dataArray = resultObject.getJSONArray("data");
                    for(int i = 0; i < dataArray.length(); i++) {
                        JSONObject groupObject = dataArray.getJSONObject(i);
                        groupList.add(groupObject.getString("key"));
                        JSONArray listArray = groupObject.getJSONArray("list");
                        ArrayList<CarBrand> list = gson.fromJson(listArray.toString(), new TypeToken<ArrayList<CarBrand>>() {}.getType());
                        childList.add(list);
                    }
                    setContentForList();
                } else {
                    CustomViewUtil.createToast(ChooseBrandActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
