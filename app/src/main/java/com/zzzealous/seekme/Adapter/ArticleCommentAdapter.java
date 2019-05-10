package com.zzzealous.seekme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.ArticleComment;
import com.zzzealous.seekme.R;

import java.util.List;

/**
 * Created by zzzealous on 2018/7/23.
 */

public class ArticleCommentAdapter   extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<ArticleComment> data;


    public ArticleCommentAdapter(Context context, List<ArticleComment> data){
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
        ArticleCommentAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ArticleCommentAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.holder_comment_record,parent,false);
            viewHolder.username = (TextView) convertView.findViewById(R.id.comment_username);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArticleCommentAdapter.ViewHolder) convertView.getTag();
        }
        ArticleComment articleComment = data.get(position);

        viewHolder.username.setText(articleComment.getCommentusername());
        viewHolder.comment.setText(articleComment.getComment());

        return convertView;
    }

    private class ViewHolder {

        private TextView username;
        private TextView comment;
    }
}
