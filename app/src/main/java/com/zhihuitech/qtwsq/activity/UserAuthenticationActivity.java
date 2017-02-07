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

/**
 * Created by Administrator on 2016/7/30.
 */
public class UserAuthenticationActivity extends Activity implements View.OnClickListener {
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
        setContentView(R.layout.user_authentication);
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
        userTypeAdapter = new DialogListAdapter(UserAuthenticationActivity.this, userTypeList, 6);
    }

    private void findViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back_user_authentication);
        btnCommunity = (Button) findViewById(R.id.btn_community_user_authentication);
        btnRegion = (Button) findViewById(R.id.btn_region_user_authentication);
        btnBuildingNumber = (Button) findViewById(R.id.btn_building_number_user_authentication);
        btnUnit = (Button) findViewById(R.id.btn_unit_user_authentication);
        btnRoomNumber = (Button) findViewById(R.id.btn_room_number_user_authentication);
        btnUserType = (Button) findViewById(R.id.btn_user_type_user_authentication);
        btnSubmit = (Button) findViewById(R.id.btn_submit_user_authentication);
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
            case R.id.ll_back_user_authentication:
                finish();
                break;
            case R.id.btn_community_user_authentication:
                if(communityList == null || communityList.size() == 0) {
                    CustomViewUtil.createToast(UserAuthenticationActivity.this, getResources().getString(R.string.no_community_to_choose));
                    return;
                }
                showCommunityDialog();
                break;
            case R.id.btn_region_user_authentication:
                if(regionList == null || regionList.size() == 0) {
                    CustomViewUtil.createToast(UserAuthenticationActivity.this, getResources().getString(R.string.no_region_to_choose));
                    return;
                }
                showRegionDialog();
                break;
            case R.id.btn_building_number_user_authentication:
                if(buildingList == null || buildingList.size() == 0) {
                    CustomViewUtil.createToast(UserAuthenticationActivity.this, getResources().getString(R.string.no_building_to_choose));
                    return;
                }
                showBuildingDialog();
                break;
            case R.id.btn_unit_user_authentication:
                if(unitList == null || unitList.size() == 0) {
                    CustomViewUtil.createToast(UserAuthenticationActivity.this, getResources().getString(R.string.no_unit_to_choose));
                    return;
                }
                showUnitDialog();
                break;
            case R.id.btn_room_number_user_authentication:
                if(roomList == null || roomList.size() == 0) {
                    CustomViewUtil.createToast(UserAuthenticationActivity.this, getResources().getString(R.string.no_room_to_choose));
                    return;
                }
                showRoomDialog();
                break;
            case R.id.btn_user_type_user_authentication:
                showUserTypeDialog();
                break;
            case R.id.btn_submit_user_authentication:
                currentOperation = SUBMIT_INFO;
                submitInformation();
                break;
        }
    }

    private void submitInformation() {
        String community = btnCommunity.getText().toString();
        String region = btnRegion.getText().toString();
        String buildingNumber = btnBuildingNumber.getText().toString();
        String unit = btnUnit.getText().toString();
        String roomNumber = btnRoomNumber.getText().toString();
        String userType = btnUserType.getText().toString();
        if(community.equals(getResources().getString(R.string.choose_community)) && communityList.size() != 0) {
            Toast.makeText(UserAuthenticationActivity.this, getResources().getString(R.string.please_choose_community), Toast.LENGTH_SHORT).show();
            return;
        }
        if(region.equals(getResources().getString(R.string.choose_region)) && regionList.size() != 0) {
            Toast.makeText(UserAuthenticationActivity.this, getResources().getString(R.string.please_choose_region), Toast.LENGTH_SHORT).show();
            return;
        }
        if(buildingNumber.equals(getResources().getString(R.string.choose_building_number)) && buildingList.size() != 0) {
            Toast.makeText(UserAuthenticationActivity.this, getResources().getString(R.string.please_choose_building), Toast.LENGTH_SHORT).show();
            return;
        }
        if(unit.equals(getResources().getString(R.string.choose_unit)) && unitList.size() != 0) {
            Toast.makeText(UserAuthenticationActivity.this, getResources().getString(R.string.please_choose_unit), Toast.LENGTH_SHORT).show();
            return;
        }
        if(roomNumber.equals(getResources().getString(R.string.choose_room_number)) && roomList.size() != 0) {
            Toast.makeText(UserAuthenticationActivity.this, getResources().getString(R.string.please_choose_room), Toast.LENGTH_SHORT).show();
            return;
        }
        if(userType.equals(getResources().getString(R.string.choose_user_type))) {
            Toast.makeText(UserAuthenticationActivity.this, getResources().getString(R.string.please_choose_user_type), Toast.LENGTH_SHORT).show();
            return;
        }
        currentOperation = SUBMIT_INFO;
        MyTask task = new MyTask();
        task.execute();
    }

    private void showCommunityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAuthenticationActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_community));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(UserAuthenticationActivity.this, communityList, 1);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserAuthenticationActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_region));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(UserAuthenticationActivity.this, regionList, 2);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAuthenticationActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_building_number));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(UserAuthenticationActivity.this, buildingList, 3);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAuthenticationActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_unit));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(UserAuthenticationActivity.this, unitList, 4);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAuthenticationActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(getResources().getString(R.string.choose_room_number));
        ListView lv = (ListView) view.findViewById(R.id.lv_content_list);
        DialogListAdapter adapter = new DialogListAdapter(UserAuthenticationActivity.this, roomList, 5);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserAuthenticationActivity.this);
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
                CustomViewUtil.createProgressDialog(UserAuthenticationActivity.this, getResources().getString(R.string.submitting_information));
            } else {
                CustomViewUtil.createProgressDialog(UserAuthenticationActivity.this, getResources().getString(R.string.obtaining_information));
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
                result = DataProvider.verification_isOwner(uid, selectedCommunityId, selectedRegionId, selectedBuildingId, selectedUnitId, selectedRoomId, selectedUserTypeId);
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
                CustomViewUtil.createToast(UserAuthenticationActivity.this, resultObject.getString("sign"));
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
