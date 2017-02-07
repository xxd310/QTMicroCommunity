package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhihuitech.qtwsq.activity.CommonRepairActivity;
import com.zhihuitech.qtwsq.activity.FamilyRepairActivity;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.DropDownItem;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class RepairPicListAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> list;

    public RepairPicListAdapter(Context context, List<Bitmap> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.repair_pic_list_item, null);
            viewHolder.ivShow = (ImageView) convertView.findViewById(R.id.iv_repair_pic_list_item);
            viewHolder.ivAdd = (ImageView) convertView.findViewById(R.id.iv_add_repair_pic_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivShow.setImageBitmap((Bitmap) getItem(position));
        if(position == list.size() - 1) {
            viewHolder.ivAdd.setVisibility(View.VISIBLE);
            viewHolder.ivShow.setVisibility(View.GONE);
        } else {
            viewHolder.ivAdd.setVisibility(View.GONE);
            viewHolder.ivShow.setVisibility(View.VISIBLE);
        }
        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof CommonRepairActivity) {
                    ((CommonRepairActivity)context).selectPic();
                } else if(context instanceof FamilyRepairActivity) {
                    ((FamilyRepairActivity)context).selectPic();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView ivShow;
        ImageView ivAdd;
    }
}
