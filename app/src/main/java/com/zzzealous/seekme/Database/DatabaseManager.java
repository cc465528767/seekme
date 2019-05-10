package com.zzzealous.seekme.Database;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Activity.LoginActivity;
import com.zzzealous.seekme.Activity.MainActivity;
import com.zzzealous.seekme.Activity.MyMemberRecord.MemberHelpActivity;
import com.zzzealous.seekme.Activity.MyMemberRecord.MemberTraceActivity;
import com.zzzealous.seekme.Activity.MyMemberRecord.MyMemberHActivity;
import com.zzzealous.seekme.Activity.MyMemberRecord.MyMemberTActivity;
import com.zzzealous.seekme.Activity.MyRecord.HelpRecordActivity;
import com.zzzealous.seekme.Activity.MyRecord.SeekRecordActivity;
import com.zzzealous.seekme.Activity.MyRecord.TraceRecordActivity;
import com.zzzealous.seekme.Bean.Article;
import com.zzzealous.seekme.Bean.ArticleComment;
import com.zzzealous.seekme.Bean.FileRequest;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.Notification;
import com.zzzealous.seekme.Bean.RealLoc;
import com.zzzealous.seekme.Bean.SosRecord;
import com.zzzealous.seekme.Bean.TraceRecord;
import com.zzzealous.seekme.Bean.User;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.Utils.TimeUtil;
import com.zzzealous.seekme.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zzzealous on 2018/3/4.
 */

public class DatabaseManager {

    private static final DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }

