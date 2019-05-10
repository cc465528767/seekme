package com.zzzealous.seekme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzzealous.seekme.Bean.Article;
import com.zzzealous.seekme.R;

import java.util.List;

/**
 * Created by zzzealous on 2018/7/11.
 */

public class ArticleAdapter extends BaseAdapter{

    private Context context;

    private LayoutInflater layoutInflater;

    private List<Article> data;

    public ArticleAdapter(Context context, List<Article> data){
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

        ArticleAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ArticleAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.holder_article,parent,false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.article_title);
            viewHolder.iv =  convertView.findViewById(R.id.arti_adapter_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArticleAdapter.ViewHolder) convertView.getTag();
        }

        Article article = data.get(position);

        viewHolder.title.setText(article.getTitle());
        int type = article.getType();
        if(type == 1){
            viewHolder.iv.setImageResource(R.drawable.be_ord);
        }
        else if (type == 2){
            viewHolder.iv.setImageResource(R.drawable.be_loap);
        }
        else if (type == 3){
            viewHolder.iv.setImageResource(R.drawable.be_lifebu);
        }

        return convertView;
    }

    private class ViewHolder {

        private TextView title;
        private ImageView iv;
    }
}
