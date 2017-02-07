package com.zhihuitech.qtwsq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuitech.qtwsq.adapter.DialogListAdapter;
import com.zhihuitech.qtwsq.entity.DropDownItem;
import com.zhihuitech.qtwsq.provider.DataProvider;
import com.zhihuitech.qtwsq.util.CustomViewUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/7/30.
 */
public class AddHousePropertyActivity extends Activity implements View.OnClickListener {
    private LinearLayout llBack;
    private Button btnCommunity;
    private Button btnRegion;
    private Button btnBuildingNumber;
    private Button btnUnit;
    private Button btnRoomNumber;
    private Button btnUserType;
    private Button btnSubmit;

    private static int SUBMIT_INFO = 0;
    private static int GET_COMMUNITY_LIST = 1;
    private static int GET_REGION_LIST = 2;
    private static int GET_BUILDING_LIST = 3;
    private static int GET_UNIT_LIST = 4;
    private static int GET_ROOM_LIST = 5;
    private int currentOperation = GET_COMMUNITY_LIST;

    private List<DropDownItem> communityList = new ArrayList<>();
    private List<DropDownItem> regionList = new ArrayList<>();
    private List<DropDownItem> buildingList = new ArrayList<>();
    private List<DropDownItem> unitList = new ArrayList<>();
    private List<DropDownItem> roomList = new ArrayList<>();
    private List<DropDownItem> userTypeList = new ArrayList<>();

    private DialogListAdapter userTypeAdapter;

    private String selectedCommunityId = "";
    private String selectedRegionId = "";
    private String selectedBuildingId = "";
    private String selectedUnitId = "";
    private String selectedRoomId = "";
    private String selectedUserTypeId = "";

