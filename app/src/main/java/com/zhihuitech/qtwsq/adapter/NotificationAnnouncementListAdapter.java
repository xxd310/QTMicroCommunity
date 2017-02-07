package com.zhihuitech.qtwsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhihuitech.qtwsq.activity.R;
import com.zhihuitech.qtwsq.entity.NotificationAnnouncement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class NotificationAnnouncementListAdapter extends BaseAdapter {
    private Context context;
    private List<NotificationAnnouncement> list;
    private SimpleDateFormat format;

    public NotificationAnnouncementListAdapter(Context context, List<NotificationAnnouncement> list) {
        this.context = context;
        this.list = list;
        format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
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
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.notification_announcement_item, null);
            viewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title_notification_announcement);
            viewHolder.tvPublishDate = (TextView) convertView
                    .findViewById(R.id.tv_publish_date_notification_announcement);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NotificationAnnouncement na = (NotificationAnnouncement) getItem(position);
        viewHolder.tvTitle.setText(na.getTitle());
        viewHolder.tvPublishDate.setText(format.format(new Date(Long.parseLong(na.getCreatetime()) * 1000)));

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvPublishDate;
    }
}
