package com.zzzealous.seekme.Activity.News.Notify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzzealous.seekme.R;

public class NoticeDetailActivity extends AppCompatActivity {
    private String title;
    private String content;
    private String sendername;
    private String sendtime;
    private String sendday;
    private String notitime;
    private String notiday;

    Button back;
    TextView titleTV;
    TextView contentTV;
    TextView sendernameTV;
    TextView sendtimeTV;
    TextView senddayTV;
    TextView notitimeTV;
    TextView notidayTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        initView();
        receiveIntent();
        setView();
    }

    private void initView() {
        titleTV = findViewById(R.id.notice_detail_title);
        contentTV = findViewById(R.id.notice_detail_content);
        sendernameTV = findViewById(R.id.notice_detail_name);
        sendtimeTV = findViewById(R.id.notice_detail_time);
        senddayTV = findViewById(R.id.notice_detail_day);
        notitimeTV = findViewById(R.id.notice_detail_sendtime);
        notidayTV = findViewById(R.id.notice_detail_sendday);

        back = findViewById(R.id.notice_detail_backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeDetailActivity.this.finish();
            }
        });
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        content = bundle.getString("content");
        sendername = bundle.getString("sendername");
        sendtime = bundle.getString("sendtime");
        sendday = bundle.getString("sendday");
        notitime = bundle.getString("notitime");
        notiday = bundle.getString("notiday");
    }


    private void setView() {
        titleTV.setText(title);
        contentTV.setText(content);
        sendernameTV.setText(sendername);
        sendtimeTV.setText(sendtime);
        senddayTV.setText(sendday);
        notitimeTV.setText(notitime);
        notidayTV.setText(notiday);
    }

}
