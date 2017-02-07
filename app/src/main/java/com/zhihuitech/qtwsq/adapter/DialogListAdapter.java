package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.DropDownItem;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class DialogListAdapter extends BaseAdapter {
    private Context context;
    private List<DropDownItem> list;
    private int step;

    public DialogListAdapter(Context context, List<DropDownItem> list) {
        this.context = context;
        this.list = list;
    }

    public DialogListAdapter(Context context, List<DropDownItem> list, int step) {
        this.context = context;
        this.list = list;
        this.step = step;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_list_item, null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_name_dialog_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(list.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView tv;
    }
}
