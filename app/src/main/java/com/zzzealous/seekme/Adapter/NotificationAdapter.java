package com.zzzealous.seekme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.Notification;
import com.zzzealous.seekme.R;

import java.util.List;

/**
 * Created by zzzealous on 2018/7/4.
 */

public class NotificationAdapter  extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Notification> data;


    public NotificationAdapter(Context context, List<Notification> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new NotificationAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.holder_notification_record,parent,false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.noti_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.noti_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotificationAdapter.ViewHolder) convertView.getTag();
        }
        Notification notification = data.get(position);

        viewHolder.time.setText(notification.getSendtime());
        viewHolder.title.setText(notification.getTitle());

        return convertView;
    }

    private class ViewHolder {

        private TextView title;
        private TextView time;
    }
}
