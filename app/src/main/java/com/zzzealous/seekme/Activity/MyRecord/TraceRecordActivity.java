package com.zzzealous.seekme.Activity.MyRecord;

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zzzealous.seekme.Adapter.TraceRecordAdapter;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.TraceRecord;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        import com.zzzealous.seekme.Database.DatabaseManager;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        import com.zzzealous.seekme.Preference.SeekmePreference;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        import com.zzzealous.seekme.R;
import com.zzzealous.seekme.UI.TraceDialog;

import java.util.ArrayList;
import java.util.List;

public class TraceRecordActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private Button trace_back;
    private TraceRecordAdapter mAdapter;
    private List<TraceRecord> data = new ArrayList<>();//获取本地数据

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_trace_record);
        if (GlobalValues.isStart != 1) {
            GlobalValues.isStart = -1;
        }
        initView();
        getData();
        setAdapter();


    }

    private void initView() {
        trace_back = (Button)findViewById(R.id.trace_backbtn);
        mListView = (ListView)findViewById(R.id.trace_record_listview);

        trace_back.setOnClickListener(this);
    }

    private void getData() {
        //获取本地数据
        DatabaseManager.getInstance().getAllRunRecord(
                SeekmePreference.getString("userId"),this);

        data = GlobalValues.AllTraceRecord;
    }
    public void refreshData(List<TraceRecord> data){
        this.data = data;
        setAdapter();
        mAdapter.refresh(data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.trace_backbtn:
                TraceRecordActivity.this.finish();
                break;
        }
    }

    private void setAdapter() {
        mAdapter = new TraceRecordAdapter(TraceRecordActivity.this,data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showTraceDialog(position);
    }

    private void showTraceDialog(int position) {
        TraceDialog dia = new TraceDialog(TraceRecordActivity.this,data.get(position).getDate(),
                 data.get(position).getTrace_starttime(),data.get(position).getTrace_realtime(),
                 data.get(position).getTrace_pic_url());
        dia.setCanceledOnTouchOutside(true);

        dia.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 清除ATR记录
        GlobalValues.AllTraceRecord.clear();
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