//    public static String token = SeekmePreference.getString("token");

    //public static Boolean loginstate=GlobalValues.sp.getBoolean("Loginstate",false);


    public void addTraceRecord(final TraceRecord traceRecord) {

        String url = GlobalValues.baseUrl + "trace/add";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("添加记录成功")) {
                        Log.i("TAG", msg);
                    } else if (msg.equals("tokennotvalue")) {
                        Log.d("token1", "token失效");
                    } else
                        Log.i("guiji", "存储失败");
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
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();

                hashMap.put("userid", SeekmePreference.getString("userId"));
                hashMap.put("starttime", traceRecord.getTrace_starttime());
                hashMap.put("realtime", traceRecord.getTrace_realtime());
                hashMap.put("picurl", traceRecord.getTrace_pic_url());
                hashMap.put("date", traceRecord.getDate());

                return hashMap;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

        };
        //设置请求标签
        request.setTag("add_trace_record");
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }


    /***
     * 从数据库获取我的所有的巡检记录
     ***/
    public void getAllRunRecord(final String userId, final Context context) {
        GlobalValues.AllTraceRecord.clear();
        String url = GlobalValues.baseUrl + "trace/search/mine";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.i("haha", s);
                    JSONObject JObject1 = new JSONObject(s);
                    String msg = JObject1.getString("msg");
                    if (msg.equals("tokennotvalue")) {
                        Log.d("token1", SeekmePreference.getString("token"));
                        // Log.d("what", GlobalValues.sp.getString("userId",null));


                    } else {

                        JSONArray JArray = new JSONObject(s).getJSONArray("data");
                        for (int i = 0; i < JArray.length(); i++) {
                            JSONObject JObject = JArray.getJSONObject(i);
                            TraceRecord traceRecord = new TraceRecord();

                            traceRecord.setTrace_starttime(JObject.getString("startTime"));
                            traceRecord.setTrace_realtime(JObject.getString("realTime"));
                            traceRecord.setDate(JObject.getString("date"));
                            traceRecord.setTrace_pic_url(JObject.getString("tracePic"));
                            traceRecord.setUserId(JObject.getString("userId"));

                            GlobalValues.AllTraceRecord.add(traceRecord);
                            Log.d("pic", traceRecord.getTrace_pic_url().toString());

                        }
                        if (context instanceof TraceRecordActivity) {
                            ((TraceRecordActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TraceRecordActivity) context).refreshData(GlobalValues.AllTraceRecord);

                                }
                            });
                        } else if (context instanceof MemberTraceActivity) {
                            ((MemberTraceActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((MemberTraceActivity) context).refresh(GlobalValues.AllTraceRecord);

                                }
                            });
                        }
                    }
                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("userid", userId);
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /***
     * 从数据库获取我的所有的求救记录
     ***/
    public void getAllSeekRecord(final String userId, final Context context) {
        GlobalValues.AllSeekRecord.clear();
        String url = GlobalValues.baseUrl + "sos/search/seek";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject1 = new JSONObject(s);
                    String msg = JObject1.getString("msg");
                    if (msg.equals("tokennotvalue")) {
                        Log.d("token1", "token失效" + s);
                        // Log.d("what", GlobalValues.sp.getString("userId",null));


                    } else {

                        JSONArray JArray = new JSONObject(s).getJSONArray("data");
                        for (int i = 0; i < JArray.length(); i++) {
                            JSONObject JObject = JArray.getJSONObject(i);
                            SosRecord sosRecord = new SosRecord();
                            sosRecord.setDate(JObject.getString("date"));
                            sosRecord.setHelper_name(JObject.getString("helperName"));
                            sosRecord.setSeeker_name(JObject.getString("seekerName"));
                            sosRecord.setSos_starttime(JObject.getString("createTime"));
                            sosRecord.setSos_pic_url(JObject.getString("sosPic"));

                            GlobalValues.AllSeekRecord.add(sosRecord);
                        }

                        if (context instanceof SeekRecordActivity) {
                            ((SeekRecordActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((SeekRecordActivity) context).refreshData(GlobalValues.AllSeekRecord);
                                }
                            });
                        }


                    }
                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("userid", userId);

                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /***
     * 从数据库获取我的所有的救援记录
     ***/
    public void getAllHelpRecord(final String userId, final Context context) {
        GlobalValues.AllHelpRecord.clear();
        String url = GlobalValues.baseUrl + "sos/search/help";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("trl", s);
                    JSONObject JObject1 = new JSONObject(s);
                    String msg = JObject1.getString("msg");

                    if (msg.equals("tokennotvalue")) {
                        Log.d("token1", "token失效");
                        // Log.d("what", GlobalValues.sp.getString("userId",null));
                    } else {

                        JSONArray JArray = new JSONObject(s).getJSONArray("data");
                        for (int i = 0; i < JArray.length(); i++) {
                            JSONObject JObject = JArray.getJSONObject(i);
                            SosRecord sosRecord = new SosRecord();
                            sosRecord.setDate(JObject.getString("date"));
                            sosRecord.setHelper_name(JObject.getString("helperName"));
                            sosRecord.setSeeker_name(JObject.getString("seekerName"));
                            sosRecord.setSos_starttime(JObject.getString("createTime"));
                            sosRecord.setSos_pic_url(JObject.getString("sosPic"));

                            GlobalValues.AllHelpRecord.add(sosRecord);
                        }

                        if (context instanceof HelpRecordActivity) {
                            ((HelpRecordActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((HelpRecordActivity) context).refresh(GlobalValues.AllHelpRecord);
                                }
                            });
                        } else if (context instanceof MemberHelpActivity) {
                            ((MemberHelpActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((MemberHelpActivity) context).refresh(GlobalValues.AllHelpRecord);

                                }
                            });
                        }
                    }
                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("userid", userId);

                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /**
     * 退出app  清除我的坐标信息
     *
     * @param userId
     */
    public void cleanMyLoc(final String userId) {
        GlobalValues.AllTraceRecord.clear();
        String url = GlobalValues.baseUrl + "realloc/cleanlatlng";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("onResponse", "啦啦啦onResponse: " + s);
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
//                    if (msg.equals("保存成功")){
//                        Log.i("TAG", msg);
//                    }else
                    Log.i("TAG", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("userId", userId);

                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /**
     * 更新我的坐标信息
     *
     * @param userId
     */
    public void updateMyLoc(final String userId, final double lat, final double lng, final Context context) {
        GlobalValues.AllTraceRecord.clear();
        String url = GlobalValues.baseUrl + "realloc/update/latlng";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("啦啦啦onResponse", "啦啦啦onResponse: " + s);
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
//                    Log.i("TAG", msg);
                    if (msg.equals("tokennotvalue")) {
                        SeekmePreference.save("tokennotvalue", false);
                        //initDialog(context);
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initDialog(context);
                                }
                            });

                        }
