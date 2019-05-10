package com.zzzealous.seekme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.TraceRecord;
import com.zzzealous.seekme.R;

import java.util.List;

/**
 * Created by zzzealous on 2018/3/6.
 */


/**
 * Created by zzzealous on 2018/3/6.
 */

public class TraceRecordAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater layoutInflater;

    private List<TraceRecord> data;

    public TraceRecordAdapter(Context context, List<TraceRecord> data){
        this.context = context;

        this.data = data;

        this.layoutInflater = LayoutInflater.from(context);
    }
    public void refresh(List<TraceRecord> data) {
        this.data = data;
        notifyDataSetChanged();
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
        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.holder_trace_record,parent,false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.trace_record_date);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.trace_record_starttime);
            viewHolder.iv = (SimpleDraweeView)convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TraceRecord traceRecord = data.get(position);

        viewHolder.date.setText(traceRecord.getDate());
        viewHolder.startTime.setText(traceRecord.getTrace_starttime());
        viewHolder.iv.setImageURI( GlobalValues.baseUrl + "images/" + traceRecord.getTrace_pic_url());

//        GlobalValues.baseUrl + "images/" + traceRecord.getTrace_pic_url()

        return convertView;
    }


    private class ViewHolder {

        private TextView date;
        private TextView startTime;
        private SimpleDraweeView iv;
    }
}