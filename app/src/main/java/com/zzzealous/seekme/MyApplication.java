package com.zzzealous.seekme;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zzzealous.seekme.Activity.MainActivity;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.Fragment.MainFragment;
import com.zzzealous.seekme.GTPush.GTDataManager;
import com.zzzealous.seekme.UI.ReceiveSosDialog;

import org.json.JSONObject;

/**
 * Created by zzzealous on 2018/2/6.
 */

public class MyApplication extends Application {

    private static DemoHandler handler;
    public static RequestQueue queues;


    @Override
    public void onCreate() {
        super.onCreate();
        //实例化  get全局上下文
        queues = Volley.newRequestQueue(getApplicationContext());

        mInstance = this;
        initGlobeActivity();

        if (handler == null) {
            handler = new DemoHandler();
        }

    }

    //返回requestsqueue
    public static RequestQueue getHttpQueues() { return queues; }

/* TODO 重要代码更新部分  activity栈写入  取currentActivity */

    private static MyApplication mInstance;
    private Activity app_activity = null;

    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                app_activity = activity;
//                Log.e("onActivityCreated===", app_activity + "");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                app_activity = activity;
//                Log.e("onActivityDestroyed===", app_activity + "");
            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
//                app_activity = activity;
//                Log.e("onActivityStarted===", app_activity + "");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                app_activity = activity;
                Log.e("onActivityResumed===", app_activity + "");
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                app_activity = activity;
//                Log.e("onActivityPaused===", app_activity + "");
            }

            @Override
            public void onActivityStopped(Activity activity) {
//                app_activity = activity;
//                Log.e("onActivityStopped===", app_activity + "");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    /**
     * 获取实例
     * @return
     */
    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    public Activity getCurrentActivity() {
        return app_activity;
    }


    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static class DemoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                try{
                    JSONObject JObject = new JSONObject((String)msg.obj) ;
                    final String name = JObject.getString("name");
                    final String phone = JObject.getString("phone");
                    final String jobnum = JObject.getString("jobnum");
                    final Double lat = Double.parseDouble(JObject.getString("seekerLat"));
                    final Double lng = Double.parseDouble(JObject.getString("seekerLng"));


                    final String SeekerCid = JObject.getString("seekerCid");
                    final String sosid = JObject.getString("sosid");
                    new ReceiveSosDialog(MyApplication.getInstance().getCurrentActivity()
                             , name, phone, jobnum, 1,
                             new ReceiveSosDialog.OnDialogButtonClickListener() {
                                 @Override
                                 public void onDialogButtonClick(int requestCode, boolean isPositive) {
                                     if(isPositive){
                                         DatabaseManager.getInstance().saveSosSecStep(sosid);
                                         // TODO 接收OK  界面跳转??  getRoute


                                         MainActivity activity = (MainActivity) MyApplication.getInstance().getCurrentActivity();
                                         MainFragment fragment = activity.getmMainFragment();
                                         fragment.transToHelperStatus(name,phone,jobnum,lat,lng);
                                         GTDataManager.getInstance().sendBackMsg(SeekerCid);

                                     }
                                 }
                             }).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

                case 2:
                    //TODO 去除开始巡检按钮   显示救援人员信息  上边有拨打电话  和 已到达   已到达按钮点击之后 显示开始巡检按钮
                   try{

                       JSONObject JObject = new JSONObject((String)msg.obj) ;
                       String name = JObject.getString("helpername");
                       String phone = JObject.getString("helperphone");
                       String jobnum = JObject.getString("helperjobnum");

                       MainActivity activity = (MainActivity) MyApplication.getInstance().getCurrentActivity();
                       MainFragment fragment = activity.getmMainFragment();
                       fragment.transToSeekerStatus(name,phone,jobnum);
                   }catch (Exception e){
                        e.printStackTrace();
                   }

                break;
            }
        }
    }
}
