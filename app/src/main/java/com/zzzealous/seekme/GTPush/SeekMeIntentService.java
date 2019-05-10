package com.zzzealous.seekme.GTPush;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.MyApplication;

import org.json.JSONObject;


/**
 * 继承 GTIntentService 接收来自个推的消息,所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class SeekMeIntentService extends GTIntentService {
    SharedPreferences sp=MyApplication.getInstance().getSharedPreferences("loginToken",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    public SeekMeIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();


        String PushType = null;

        if (payload != null){
            String data = new String(payload);
            try {
                JSONObject JObject = new JSONObject(data) ;

                Log.e(TAG,  data);
                PushType = JObject.getString("type");

                switch (PushType){
                    case "1":
                        Log.e(TAG,  JObject.getString("name")+"，正在发起请求");

                        sendMessage(data, 1);

                        break;
                    case "2":

                        sendMessage(data,2);

                        break;

                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        String cid = clientid;
        Log.e(TAG, "大家好|--❤❤❤❤❤❤❤❤❤❤❤--->|"+ "clientid = " + cid);
        editor.putString("mycid",cid);
        editor.commit();
        GlobalValues.MyCid = clientid;
        Log.d("getui1", "getParams: "+cid);

        DatabaseManager.getInstance().updateClientId(cid);
        Log.d("getui", "getParams: "+ sp.getString("mycid",null));

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        Log.e(TAG, msg+ "你好吗|--❤❤❤❤❤❤❤❤❤❤❤--->|");

//        NotificationManager manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("横幅通知");
//        builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setSmallIcon(R.mipmap.notification_icon);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_icon));
//        builder.setAutoCancel(true);
//        Notification notification = builder.build();
//        manger.notify(6, notification);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {




    }


    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        MyApplication.sendMessage(msg);
    }
}
