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
import com.zhihuitech.qtwsq.entity.RepairProject;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import com.zhihuitech.qtwsq.util.ImageUtils;
import com.zhihuitech.qtwsq.util.MyGridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zhihuitech.qtwsq.activity.R.drawable.add;

/**
 * Created by Administrator on 2016/7/30.
 */
public class CommonRepairActivity extends Activity {
    private ImageView ivBack;
    private TextView tvRepairProject;
    private TextView tvRepairProjectDetail;
    private EditText etDetailDescription;
    private TextView tvAddress;
    private EditText etDetailAddress;
    private MyGridView gvPics;
    private TextView tvSubmit;

    private final int GET_REPAIR = 1;
    private final int GET_REPAIR2 = 2;
    private final int SUBMIT = 3;

    private List<RepairProject> repairProjectList = new ArrayList<>();
    private RepairProject selectedRepairProject;
    private List<DropDownItem> repairProjectDetailList = new ArrayList<>();
    private DropDownItem selectedRepairProjectDetail;

    private List<DropDownItem> addressList = new ArrayList<>();
    private DropDownItem selectedAddress;

    private RepairPicListAdapter picAdapter;
    private List<Bitmap> bitmapList = new ArrayList<>();

    // class variables
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    private MyApplication myApp;

    private String detailDescription = "";
    private String detailAddress = "";
    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_repair);
        initStatusBar();

        myApp = (MyApplication) getApplication();

        findViews();
        addListeners();

        initAddressList();
        new Thread() {
            @Override
            public void run() {
                String result = DataProvider.common();
                Message msg = handler.obtainMessage();
                msg.what = GET_REPAIR;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();

        initGridView();
    }

    private void initGridView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_car_pic);
        bitmapList.add(bitmap);
        picAdapter = new RepairPicListAdapter(this, bitmapList);
        gvPics.setAdapter(picAdapter);
        gvPics.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    public void selectPic() {
        Intent intent = new Intent(CommonRepairActivity.this, ImagesSelectorActivity.class);
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
                case GET_REPAIR2:
                    parseGetRepair2Result((String) msg.obj);
                    break;
            }
        }
    };

    private void initAddressList() {
        addressList.clear();
        addressList.add(new DropDownItem("", myApp.getUser().getAddress()));
        addressList.add(new DropDownItem("", "其他"));
        selectedAddress = addressList.get(0);
        tvAddress.setText(addressList.get(0).getName());
        tvAddress.setTextColor(Color.BLACK);
    }

    private void parseGetRepair2Result(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    JSONArray dataArray = resultObject.getJSONArray("data");
                    Gson gson = new Gson();
                    repairProjectDetailList = gson.fromJson(dataArray.toString(), new TypeToken<List<DropDownItem>>() {}.getType());
                    if(repairProjectDetailList.size() > 0) {
                        tvRepairProjectDetail.setVisibility(View.VISIBLE);
                    } else {
                        tvRepairProjectDetail.setVisibility(View.GONE);
                    }
                } else {
                    CustomViewUtil.createToast(CommonRepairActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseGetRepairResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getInt("status") == 1) {
                    JSONArray dataArray = resultObject.getJSONArray("data");
                    Gson gson = new Gson();
                    repairProjectList = gson.fromJson(dataArray.toString(), new TypeToken<List<RepairProject>>() {}.getType());
                } else {
                    CustomViewUtil.createToast(CommonRepairActivity.this, resultObject.getString("sign"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRepairProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonRepairActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.please_choose_your_repair_project_type));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        RepairProjectDialogListAdapter adapter = new RepairProjectDialogListAdapter(CommonRepairActivity.this, repairProjectList);
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
                tvRepairProjectDetail.setText(getResources().getString(R.string.please_choose_your_repair_project_detail));
                tvRepairProjectDetail.setTextColor(Color.GRAY);
                selectedRepairProjectDetail = null;

                new Thread() {
                    @Override
                    public void run() {
                        String result = DataProvider.getRepair2(selectedRepairProject.getId());
                        Message msg = handler.obtainMessage();
                        msg.what = GET_REPAIR2;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    private void showRepairProjectDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonRepairActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.please_choose_your_repair_project_detail));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(CommonRepairActivity.this, repairProjectDetailList);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                selectedRepairProjectDetail = repairProjectDetailList.get(position);
                tvRepairProjectDetail.setText(selectedRepairProjectDetail.getName());
                tvRepairProjectDetail.setTextColor(Color.BLACK);
            }
        });
    }

    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonRepairActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.please_choose_address));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(CommonRepairActivity.this, addressList);
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
                selectedAddress = addressList.get(position);
                tvAddress.setText(selectedAddress.getName());
                tvAddress.setTextColor(Color.BLACK);
            }
        });
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_common_repair);
        tvRepairProject = (TextView) findViewById(R.id.tv_choose_repair_project_common_repair);
        tvRepairProjectDetail = (TextView) findViewById(R.id.tv_choose_repair_project_detail_common_repair);
        etDetailDescription = (EditText) findViewById(R.id.et_detail_description_common_repair);
        tvAddress = (TextView) findViewById(R.id.tv_repair_address_common_repair);
        etDetailAddress = (EditText) findViewById(R.id.et_detail_address_common_repair);
        gvPics = (MyGridView) findViewById(R.id.gv_pics_common_repair);
        tvSubmit = (TextView) findViewById(R.id.tv_submit_common_repair);
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
                if(repairProjectList == null || repairProjectList.size() == 0) {
                    CustomViewUtil.createToast(CommonRepairActivity.this, getResources().getString(R.string.no_repair_project_type_to_choose));
                } else {
                    showRepairProjectDialog();
                }
            }
        });
        tvRepairProjectDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repairProjectDetailList == null || repairProjectDetailList.size() == 0) {
                    CustomViewUtil.createToast(CommonRepairActivity.this, getResources().getString(R.string.no_repair_project_detail_to_choose));
                } else {
                    showRepairProjectDetailDialog();
                }
            }
        });
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddressDialog();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedRepairProject == null) {
                    CustomViewUtil.createToast(CommonRepairActivity.this, getResources().getString(R.string.please_choose_your_repair_project_type));
                    return;
                }
                if(selectedRepairProjectDetail == null) {
                    CustomViewUtil.createToast(CommonRepairActivity.this, getResources().getString(R.string.please_choose_your_repair_project_detail));
                    return;
                }
                detailDescription = etDetailDescription.getText().toString();
                if(detailDescription.equals("")) {
                    CustomViewUtil.createToast(CommonRepairActivity.this, getResources().getString(R.string.please_input_detail_description));
                    return;
                }
                detailAddress = etDetailAddress.getText().toString();
                if(detailAddress.equals("")) {
                    CustomViewUtil.createToast(CommonRepairActivity.this, getResources().getString(R.string.please_input_detail_address));
                    return;
                }
                StringBuffer sb = new StringBuffer();
                if(mResults != null && mResults.size() > 0) {
                    for(String path : mResults) {
                        sb.append(ImageUtils.encode(path) + ",");
                    }
                    imgUrl = sb.substring(0, sb.length() - 1);
                }
                new Thread() {
                    @Override
                    public void run() {
                        //id, String type, String entry_id, String entry_name, String entry_id2, String entry_name2, String booktime, String xiaoqu, String address, String info, String imgurl) {
                        String result = DataProvider.serviceSubmit(myApp.getUser().getId(), "2", selectedRepairProject.getId(), selectedRepairProject.getName(), selectedRepairProjectDetail.getId(), selectedRepairProjectDetail.getName(), "", "", detailAddress, detailDescription, imgUrl);
                        Message msg = handler.obtainMessage();
                        msg.what = SUBMIT;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
