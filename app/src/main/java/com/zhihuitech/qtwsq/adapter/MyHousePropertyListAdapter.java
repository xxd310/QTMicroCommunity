package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zhihuitech.qtwsq.activity.MyHousePropertyActivity;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.MyHouseProperty;
import com.zhihuitech.qtwsq.util.CustomViewUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class MyHousePropertyListAdapter extends BaseAdapter {
    private Context context;
    private List<MyHouseProperty> list;
    private Drawable deleteEnableDrawable;
    private Drawable deleteDisableDrawable;

    public MyHousePropertyListAdapter(Context context, List<MyHouseProperty> list) {
        this.context = context;
        this.list = list;
        deleteEnableDrawable =  context.getResources().getDrawable(R.drawable.delete_enabled);
        deleteEnableDrawable.setBounds(0, 0, deleteEnableDrawable.getIntrinsicWidth() / 2, deleteEnableDrawable.getIntrinsicHeight() / 2);
        deleteDisableDrawable =  context.getResources().getDrawable(R.drawable.delete_disabled);
        deleteDisableDrawable.setBounds(0, 0, deleteDisableDrawable.getIntrinsicWidth() / 2, deleteDisableDrawable.getIntrinsicHeight() / 2);
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
            convertView = mInflater.inflate(R.layout.my_house_property_item, null);
            viewHolder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_name_my_house_property_item);
            viewHolder.tvPhone = (TextView) convertView
                    .findViewById(R.id.tv_phone_my_house_property_item);
            viewHolder.tvAddress = (TextView) convertView
                    .findViewById(R.id.tv_address_my_house_property_item);
            viewHolder.llDefault = (LinearLayout) convertView.findViewById(R.id.ll_default_address_my_house_property_item);
            viewHolder.cbDefault = (CheckBox) convertView.findViewById(R.id.cb_default_my_house_property_item);
            viewHolder.tvDefault = (TextView) convertView.findViewById(R.id.tv_default_my_house_property_item);
            viewHolder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete_my_house_property_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyHouseProperty property = (MyHouseProperty) getItem(position);
        viewHolder.tvName.setText(property.getRealname());
        viewHolder.tvPhone.setText(property.getTel());
        viewHolder.tvAddress.setText(property.getIsdefault().equals("1")? "【默认】" + property.getAddress() : property.getAddress());
        viewHolder.llDefault.setEnabled(property.getIsdefault().equals("0") ? true : false);
        viewHolder.cbDefault.setChecked(property.getIsdefault().equals("1") ? true : false);
        viewHolder.tvDefault.setTextColor(property.getIsdefault().equals("1") ? context.getResources().getColor(R.color.default_address_text) : Color.GRAY);
        viewHolder.tvDelete.setVisibility((property.getId() == null || property.getId().equals("")) ? View.GONE : View.VISIBLE);
        viewHolder.tvDelete.setEnabled(property.getIsdefault().equals("1") ? false : true);
        viewHolder.tvDelete.setCompoundDrawables(property.getIsdefault().equals("1") ? deleteDisableDrawable : deleteEnableDrawable, null, null, null);

        viewHolder.llDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyHousePropertyActivity)context).showSetDefaultConfirmDialog(position);
            }
        });
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyHousePropertyActivity)context).showDeleteConfirmDialog(position);
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView tvName;
        TextView tvPhone;
        TextView tvAddress;
        LinearLayout llDefault;
        CheckBox cbDefault;
        TextView tvDefault;
        TextView tvDelete;
    }
}
