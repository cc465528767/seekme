package com.zzzealous.seekme.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zzzealous.seekme.Activity.MyMemberRecord.MyMemberHActivity;
import com.zzzealous.seekme.Activity.MyMemberRecord.MyMemberTActivity;
import com.zzzealous.seekme.Activity.MyRecord.HelpRecordActivity;
import com.zzzealous.seekme.Activity.MyRecord.SeekRecordActivity;
import com.zzzealous.seekme.Activity.MyRecord.TraceRecordActivity;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;

public class WorkFragment extends Fragment implements View.OnClickListener {

    LinearLayout ManagerView ;
    LinearLayout MyTrace;
    LinearLayout MySos;
    LinearLayout MyHelp;

    LinearLayout MemberTrace;
    LinearLayout MenberHelp;

    View view;

    public static final int START_TARCE_RECORD_ACTIVTIY = 0x00;
    public static final int START_SEEK_RECORD_ACTIVTIY = 0x01;
    public static final int START_HELP_RECORD_ACTIVTIY = 0x02;
    public static final int START_MY_MEMBER_T_ACTIVTIY = 0x03;
    public static final int START_MY_MEMBER_H_ACTIVTIY = 0x04;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_TARCE_RECORD_ACTIVTIY :
                    startActivity(new Intent(getActivity(),TraceRecordActivity.class));
                    break;
                case START_SEEK_RECORD_ACTIVTIY  :
                    startActivity(new Intent(getActivity(),SeekRecordActivity.class));
                    break;
                case START_HELP_RECORD_ACTIVTIY :
                    startActivity(new Intent(getActivity(),HelpRecordActivity.class));
                    break;
                case START_MY_MEMBER_T_ACTIVTIY:
                    startActivity(new Intent(getActivity(),MyMemberTActivity.class));
                    break;
                case START_MY_MEMBER_H_ACTIVTIY:
                    startActivity(new Intent(getActivity(),MyMemberHActivity.class));
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_work, container, false);
        Log.d("what", SeekmePreference.getString("userId"));
        initView();

        return view;
    }

    private void initRecord() {
        GlobalValues.AllTraceRecord.clear();

        DatabaseManager.getInstance().getAllRunRecord(SeekmePreference.getString("userId"),this.getActivity());

    }

    private void initView() {
        //监管框
        ManagerView = (LinearLayout)view.findViewById(R.id.manager_view);

        //条
        MyTrace = (LinearLayout)view.findViewById(R.id.showMyTraceRecord);
        MySos = (LinearLayout)view.findViewById(R.id.showMySeekRecord);
        MyHelp = (LinearLayout)view.findViewById(R.id.showMyHelpRecord);

        MemberTrace = (LinearLayout)view.findViewById(R.id.myMemberTrace);
        MenberHelp = (LinearLayout)view.findViewById(R.id.myMemberHelp);

        if(SeekmePreference.getInt("leaf") == 2 || SeekmePreference.getInt("leaf") == 3)
        {
            ManagerView.setVisibility(View.VISIBLE);
        }

        MyTrace.setOnClickListener(this);
        MySos.setOnClickListener(this);
        MyHelp.setOnClickListener(this);

        MemberTrace.setOnClickListener(this);
        MenberHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showMyTraceRecord:
                startActivity(new Intent(getActivity(),TraceRecordActivity.class));

                /*//TODO 通过点击选项  把自己巡检记录导入本地
                DatabaseManager.getInstance().getAllRunRecord(
                        SeekmePreference.getString("userId"),getActivity());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(GlobalValues.AllTraceRecord.size() == 0){
                            ToastUtil.setShortToast(getActivity(), "暂无记录或网络不畅");
                        }

                        handler.sendEmptyMessage(START_TARCE_RECORD_ACTIVTIY);
                    }
                },10);*/
                break;
            case R.id.showMySeekRecord:

                //TODO 通过点击选项  把自己求救记录导入本地
//                DatabaseManager.getInstance().getAllSeekRecord(
//                        SeekmePreference.getString("userId"));

//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(GlobalValues.AllSeekRecord.size() == 0){
//                            ToastUtil.setShortToast(getActivity(), "暂无记录或网络不畅");
//                        }
//
//                        handler.sendEmptyMessage(START_SEEK_RECORD_ACTIVTIY);
//                    }
//                },400);
                startActivity(new Intent(getActivity(),SeekRecordActivity.class));
                break;
            case R.id.showMyHelpRecord:

                //TODO 通过点击选项  把自己施救记录导入本地
//                DatabaseManager.getInstance().getAllHelpRecord(
//                        SeekmePreference.getString("userId"));
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(GlobalValues.AllHelpRecord.size() == 0){
//                            ToastUtil.setShortToast(getActivity(), "暂无记录或网络不畅");
//                        }
//
//                        handler.sendEmptyMessage(START_HELP_RECORD_ACTIVTIY);
//                    }
//                },10);
                startActivity(new Intent(getActivity(),HelpRecordActivity.class));
                break;
            case R.id.myMemberTrace:

                //TODO 通过点击选项  获取可跟踪记录成员
//                DatabaseManager.getInstance().getMyMemberList(
//                        SeekmePreference.getInt("belong"),
//                        SeekmePreference.getInt("leaf"));
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        ToastUtil.setShortToast(getActivity(), GlobalValues.MyMemberList.size()+"");
//
//                        handler.sendEmptyMessage(START_MY_MEMBER_T_ACTIVTIY);
//                    }
//                },300);
                startActivity(new Intent(getActivity(),MyMemberTActivity.class));
                break;
            case R.id.myMemberHelp:
                //TODO 通过点击选项  获取可跟踪记录成员
//                DatabaseManager.getInstance().getMyMemberList(
//                        SeekmePreference.getInt("belong"),
//                        SeekmePreference.getInt("leaf"));
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.sendEmptyMessage(START_MY_MEMBER_H_ACTIVTIY);
//                    }
//                },300);
                startActivity(new Intent(getActivity(),MyMemberHActivity.class));
                break;

        }
    }
}
