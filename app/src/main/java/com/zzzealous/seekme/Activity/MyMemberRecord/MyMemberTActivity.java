package com.zzzealous.seekme.Activity.MyMemberRecord;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.zzzealous.seekme.Adapter.MemberListAdapter;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.User;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;

import java.util.ArrayList;
import java.util.List;

public class MyMemberTActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button member_back;
    private MemberListAdapter mAdapter;
    private List<User> data = new ArrayList<>();//获取本地数据

    private String name;//被点击的人名

    private ListView mListView;

    public static final int START_MY_MEMBER_TARCE_RECORD_ACTIVTIY = 0x00;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_MY_MEMBER_TARCE_RECORD_ACTIVTIY:
                    Intent intent = new Intent(MyMemberTActivity.this, MemberTraceActivity.class);
                    intent.putExtra("name", name);//用于携带所点击的用户名

                    startActivity(intent);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_member_t);

        initView();
        getData();
        setAdapter();

    }

    private void initView() {
        member_back = findViewById(R.id.member_backbtn1);
        mListView = findViewById(R.id.my_member_listview);

        member_back.setOnClickListener(this);
    }

    private void getData() {
        DatabaseManager.getInstance().getMyMemberList(
                SeekmePreference.getInt("belong"),
                SeekmePreference.getInt("leaf"), this);

        data = GlobalValues.MyMemberList;
    }

    private void setAdapter() {
        mAdapter = new MemberListAdapter(getApplicationContext(), data);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);
    }

    public void refresh(List<User> data) {
        this.data=data;
        setAdapter();
        mAdapter.refresh(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_backbtn1:
                MyMemberTActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GlobalValues.AllTraceRecord.clear();
//        DatabaseManager.getInstance().getAllRunRecord(data.get(position).getUserId(), MyMemberTActivity.this);

        name = data.get(position).getName();
        Intent intent = new Intent(MyMemberTActivity.this, MemberTraceActivity.class);
        intent.putExtra("id", data.get(position).getUserId());//用于携带所点击的用户id
        intent.putExtra("name", name);//用于携带所点击的用户名
        startActivity(intent);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handler.sendEmptyMessage(START_MY_MEMBER_TARCE_RECORD_ACTIVTIY);
//            }
//        }, 300);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 清除MML记录
        GlobalValues.MyMemberList.clear();
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
