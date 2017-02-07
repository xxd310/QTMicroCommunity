package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zhihuitech.qtwsq.activity.CarManagementActivity;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.Car;
import com.zhihuitech.qtwsq.entity.DropDownItem;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class CarListAdapter extends BaseAdapter {
    private Context context;
    private List<Car> list;
    private int step;

    public CarListAdapter(Context context, List<Car> list) {
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
        if(null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.car_management_list_item, null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_brand_icon_list_item);
            viewHolder.tvBrandName = (TextView) convertView.findViewById(R.id.tv_brand_name_list_item);
            viewHolder.tvPlateNo = (TextView) convertView.findViewById(R.id.tv_car_plate_no_list_item);
            viewHolder.tvReviewStatus = (TextView) convertView.findViewById(R.id.tv_review_status);
            viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Car c = (Car) getItem(position);
        Glide.with(context).load(c.getUrl()).asBitmap().into(viewHolder.ivIcon);
        viewHolder.tvBrandName.setText("品牌：" + c.getBrand());
        viewHolder.tvPlateNo.setText("车牌号：" + c.getCar_no());
        viewHolder.tvReviewStatus.setVisibility(c.getStatus().equals("0") ? View.VISIBLE : View.INVISIBLE);
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CarManagementActivity)context).showDeleteConfirmDialog(position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvBrandName;
        TextView tvPlateNo;
        TextView tvReviewStatus;
        ImageView ivDelete;
    }
}
