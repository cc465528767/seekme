package com.zzzealous.seekme.GTPush;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.Preference.SeekmePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zzzealous on 2018/6/11.
 */

public class GTDataManager {

    /**
     *  将 content 写为  String类的json 字段
     *  里边包含 type  和   text   文本内容和消息类型
     *                    jobnum  phone name
     *
     *  文本内容用来解析 显示
     *
     *  消息类型用于后台识别  用于不同工作
     */

    private static final GTDataManager instance = new GTDataManager();
    public static GTDataManager getInstance()
    {
        return instance;
    }



    public void sendSosMsg(final JSONArray jsonArray,
                           final String sosid) {
        String url = GlobalValues.baseUrl + "gtrequest/sendSos";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    Log.d("sos", "onResponse: "+s);
                    if (msg.equals("发送记录成功")){
                        Log.i("TAG", msg);
                    }else
                        Log.i("TAG", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                })
        {
            protected Map<String ,String> getParams() throws AuthFailureError
            {
                Map<String,String> hashMap = new HashMap<>();
                hashMap.put("userid", SeekmePreference.getString("userId"));
                hashMap.put("clientList",jsonArray.toString());

                hashMap.put("content","{\"type\":\"1\","+
                        "\"phone\":"+"\""+SeekmePreference.getString("phone1")+"\","+
                        "\"name\":"+"\""+SeekmePreference.getString("name")+"\","+
                        "\"sosid\":"+"\""+sosid+"\","+
                        "\"jobnum\":"+"\""+SeekmePreference.getString("userId")+"\","+
                        "\"seekerCid\":"+"\""+SeekmePreference.getString("mycid")+"\","+
                        "\"seekerLat\":"+"\""+String.valueOf(GlobalValues.SosLat)+"\","+
                        "\"seekerLng\":"+"\""+String.valueOf(GlobalValues.SosLng)+"\""+"}");
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }


    public void sendBackMsg(final String seekerCid){
        String url = GlobalValues.baseUrl + "gtrequest/back";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("反馈成功")){
                        Log.i("TAG", msg);
                    }else
                        Log.i("TAG", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                })
        {
            protected Map<String ,String> getParams() throws AuthFailureError
            {
                Map<String,String> hashMap = new HashMap<>();
                hashMap.put("seekercid", seekerCid);
                hashMap.put("content", "{\"type\":\"2\","+
                        "\"helperphone\":"+"\""+SeekmePreference.getString("phone1")+"\","+
                        "\"helpername\":"+"\""+SeekmePreference.getString("name")+"\","+
                        "\"helperjobnum\":"+"\""+SeekmePreference.getString("userId")+"\"}");

                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }


    public void sendNotifyMsg(final  JSONArray jsonArray,final String NoticeTitle,final String NoticeContent){
        String url = GlobalValues.baseUrl + "gtrequest/sendNotification";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    Log.i("为什么出不来", msg+s);
                    if (msg.equals("发送记录成功")){
                        Log.i("TAG", msg);
                    }else
                        Log.i("TAG", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                })
        {
            protected Map<String ,String> getParams() throws AuthFailureError
            {
                Map<String,String> hashMap = new HashMap<>();
                hashMap.put("senderid", SeekmePreference.getString("userId"));
                hashMap.put("clientList",jsonArray.toString());

                hashMap.put("content","{\"type\":\"3\","+
                        "\"noticeTitle\":"+"\""+NoticeTitle+"\","+
                        "\"noticeContent\":"+"\""+NoticeContent+"\""+"}");
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }


    /***
     * 从数据库获取  能监控的所有成员
     ***/
    public void getClientArray(){
        String url = GlobalValues.baseUrl+"client/member";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    GlobalValues.ClientArray = new JSONObject(s).getJSONArray("data");
                    Log.d("shishiba", s+"onResponse: "+s);


                } catch (JSONException JException){
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        })
        {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("myid", SeekmePreference.getString("mycid"));
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

}