    private String uid;
    private StringBuffer sbAddress;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_house_property);
        initStatusBar();

        myApp = (MyApplication) this.getApplication();
        uid = myApp.getUser().getId();

        findViews();
        addListeners();

        initData();

        MyTask task = new MyTask();
        task.execute();
    }

    private void initData() {
        userTypeList = DataProvider.getUserTypeList();
        userTypeAdapter = new DialogListAdapter(AddHousePropertyActivity.this, userTypeList, 6);
    }

    private void findViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back_add_house_property);
        btnCommunity = (Button) findViewById(R.id.btn_community_add_house_property);
        btnRegion = (Button) findViewById(R.id.btn_region_add_house_property);
        btnBuildingNumber = (Button) findViewById(R.id.btn_building_number_add_house_property);
        btnUnit = (Button) findViewById(R.id.btn_unit_add_house_property);
        btnRoomNumber = (Button) findViewById(R.id.btn_room_number_add_house_property);
        btnUserType = (Button) findViewById(R.id.btn_user_type_add_house_property);
        btnSubmit = (Button) findViewById(R.id.btn_submit_add_house_property);
    }

    private void addListeners() {
        llBack.setOnClickListener(this);
        btnCommunity.setOnClickListener(this);
        btnRegion.setOnClickListener(this);
        btnBuildingNumber.setOnClickListener(this);
        btnUnit.setOnClickListener(this);
        btnRoomNumber.setOnClickListener(this);
        btnUserType.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back_add_house_property:
                finish();
                break;
            case R.id.btn_community_add_house_property:
                if(communityList == null || communityList.size() == 0) {
                    CustomViewUtil.createToast(AddHousePropertyActivity.this, getResources().getString(R.string.no_community_to_choose));
                    return;
                }
                showCommunityDialog();
                break;
            case R.id.btn_region_add_house_property:
                if(regionList == null || regionList.size() == 0) {
                    CustomViewUtil.createToast(AddHousePropertyActivity.this, getResources().getString(R.string.no_region_to_choose));
                    return;
                }
                showRegionDialog();
                break;
            case R.id.btn_building_number_add_house_property:
                if(buildingList == null || buildingList.size() == 0) {
                    CustomViewUtil.createToast(AddHousePropertyActivity.this, getResources().getString(R.string.no_building_to_choose));
                    return;
                }
                showBuildingDialog();
                break;
            case R.id.btn_unit_add_house_property:
                if(unitList == null || unitList.size() == 0) {
                    CustomViewUtil.createToast(AddHousePropertyActivity.this, getResources().getString(R.string.no_unit_to_choose));
                    return;
                }
                showUnitDialog();
                break;
            case R.id.btn_room_number_add_house_property:
                if(roomList == null || roomList.size() == 0) {
                    CustomViewUtil.createToast(AddHousePropertyActivity.this, getResources().getString(R.string.no_room_to_choose));
                    return;
                }
                showRoomDialog();
                break;
            case R.id.btn_user_type_add_house_property:
                showUserTypeDialog();
                break;
            case R.id.btn_submit_add_house_property:
                currentOperation = SUBMIT_INFO;
                submitInformation();
                break;
        }
    }

    private void submitInformation() {
        sbAddress = new StringBuffer();
        String community = btnCommunity.getText().toString();
        String region = btnRegion.getText().toString();
        String buildingNumber = btnBuildingNumber.getText().toString();
        String unit = btnUnit.getText().toString();
        String roomNumber = btnRoomNumber.getText().toString();
        String userType = btnUserType.getText().toString();
        if(community.equals(getResources().getString(R.string.choose_community)) && communityList.size() != 0) {
            Toast.makeText(AddHousePropertyActivity.this, getResources().getString(R.string.please_choose_community), Toast.LENGTH_SHORT).show();
            return;
        } else {
            sbAddress.append((community.equals(getResources().getString(R.string.no_community)) || selectedCommunityId.equals("")) ? "" : community);
        }
        if(region.equals(getResources().getString(R.string.choose_region)) && regionList.size() != 0) {
            Toast.makeText(AddHousePropertyActivity.this,getResources().getString(R.string.please_choose_region), Toast.LENGTH_SHORT).show();
            return;
        } else {
            sbAddress.append((region.equals(getResources().getString(R.string.no_region)) || selectedRegionId.equals("")) ? "" : region);
        }
        if(buildingNumber.equals(getResources().getString(R.string.choose_building_number)) && buildingList.size() != 0) {
            Toast.makeText(AddHousePropertyActivity.this, getResources().getString(R.string.please_choose_building), Toast.LENGTH_SHORT).show();
            return;
        } else {
            sbAddress.append((buildingNumber.equals(getResources().getString(R.string.no_building)) || selectedBuildingId.equals("")) ? "" : buildingNumber);
        }
        if(unit.equals(getResources().getString(R.string.choose_unit)) && unitList.size() != 0) {
            Toast.makeText(AddHousePropertyActivity.this, getResources().getString(R.string.please_choose_unit), Toast.LENGTH_SHORT).show();
            return;
        } else {
            sbAddress.append((unit.equals(getResources().getString(R.string.no_unit)) || selectedUnitId.equals("")) ? "" : unit);
        }
        if(roomNumber.equals(getResources().getString(R.string.choose_room_number)) && roomList.size() != 0) {
            Toast.makeText(AddHousePropertyActivity.this, getResources().getString(R.string.please_choose_room), Toast.LENGTH_SHORT).show();
            return;
        } else {
            sbAddress.append((roomNumber.equals(getResources().getString(R.string.no_room)) || selectedRoomId.equals("")) ? "" : roomNumber);
        }
        if(userType.equals(getResources().getString(R.string.choose_user_type))) {
            Toast.makeText(AddHousePropertyActivity.this, getResources().getString(R.string.please_choose_user_type), Toast.LENGTH_SHORT).show();
            return;
        }
        currentOperation = SUBMIT_INFO;
        MyTask task = new MyTask();
        task.execute();
    }

    private void showCommunityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddHousePropertyActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_community));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(AddHousePropertyActivity.this, communityList, 1);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                btnCommunity.setText(communityList.get(position).getName());
                selectedCommunityId = communityList.get(position).getId();

                btnRegion.setEnabled(true);
                btnRegion.setText(getResources().getString(R.string.choose_region));
                regionList.clear();
                selectedRegionId = "";
                btnBuildingNumber.setEnabled(false);
                btnBuildingNumber.setText(getResources().getString(R.string.choose_building_number));
                buildingList.clear();
                selectedBuildingId = "";
                btnUnit.setEnabled(false);
                btnUnit.setText(getResources().getString(R.string.choose_unit));
                unitList.clear();
                selectedUnitId = "";
                btnRoomNumber.setEnabled(false);
                btnRoomNumber.setText(getResources().getString(R.string.choose_room_number));
                roomList.clear();
                selectedRoomId = "";

                currentOperation = GET_REGION_LIST;
                MyTask task = new MyTask();
                task.execute();
            }
        });
    }

    private void showRegionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddHousePropertyActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_region));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(AddHousePropertyActivity.this, regionList, 2);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                btnRegion.setText(regionList.get(position).getName());
                selectedRegionId = regionList.get(position).getId();

                btnBuildingNumber.setEnabled(true);
                btnBuildingNumber.setText(getResources().getString(R.string.choose_building_number));
                buildingList.clear();
                selectedBuildingId = "";
                btnUnit.setEnabled(false);
                btnUnit.setText(getResources().getString(R.string.choose_unit));
                unitList.clear();
                selectedUnitId = "";
                btnRoomNumber.setEnabled(false);
                btnRoomNumber.setText(getResources().getString(R.string.choose_room_number));
                roomList.clear();
                selectedRoomId = "";

                currentOperation = GET_BUILDING_LIST;
                MyTask task = new MyTask();
                task.execute();
            }
        });
    }

    private void showBuildingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddHousePropertyActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_building_number));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(AddHousePropertyActivity.this, buildingList, 3);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                btnBuildingNumber.setText(buildingList.get(position).getName());
                selectedBuildingId = buildingList.get(position).getId();

                btnUnit.setEnabled(true);
                btnUnit.setText(getResources().getString(R.string.choose_unit));
                unitList.clear();
                selectedUnitId = "";
                btnRoomNumber.setEnabled(false);
                btnRoomNumber.setText(getResources().getString(R.string.choose_room_number));
                roomList.clear();
                selectedRoomId = "";

                currentOperation = GET_UNIT_LIST;
                MyTask task = new MyTask();
                task.execute();
            }
        });
    }

    private void showUnitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddHousePropertyActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_unit));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(AddHousePropertyActivity.this, unitList, 4);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                btnUnit.setText(unitList.get(position).getName());
                selectedUnitId = unitList.get(position).getId();

                btnRoomNumber.setEnabled(true);
                btnRoomNumber.setText(getResources().getString(R.string.choose_room_number));
                roomList.clear();
                selectedRoomId = "";

                currentOperation = GET_ROOM_LIST;
                MyTask task = new MyTask();
                task.execute();
            }
        });
    }

    private void showRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddHousePropertyActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_room_number));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(AddHousePropertyActivity.this, roomList, 5);
        lv.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                btnRoomNumber.setText(roomList.get(position).getName());
                selectedRoomId = roomList.get(position).getId();
            }
        });
    }

    private void showUserTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddHousePropertyActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_user_type));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        lv.setAdapter(userTypeAdapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                btnUserType.setText(userTypeList.get(position).getName());
                selectedUserTypeId = userTypeList.get(position).getId();
            }
        });
    }


    class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(currentOperation == SUBMIT_INFO) {
                CustomViewUtil.createProgressDialog(AddHousePropertyActivity.this, getResources().getString(R.string.submitting_information));
            } else {
                CustomViewUtil.createProgressDialog(AddHousePropertyActivity.this, getResources().getString(R.string.obtaining_information));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            if(currentOperation == GET_COMMUNITY_LIST) {
                result = DataProvider.getDialogContent("", currentOperation + "");
            } else if(currentOperation == GET_REGION_LIST) {
                result = DataProvider.getDialogContent(selectedCommunityId, currentOperation + "");
            } else if(currentOperation == GET_BUILDING_LIST) {
                result = DataProvider.getDialogContent(selectedRegionId, currentOperation + "");
            } else if(currentOperation == GET_UNIT_LIST) {
                result = DataProvider.getDialogContent(selectedBuildingId, currentOperation + "");
            } else if(currentOperation == GET_ROOM_LIST) {
                result = DataProvider.getDialogContent(selectedUnitId, currentOperation + "");
            } else if(currentOperation == SUBMIT_INFO) {
                result = DataProvider.userEstateAdd(uid, selectedCommunityId, sbAddress.toString() , selectedRoomId, selectedUserTypeId);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            CustomViewUtil.dismissDialog();
            if(currentOperation == GET_COMMUNITY_LIST) {
                parseCommunityResult(result);
            } else if(currentOperation == GET_REGION_LIST) {
                parseRegionResult(result);
            } else if(currentOperation == GET_BUILDING_LIST) {
                parseBuildingResult(result);
            } else if(currentOperation == GET_UNIT_LIST) {
                parseUnitResult(result);
            } else if(currentOperation == GET_ROOM_LIST) {
                parseRoomResult(result);
            } else if(currentOperation == SUBMIT_INFO) {
                parseSubmitResult(result);
            }
        }
    }

    private void parseSubmitResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                CustomViewUtil.createToast(AddHousePropertyActivity.this, resultObject.getString("sign"));
                if(resultObject.getInt("status") == 1) {
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseCommunityResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                JSONArray data = resultObject.getJSONArray("data");
                Gson gson = new Gson();
                communityList = gson.fromJson(data.toString(), new TypeToken<List<DropDownItem>>() {}.getType());
                handleNullName(communityList, getResources().getString(R.string.no_community));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseRegionResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                JSONArray data = resultObject.getJSONArray("data");
                Gson gson = new Gson();
                regionList = gson.fromJson(data.toString(), new TypeToken<List<DropDownItem>>() {}.getType());
                handleNullName(regionList, getResources().getString(R.string.no_region));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseBuildingResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                JSONArray data = resultObject.getJSONArray("data");
                Gson gson = new Gson();
                buildingList = gson.fromJson(data.toString(), new TypeToken<List<DropDownItem>>() {}.getType());
                handleNullName(buildingList, getResources().getString(R.string.no_building));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseUnitResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                JSONArray data = resultObject.getJSONArray("data");
                Gson gson = new Gson();
                unitList = gson.fromJson(data.toString(), new TypeToken<List<DropDownItem>>() {}.getType());
                handleNullName(unitList, getResources().getString(R.string.no_unit));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseRoomResult(String result) {
        try {
            if(result != null && !result.equals("")) {
                JSONObject resultObject = new JSONObject(result);
                JSONArray data = resultObject.getJSONArray("data");
                Gson gson = new Gson();
                roomList = gson.fromJson(data.toString(), new TypeToken<List<DropDownItem>>() {}.getType());
                handleNullName(roomList, getResources().getString(R.string.no_room));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleNullName(List<DropDownItem> list, String defaultName) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals("")) {
                list.get(i).setName(defaultName);
            }
        }
    }

}
