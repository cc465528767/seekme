package com.zzzealous.seekme.Activity.MyMemberRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zzzealous.seekme.Adapter.SosRecordAdapter;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.SosRecord;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.UI.ShowSosDialog;

import java.util.ArrayList;
import java.util.List;

public class MemberHelpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView title;
    private String userid;
    private String titlename;
    private Button memberBack;

    private SosRecordAdapter mAdapter;
    private List<SosRecord> data = new ArrayList<>();//获取数据


    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_member_help);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("id");
        titlename = bundle.getString("name");


        initView();
        getData();
        setAdapter();
    }

    private void initView() {
        title = findViewById(R.id.member_help_title);
        title.setText(titlename+"的救援记录");


        memberBack = findViewById(R.id.member_backbtn2);
        mListView = (ListView)findViewById(R.id.member_help_listview);

        memberBack.setOnClickListener(this);

    }
    public void getData() {
        DatabaseManager.getInstance().getAllHelpRecord(userid,this);

        //获取本地数据
        data = GlobalValues.AllHelpRecord;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.member_backbtn2:
                MemberHelpActivity.this.finish();
                break;
        }
    }

    private void setAdapter() {
        mAdapter = new SosRecordAdapter(this,data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void refresh(List<SosRecord> data) {
        this.data = data;
        setAdapter();
        mAdapter.refresh(data);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showSosDialog(position);
    }

    private void showSosDialog(int position) {
        ShowSosDialog dia = new ShowSosDialog(MemberHelpActivity.this,data.get(position).getDate(),
                 data.get(position).getSos_starttime(),
                 data.get(position).getHelper_name(),data.get(position).getSeeker_name()
                 ,data.get(position).getSos_pic_url());
        dia.setCanceledOnTouchOutside(true);

        dia.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 清除ATR记录
        GlobalValues.AllHelpRecord.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
