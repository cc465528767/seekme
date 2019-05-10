package com.zzzealous.seekme.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zzzealous on 2018/2/22.
 */

public class MyOrientationListener implements SensorEventListener {
    private SensorManager mSensorManager;//传感器管理者
    private Context mContext;
    private Sensor  mSensor;//传感器

    private float lastX;//X轴

    public MyOrientationListener(Context context){

        this.mContext = context;
    }

    public void start(){
        mSensorManager = (SensorManager) mContext
                 .getSystemService(Context.SENSOR_SERVICE);//从系统服务获取传感器服务
        if(mSensorManager!=null){
            //获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        }

        if(mSensor!= null) {
            //创建成功绑定  开始监听
            mSensorManager.registerListener(this,mSensor,
                     SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //当方向发生变化 event会携带一个 orientation类型的传感器
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float x = event.values[SensorManager.DATA_X];

            if(Math.abs(x - lastX) > 1.0){
                //回调 通知主界面更新
                if(mOnOrientationListener!=null){
                    mOnOrientationListener.onOrientationChanged(x);
                }
            }
            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public OnOrientationListener mOnOrientationListener;

    public void setmOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public interface OnOrientationListener{
        void onOrientationChanged(float x);
    }
}
