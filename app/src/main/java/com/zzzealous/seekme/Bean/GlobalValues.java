package com.zzzealous.seekme.Bean;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zzzealous on 2018/2/4.
 */

public class GlobalValues {
    //阿里云网址  http://47.94.144.72:8020/
    //实验室  192.168.191.3 10.1.112.253 10.62.154.152
    //测试时依据服务器地址随时更换ip 本地的写本机，阿里云写阿里云
    //sp = getSharedPreferences("loginToken", Context.MODE_PRIVATE);
    public static String baseUrl = "http://10.62.154.84:8020/";

    public static Double SosLat = null;
    public static Double SosLng = null;
//    public static SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("loginToken", Context.MODE_PRIVATE);

    //是否巡检中
    public static int isStart = -1;//停止为0  开始为1  暂停为2
    //轨迹。巡检记录
    public static List<TraceRecord> AllTraceRecord = new ArrayList<TraceRecord>();
    //施救记录
    public static List<SosRecord> AllHelpRecord = new ArrayList<SosRecord>();
    //求救记录
    public static List<SosRecord> AllSeekRecord = new ArrayList<SosRecord>();
    //用户表
    public static List<User> MyMemberList = new ArrayList<User>();
    //在线人员地点表
    public static List<RealLoc> RealLocList = new ArrayList<RealLoc>();

    public static List<Notification> NotificationList = new ArrayList<Notification>();

    public static List<Article> ArticleList = new ArrayList<Article>();
    public static List<ArticleComment> ArticleCommentList = new ArrayList<ArticleComment>();

    public static JSONArray ClientArray = new JSONArray();
    public static String sosName =null;
    public static String MyCid = null;
}
