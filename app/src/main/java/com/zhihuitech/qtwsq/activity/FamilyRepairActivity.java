package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;
import com.zhihuitech.qtwsq.adapter.DialogListAdapter;
import com.zhihuitech.qtwsq.adapter.RepairPicListAdapter;
import com.zhihuitech.qtwsq.adapter.RepairProjectDialogListAdapter;
import com.zhihuitech.qtwsq.entity.DropDownItem;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.entity.RepairProject;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import com.zhihuitech.qtwsq.util.ImageUtils;
import com.zhihuitech.qtwsq.util.MyGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.gson.internal.bind.util.ISO8601Utils.format;

/**
 * Created by Administrator on 2016/7/30.
 */
public class FamilyRepairActivity extends Activity {
    private ImageView ivBack;
    private TextView tvRepairProject;
    private EditText etDetailDescription;
    private TextView tvDate;
    private TextView tvAddress;
    private MyGridView gvPics;
    private TextView tvSubmit;

    private List<RepairProject> repairProjectList = new ArrayList<>();
    private List<DropDownItem> dateList = new ArrayList<>();

    private final int GET_REPAIR = 1;
    private final int SUBMIT = 2;

    private RepairProject selectedRepairProject;

    private RepairPicListAdapter picAdapter;
    private List<Bitmap> bitmapList = new ArrayList<>();

    // class variables
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    private MyApplication myApp;

    private String detailDescription = "";
    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_repair);
        initStatusBar();
        myApp = (MyApplication) getApplication();

        findViews();
        addListeners();

        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.family(myApp.getUser().getId());
                Message msg = handler.obtainMessage();
                msg.what = GET_REPAIR;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
        initGridView();
        initDateList();
    }

    private void initGridView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_car_pic);
        bitmapList.add(bitmap);
        picAdapter = new RepairPicListAdapter(this, bitmapList);
        gvPics.setAdapter(picAdapter);
        gvPics.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    public void selectPic() {
        Intent intent = new Intent(FamilyRepairActivity.this, ImagesSelectorActivity.class);
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10);
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                recycleBitmap();
                bitmapList.clear();
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
                for(int i = 0; i <mResults.size(); i++) {
                    Bitmap bm = ImageUtils.compressBySize(mResults.get(i), 480, 720);
                    bitmapList.add(bm);
                }
                bitmapList.add(null);
                picAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void recycleBitmap() {
        for(Bitmap bitmap : bitmapList) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_REPAIR:
                    parseGetRepairResult((String) msg.obj);
                    break;
            }
        }
    };

    private void parseGetRepairResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    JSONArray dataArray = resultObject.getJSONArray("data");
                    Gson gson = new Gson();
                    repairProjectList = gson.fromJson(dataArray.toString(), new TypeToken<List<RepairProject>>() {}.getType());
                    tvAddress.setText(resultObject.getString("address"));
                } else {
                    CustomViewUtil.createToast(FamilyRepairActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyRepairActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.please_choose_date));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(FamilyRepairActivity.this, dateList);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                tvDate.setText(dateList.get(position).getName());
                tvDate.setTextColor(Color.BLACK);
            }
        });
    }

    private void showRepairProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyRepairActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.please_choose_your_repair_project));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        RepairProjectDialogListAdapter adapter = new RepairProjectDialogListAdapter(FamilyRepairActivity.this, repairProjectList);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if(selectedRepairProject != null && selectedRepairProject.getId().equals(repairProjectList.get(position).getId())) {
                    return;
                }
                selectedRepairProject = repairProjectList.get(position);
                tvRepairProject.setText(selectedRepairProject.getName());
                tvRepairProject.setTextColor(Color.BLACK);
            }
        });
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_family_repair);
        tvRepairProject = (TextView) findViewById(R.id.tv_choose_repair_project_family_repair);
        etDetailDescription = (EditText) findViewById(R.id.et_detail_description_family_repair);
        tvDate = (TextView) findViewById(R.id.tv_repair_date_family_repair);
        tvAddress = (TextView) findViewById(R.id.tv_repair_address_family_repair);
        gvPics = (MyGridView) findViewById(R.id.gv_pics_family_repair);
        tvSubmit = (TextView) findViewById(R.id.tv_submit_family_repair);
    }

    private void addListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRepairProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepairProjectDialog();
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initDateList() {
        dateList.clear();
        DropDownItem ddi;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH");
        if(Integer.parseInt(sdf.format(c.getTime()).substring(sdf.format(c.getTime()).length() - 2)) < 12) {
            ddi = new DropDownItem();
            ddi.setName(sdf.format(c.getTime()).substring(0, 6) + getResources().getString(R.string.morning));
            dateList.add(ddi);
            ddi = new DropDownItem();
            ddi.setName(sdf.format(c.getTime()).substring(0, 6) + getResources().getString(R.string.afternoon));
            dateList.add(ddi);
        } else {
            ddi = new DropDownItem();
            ddi.setName(sdf.format(c.getTime()).substring(0, 6) + getResources().getString(R.string.afternoon));
            dateList.add(ddi);
        }
        for(int i = 1; i < 8; i++) {
            if(dateList.size() >= 14) {
                break;
            }
            c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
            ddi = new DropDownItem();
            ddi.setName(sdf.format(c.getTime()).substring(0, 6) + getResources().getString(R.string.morning));
            dateList.add(ddi);
            if(dateList.size() >= 14) {
                break;
            }
            ddi = new DropDownItem();
            ddi.setName(sdf.format(c.getTime()).substring(0, 6) + getResources().getString(R.string.afternoon));
            dateList.add(ddi);
        }
        tvDate.setText(dateList.get(0).getName());
    }

}
