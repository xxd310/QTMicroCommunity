package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.Car;
import com.zhihuitech.qtwsq.entity.CarBrand;
import com.zhihuitech.qtwsq.util.MyGridView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CarBrandListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> groupList;
    private ArrayList<ArrayList<CarBrand>> childList;
    private Context context;

    public CarBrandListAdapter(ArrayList<String> groupList, ArrayList<ArrayList<CarBrand>> childList, Context context) {
        super();
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.brand_list_group_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_first_char);
        tv.setText(groupList.get(groupPosition));
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.brand_list_child_item, null);
        MyGridView lv = (MyGridView) view.findViewById(R.id.mgv_brand_list);
        CarBrandChildListAdapter adapter = new CarBrandChildListAdapter(context, childList.get(groupPosition));
        lv.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
