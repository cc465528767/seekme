package com.zzzealous.seekme.Activity.News.Article;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.zzzealous.seekme.Adapter.ArticleAdapter;
import com.zzzealous.seekme.Bean.Article;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.R;

import java.util.ArrayList;
import java.util.List;

public class LifeArticleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Button back;
    ListView listView;
    ArticleAdapter articleAdapter;

    private List<Article> data = new ArrayList<>();



    private String title;
    private String content;
    private String publisher;
    private int likenumber;
    private int type ;
    private int paperid;

    public static final int GET_VISIBLE_COMMENT = 0x00;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_VISIBLE_COMMENT :

                    Intent intent = new Intent(LifeArticleActivity.this, ArticleDetailActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("paperid",paperid);
                    intent.putExtra("content",content);
                    intent.putExtra("publisher",publisher);
                    intent.putExtra("likenumber",likenumber);
                    intent.putExtra("type",type);
                    startActivity(intent);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_article);

        initView();
        getData();
        setAdapter();
    }

    private void initView() {
        back = findViewById(R.id.life_backbtn);
        listView = findViewById(R.id.life_article_listview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LifeArticleActivity.this.finish();
            }
        });
    }

    private void getData() {
        data = GlobalValues.ArticleList;
    }

    private void setAdapter() {
        articleAdapter = new ArticleAdapter(LifeArticleActivity.this,data);
        listView.setAdapter(articleAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        title = data.get(position).getTitle();
        content = data.get(position).getContent();
        publisher = data.get(position).getPublisher();
        likenumber  = data.get(position).getLikenumber();
        type = data.get(position).getType();
        paperid = data.get(position).getPaperid();

        DatabaseManager.getInstance().getVisibleComment(paperid);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(GET_VISIBLE_COMMENT);
            }
        },300);
    }

}
