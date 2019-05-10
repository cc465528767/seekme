package com.zzzealous.seekme.Activity.News.Notify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzzealous.seekme.Adapter.NotificationAdapter;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.Notification;
import com.zzzealous.seekme.GTPush.GTDataManager;
import com.zzzealous.seekme.R;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    LinearLayout createNotice;
    Button noticeBackbtn;
    NotificationAdapter mAdapter;
    ListView mListView;

    private List<Notification> data = new ArrayList<>();//获取本地数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initView();
        getData();

        setAdapter();
    }

    private void initView() {
        createNotice  = (LinearLayout)findViewById(R.id.create_notice_ll);
        noticeBackbtn =(Button)findViewById(R.id.notice_backbtn);
        mListView = (ListView)findViewById(R.id.notice_record_listview);


        createNotice.setOnClickListener(this);
        noticeBackbtn.setOnClickListener(this);
    }
    private void getData() {
        //获取本地数据
        data = GlobalValues.NotificationList;
    }

    private void setAdapter() {
        mAdapter = new NotificationAdapter(NoticeActivity.this,data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notice_backbtn:
                NoticeActivity.this.finish();
                break;

            case R.id.create_notice_ll:
                GlobalValues.ClientArray = null;
                GTDataManager.getInstance().getClientArray();
                Intent intent = new Intent(NoticeActivity.this, CreateNoticeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = data.get(position).getTitle();
        String content = data.get(position).getContent();
        String sendername = data.get(position).getSenderName();
        String sendtime = data.get(position).getSendtime();
        String sendday = data.get(position).getSendday();
        String notitime = data.get(position).getNotitime();
        String notiday = data.get(position).getNotiday();

        Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("sendername",sendername);
        intent.putExtra("sendtime",sendtime);
        intent.putExtra("sendday",sendday);
        intent.putExtra("notitime",notitime);
        intent.putExtra("notiday",notiday);

        startActivity(intent);
    }

}
