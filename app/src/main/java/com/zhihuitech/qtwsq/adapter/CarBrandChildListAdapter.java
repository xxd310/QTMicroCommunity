package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.zhihuitech.qtwsq.activity.ChooseBrandActivity;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.Car;
import com.zhihuitech.qtwsq.entity.CarBrand;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CarBrandChildListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CarBrand> childList;

    public CarBrandChildListAdapter(Context context, ArrayList<CarBrand> childList) {
        super();
        this.context = context;
        this.childList = childList;
    }

    @Override
    public int getCount() {
        return childList.size();
    }

    @Override
    public Object getItem(int position) {
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.brand_list_child_item_item, null);
            viewHolder.ivIcon = (ImageView) convertView
                    .findViewById(R.id.iv_brand_icon);
            viewHolder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_brand_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CarBrand cb = (CarBrand) getItem(position);
        viewHolder.tvName.setText(cb.getName());
        Glide.with(context).load(cb.getUrl()).asBitmap().into(viewHolder.ivIcon);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChooseBrandActivity)context).returnResult(cb);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
    }
}
