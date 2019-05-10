package com.zzzealous.seekme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.User;
import com.zzzealous.seekme.R;

import java.util.List;

/**
 * Created by zzzealous on 2018/3/6.
 */

public class MemberListAdapter extends BaseAdapter {
    private Context context;

    private LayoutInflater layoutInflater;

    private List<User> data;

    public MemberListAdapter(Context context, List<User> data) {
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
        MemberListAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new MemberListAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.holder_my_member, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.jobnumber = (TextView) convertView.findViewById(R.id.member_jobnumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MemberListAdapter.ViewHolder) convertView.getTag();
        }
        User user = data.get(position);

        viewHolder.name.setText(user.getName());
        viewHolder.jobnumber.setText(user.getJobnumber());


        return convertView;
    }

    public void refresh(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    private class ViewHolder {

        private TextView name;
        private TextView jobnumber;

    }
}
