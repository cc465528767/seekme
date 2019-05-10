package com.zzzealous.seekme.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zzzealous.seekme.UI.ReceiveSosDialog;


/**
 * Created by zzzealous on 2018/3/16.
 */

public class MyReceivers extends BroadcastReceiver {
    private static final String TAG = "Receiver 测试";

    private String userName;
    private String phone;
    private String jobNum;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            userName = bundle.getString("userName");
            phone = bundle.getString("phone");
            jobNum = bundle.getString("jobNum");

            new ReceiveSosDialog(context, userName, phone, jobNum, 0, new ReceiveSosDialog.OnDialogButtonClickListener() {
                @Override
                public void onDialogButtonClick(int requestCode, boolean isPositive) {
                    if (isPositive){


                    }
                }
            }).show();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
