package com.zzzealous.seekme.Fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zzzealous.seekme.Activity.News.Article.LifeArticleActivity;
import com.zzzealous.seekme.Activity.News.Article.StudyArticleActivity;
import com.zzzealous.seekme.Activity.News.Article.WorkArticleActivity;
import com.zzzealous.seekme.Activity.News.Notify.NoticeActivity;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment  implements View.OnClickListener{

    private View view;
    LinearLayout linear_gongzuo;
    LinearLayout linear_xuexi;
    LinearLayout linear_shenghuo;

    LinearLayout linear_tongzhi;
    //LinearLayout linear_pingxuan;
    //LinearLayout linear_toupiao;


    public NewsFragment() {
        // Required empty public constructor
    }

    public static final int START_NOTIFICATION_ACTIVTIY = 0x00;
    public static final int START_WORK_ACTIVTIY  = 0x01;
    public static final int START_STUDY_ACTIVTIY = 0x02;
    public static final int START_LIFE_ACTIVTIY = 0x03;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_WORK_ACTIVTIY :
                    startActivity(new Intent(getActivity(),WorkArticleActivity.class));
                    break;
                case START_STUDY_ACTIVTIY :
                    startActivity(new Intent(getActivity(),StudyArticleActivity.class));
                    break;
                case START_LIFE_ACTIVTIY :
                    startActivity(new Intent(getActivity(),LifeArticleActivity.class));
                    break;
                case START_NOTIFICATION_ACTIVTIY :
                    startActivity(new Intent(getActivity(),NoticeActivity.class));
                    break;


            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_news, container, false);

        initView();

        return view;
    }

    private void initView() {
        linear_gongzuo = (LinearLayout)view.findViewById(R.id.showMyWork);
        linear_xuexi = (LinearLayout)view.findViewById(R.id.showMyStudy);
        linear_shenghuo = (LinearLayout)view.findViewById(R.id.showMyLife);

        linear_tongzhi = (LinearLayout)view.findViewById(R.id.tongzhi);
        //linear_pingxuan = (LinearLayout)view.findViewById(R.id.pingxuan);
        //linear_toupiao = (LinearLayout)view.findViewById(R.id.toupiao);


        linear_gongzuo.setOnClickListener(this);
        linear_xuexi.setOnClickListener(this);
        linear_shenghuo.setOnClickListener(this);

        linear_tongzhi.setOnClickListener(this);
        //linear_pingxuan.setOnClickListener(this);
       // linear_toupiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showMyWork:
                DatabaseManager.getInstance().getArticlecList("1");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        handler.sendEmptyMessage(START_WORK_ACTIVTIY);
                    }
                },400);
                break;
            case R.id.showMyStudy:
                DatabaseManager.getInstance().getArticlecList("2");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        handler.sendEmptyMessage(START_STUDY_ACTIVTIY);
                    }
                },400);
                break;
            case R.id.showMyLife:
                DatabaseManager.getInstance().getArticlecList("3");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        handler.sendEmptyMessage(START_LIFE_ACTIVTIY);
                    }
                },400);
                break;
            case R.id.tongzhi:

                //TODO 通过点击选项  把自己求救记录导入本地
                DatabaseManager.getInstance().getAllNotiRecord();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        handler.sendEmptyMessage(START_NOTIFICATION_ACTIVTIY);
                    }
                },300);
                break;
           /* case R.id.pingxuan:
                startActivity(new Intent(getActivity(),SelectActivity.class));
                break;
            case R.id.toupiao:
                startActivity(new Intent(getActivity(),VoteActivity.class));
                break;*/

        }
    }
}
