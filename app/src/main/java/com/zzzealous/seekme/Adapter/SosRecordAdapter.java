package com.zzzealous.seekme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.SosRecord;
import com.zzzealous.seekme.R;

import java.util.List;

/**
 * Created by zzzealous on 2018/4/1.
 */

public class SosRecordAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater layoutInflater;

    private List<SosRecord> data;

    public SosRecordAdapter(Context context, List<SosRecord> data) {
        this.context = context;

        this.data = data;

        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new SosRecordAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.holder_sos_record, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.sos_record_date);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.sos_record_starttime);
            viewHolder.iv = (SimpleDraweeView) convertView.findViewById(R.id.sos_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SosRecordAdapter.ViewHolder) convertView.getTag();
        }
        SosRecord sosRecord = data.get(position);

        viewHolder.date.setText(sosRecord.getDate());
        viewHolder.startTime.setText(sosRecord.getSos_starttime());
        viewHolder.iv.setImageURI(GlobalValues.baseUrl + "images/" + sosRecord.getSos_pic_url());


        return convertView;
    }

    public void refresh(List<SosRecord> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    private class ViewHolder {

        private TextView date;
        private TextView startTime;
        private SimpleDraweeView iv;
    }
}