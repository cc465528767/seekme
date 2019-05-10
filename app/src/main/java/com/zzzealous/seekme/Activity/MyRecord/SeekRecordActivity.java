package com.zzzealous.seekme.Activity.MyRecord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zzzealous.seekme.Adapter.SosRecordAdapter;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.SosRecord;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.UI.ShowSosDialog;

import java.util.ArrayList;
import java.util.List;

public class SeekRecordActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private Button trace_back;
    private SosRecordAdapter mAdapter;
    private List<SosRecord> data = new ArrayList<>();//获取本地数据

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_seek_record);
        if (GlobalValues.isStart != 1) {
            GlobalValues.isStart = -1;
        }
        initView();
        getData();
        setAdapter();
    }

    private void initView() {
        trace_back = (Button)findViewById(R.id.seek_backbtn);
        mListView = (ListView)findViewById(R.id.seek_record_listview);

        trace_back.setOnClickListener(this);
    }

    private void getData() {
        //获取本地数据
        DatabaseManager.getInstance().getAllSeekRecord(
                SeekmePreference.getString("userId"),this);

        data = GlobalValues.AllSeekRecord;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seek_backbtn:
                SeekRecordActivity.this.finish();
                break;
        }
    }

    private void setAdapter() {
        mAdapter = new SosRecordAdapter(SeekRecordActivity.this,data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void refreshData(List<SosRecord> data){
        this.data = data;
        setAdapter();
        mAdapter.refresh(data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showSosDialog(position);
    }

    private void showSosDialog(int position) {
        ShowSosDialog dia = new ShowSosDialog(SeekRecordActivity.this,data.get(position).getDate(),
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
        GlobalValues.AllSeekRecord.clear();
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