//                        Intent intent=new Intent();
//                        intent.setAction("tokenoff");
//                        context.sendBroadcast(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                int belong = SeekmePreference.getInt("belong");
                Log.d("beglong", "getParams: " + String.valueOf(belong));
                Log.d("beglong2", "getParams: " + SeekmePreference.getString("mycid"));

                hashMap.put("userId", userId);
                hashMap.put("lat", lat + "");
                hashMap.put("lng", lng + "");
                hashMap.put("clientId", SeekmePreference.getString("mycid"));
                hashMap.put("belong", String.valueOf(belong));
                hashMap.put("phone1", SeekmePreference.getString("phone1"));
                hashMap.put("jobnum", SeekmePreference.getString("jobnumber"));
                hashMap.put("name", SeekmePreference.getString("name"));


                //hashMap.put("belong", GlobalValues.sp.getInt("mycid",null));
                hashMap.put("", lng + "");
                return hashMap;

            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }


    /***
     * 上传图片
     ***/
    public void UpdatePicture(String filePath, String fileName) {
        String url = GlobalValues.baseUrl + "testUploadFile";
        HashMap<String, String> param = new HashMap<>();
        param.put("filename", fileName);
        Log.d("dreamkong", "filePath: "+filePath);
        File file = new File(filePath);
        FileRequest fileRequest = new FileRequest(url,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.i("你好", s);
                    }
                }, "file1", file, param);
        MyApplication.getHttpQueues().add(fileRequest);
    }

    /***
     * 从数据库获取  能监控的所有成员
     ***/
    public void getMyMemberList(final int belong, final int leaf, final Context context) {
        String url = GlobalValues.baseUrl + "user/search/member";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject JObject1 = new JSONObject(s);
                    String msg = JObject1.getString("msg");
                    if (msg.equals("tokennotvalue")) {
                        Log.d("token1", s);
                        // Log.d("what", GlobalValues.sp.getString("userId",null));


                    } else {
                        JSONArray JArray = new JSONObject(s).getJSONArray("data");
                        for (int i = 0; i < JArray.length(); i++) {
                            JSONObject JObject = JArray.getJSONObject(i);
                            User user = new User();
                            user.setName(JObject.getString("name"));
                            user.setGender(JObject.getInt("gender"));
                            user.setLeaf(JObject.getInt("leaf"));
                            user.setBelong(JObject.getInt("belong"));
                            user.setJobnumber(JObject.getString("jobnumber"));
                            user.setUserId(JObject.getString("userId"));

                            GlobalValues.MyMemberList.add(user);
                        }
                        if (context instanceof MyMemberTActivity) {
                            ((MyMemberTActivity) context).refresh(GlobalValues.MyMemberList);
                        } else if (context instanceof MyMemberHActivity) {
                            ((MyMemberHActivity) context).refresh(GlobalValues.MyMemberList);
                        }

                    }
                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("belong", belong + "");
                hashMap.put("leaf", leaf + "");
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /***
     * 从数据库获取  所有可获得定位人员位置信息
     ***/
    public void getRealLocList(final String jobnum, final int belong, final int leaf) {
        String url = GlobalValues.baseUrl + "realloc/search";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    String s1 = new JSONObject(s).getString("msg");
//                    Log.i("TAG", "onResponse: "+s1);

                    JSONArray JArray = new JSONObject(s).getJSONArray("data");
                    for (int i = 0; i < JArray.length(); i++) {

                        JSONObject JObject = JArray.getJSONObject(i);
                        if (JObject.getDouble("lat") != 0 && JObject.getDouble("lng") != 0) {
                            RealLoc loc = new RealLoc();
                            loc.setName(JObject.getString("name"));
                            loc.setPhone1(JObject.getString("phone1"));
                            loc.setBelong(JObject.getString("belong"));
                            loc.setUserId(JObject.getString("userId"));
                            loc.setLat(JObject.getDouble("lat"));
                            loc.setLng(JObject.getDouble("lng"));
                            loc.setJobnum(JObject.getString("jobnum"));

                            GlobalValues.RealLocList.add(loc);
                        }
                    }

                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("jobnum", jobnum);
                hashMap.put("belong", belong + "");
                hashMap.put("leaf", leaf + "");
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /* TODO 保存clientId */

    public void updateClientId(final String clientid) {
        String url = GlobalValues.baseUrl + "client/update";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("client记录更新成功")) {
                        Log.i("TAG", msg);
                    } else if (msg.equals("tokennotvalue")) {
                        Log.d("token1", "token失效");
                        // Log.d("what", GlobalValues.sp.getString("userId",null));


                    } else
                        Log.i("TAG", "client记录更新失败");
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
                }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("userid", SeekmePreference.getString("userId"));
                hashMap.put("clientid", clientid);
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    /**
     * 请求紧急救援的记录 第一步
     * <p>
     * 暂定 没人接收
     *
     * @param createtime
     * @param date
     * @param lat
     * @param lng
     * @param picurl
     * @param sosid
     */
    public void saveSosFirStep(final String createtime, final String date,
                               final String lat, final String lng,
                               final String picurl, final String sosid) {
        String url = GlobalValues.baseUrl + "sos/insert/firststep";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("sos记录添加成功")) {
                        Log.i("TAG", msg);
                    } else if (msg.equals("tokennotvalue")) {
                        Log.d("token1", "token失效");
                        // Log.d("what", GlobalValues.sp.getString("userId",null));


                    } else
                        Log.i("TAG", "sos记录添加失败");
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
                }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("seekerid", SeekmePreference.getString("userId"));
                hashMap.put("seekername", SeekmePreference.getString("name"));
                hashMap.put("sosid", sosid);
                hashMap.put("createtime", createtime);
                hashMap.put("date", date);
                hashMap.put("lat", lat);
                hashMap.put("lng", lng);
                hashMap.put("picurl", picurl);

                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }


    /**
     * 施救者接收
     * sos 相应步骤
     *
     * @param sosid
     */
    public void saveSosSecStep(final String sosid) {
        String url = GlobalValues.baseUrl + "sos/insert/secondstep";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("救援成功")) {
                        ToastUtil.setLongToast(MyApplication.getInstance().getCurrentActivity(), msg);


                        Log.i("TAG", msg);
                        //  进入 MainActivity  MainFragment
                    } else
                        ToastUtil.setLongToast(
                                MyApplication.getInstance().getCurrentActivity(),
                                "已经有人进行救援");
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
                }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("helpername", SeekmePreference.getString("name"));
                hashMap.put("helperid", SeekmePreference.getString("userId"));
                hashMap.put("sosid", sosid);
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void saveNotification(final String title, final String content,
                                 final String notiday, final String notitime) {
        String url = GlobalValues.baseUrl + "notification/add";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("添加通知成功")) {
                        Log.i("TAG", msg);
                    } else
                        Log.i("TAG", "存储失败");
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
                }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("sendername", SeekmePreference.getString("name"));
                hashMap.put("sendday", TimeUtil.getDate());
                hashMap.put("sendtime", TimeUtil.getNowTime());
                hashMap.put("notitime", notitime);
                hashMap.put("notiday", notiday);
                hashMap.put("title", title);
                hashMap.put("content", content);
