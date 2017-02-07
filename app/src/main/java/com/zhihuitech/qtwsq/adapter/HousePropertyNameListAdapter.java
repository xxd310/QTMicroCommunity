package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class HousePropertyNameListAdapter extends BaseAdapter {
    private Context context;
    private List<MyHouseProperty> list;

    public HousePropertyNameListAdapter(Context context, List<MyHouseProperty> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.house_property_list_item, null);
            viewHolder.rb = (RadioButton) convertView
                    .findViewById(R.id.rb_is_default);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyHouseProperty property = (MyHouseProperty) getItem(position);
        viewHolder.rb.setText(property.getCommunity_name());
        viewHolder.rb.setChecked(position == 0 ? true : false);
        return convertView;
    }

    static class ViewHolder{
        RadioButton rb;
    }
}