//            TODO  用户在这加    hashMap.put("date",traceRecord.getDate());

                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void getAllNotiRecord() {
        GlobalValues.NotificationList.clear();
        String url = GlobalValues.baseUrl + "notification/getall";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONArray JArray = new JSONObject(s).getJSONArray("data");
                    for (int i = 0; i < JArray.length(); i++) {
                        JSONObject JObject = JArray.getJSONObject(i);
                        Notification notification = new Notification();
                        notification.setSenderName(JObject.getString("sendername"));
                        notification.setTitle(JObject.getString("title"));
                        notification.setContent(JObject.getString("content"));
                        notification.setNotitime(JObject.getString("notitime"));
                        notification.setNotiday(JObject.getString("notiday"));
                        notification.setSendday(JObject.getString("sendday"));
                        notification.setSendtime(JObject.getString("sendtime"));

                        GlobalValues.NotificationList.add(notification);
                    }

                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {

            // 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }
        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void getArticlecList(final String type) {
        String url = GlobalValues.baseUrl + "article/find";
        GlobalValues.ArticleList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    String s1 = new JSONObject(s).getString("msg");
                    JSONArray JArray = new JSONObject(s).getJSONArray("data");
                    for (int i = 0; i < JArray.length(); i++) {

                        JSONObject JObject = JArray.getJSONObject(i);
                        Article article = new Article();

                        article.setPaperid(JObject.getInt("paperid"));
                        article.setTitle(JObject.getString("title"));
                        article.setPublisher(JObject.getString("publisher"));
                        article.setType(JObject.getInt("type"));
                        article.setLikenumber(JObject.getInt("likenumber"));
                        article.setContent(JObject.getString("content"));
                        GlobalValues.ArticleList.add(article);

                    }

                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("type", type);

                //Log.d("database1", "getParams: "+token);
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void articleLikeAddOne(final Integer paperid) {
        String url = GlobalValues.baseUrl + "article/addlike";
        GlobalValues.ArticleList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("点赞添加成功")) {
                        Log.i("点赞添加成功", "onResponse: ");
                    } else
                        Log.i("点赞添加失败", "onResponse: ");

                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("paperid", paperid + "");
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void addComment(final int paperid, final String name, final String comment) {
        String url = GlobalValues.baseUrl + "articlecomment/add";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject JObject = new JSONObject(s);
                    String msg = JObject.getString("msg");
                    if (msg.equals("评论成功")) {
                        Log.i("log:", msg);
                    } else
                        Log.i("log:", msg);

                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String paper_id = Integer.toString(paperid);
                Log.d("paperid", paper_id);
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("parentsid", paper_id);
                hashMap.put("commentusername", name);
                hashMap.put("comment", comment);
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void getVisibleComment(final Integer paperid) {
        String url = GlobalValues.baseUrl + "articlecomment/getvisible";

        GlobalValues.ArticleCommentList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    String s1 = new JSONObject(s).getString("msg");
                    JSONArray JArray = new JSONObject(s).getJSONArray("data");
                    for (int i = 0; i < JArray.length(); i++) {

                        JSONObject JObject = JArray.getJSONObject(i);
                        ArticleComment articleComment = new ArticleComment();

                        articleComment.setComment(JObject.getString("comment"));
                        articleComment.setCommentusername(JObject.getString("commentusername"));

                        GlobalValues.ArticleCommentList.add(articleComment);

                    }

                } catch (JSONException JException) {
                    JException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {// 定义请求数据
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
                // 自定义请求头 X-YAuth-Token
                headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
                String paper_id = Integer.toString(paperid);
                hashMap.put("parentsid", paper_id);
                return hashMap;
            }

        };
        // 将请求添加到队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void initDialog(final Context context) {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("登录过期");
        normalDialog.setMessage("请重新登录，谢谢");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SeekmePreference.save("userId", "");
                        SeekmePreference.save("name", "");
                        SeekmePreference.save("Emergency_contact1", "");
                        SeekmePreference.save("Emergency_contact2", "");
                        SeekmePreference.save("jobnumber", "");
                        SeekmePreference.save("job", "");
                        SeekmePreference.save("phone1", "");
                        SeekmePreference.save("password", "");
                        SeekmePreference.save("token", "");
                        SeekmePreference.save("Loginstate", false);
                        SeekmePreference.save("gender", 0);
                        SeekmePreference.save("leaf", 0);
                        SeekmePreference.save("belong", 0);
                        // LocalUser.getInstance().setPhoto("");

                        Intent logoutIntent = new Intent(context, LoginActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(logoutIntent);
                        ((MainActivity) context).finish();

                    }
                });
        normalDialog.setCancelable(false);
        normalDialog.show();
    }
}
