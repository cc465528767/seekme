package com.zzzealous.seekme.Utils;

import java.util.Calendar;

/**
 * Created by zzzealous on 2018/2/27.
 */

public class TimeUtil {

    /**
     * 将秒转化为时分秒
     * **/
    public static String formatTime(int recordingTime){
        String hh=recordingTime/3600>9?recordingTime/3600+"":"0"+recordingTime/3600;
        String  mm=(recordingTime % 3600)/60>9?(recordingTime % 3600)/60+"":"0"+(recordingTime % 3600)/60;
        String ss=(recordingTime % 3600) % 60>9?(recordingTime % 3600) % 60+"":"0"+(recordingTime % 3600) % 60;
        return hh+" : "+mm+" : "+ss;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        Calendar calendar=Calendar.getInstance();
        String hh , mm , ss ;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if(hour>=0 && hour<10)
            hh = "0" + hour;
        else
            hh = hour+"";

        if(minute>=0 && minute<10)
            mm = "0" + minute;
        else
            mm = minute+"";

        if(second>=0 && second<10)
            ss = "0" + second;
        else
            ss = second+"";

        return hh+" : "+ mm +" : "+ ss ;
    }

    /**
     * 获取Date
     * @return
     */
    public static String getDate(){

        Calendar calendar=Calendar.getInstance();
        String yyyy, MM, dd;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        yyyy = year+"";

        if(month<10)
            MM = "0" + month;
        else
            MM = month+"";

        if(day<10)
            dd = "0" + day;
        else
            dd = day+"";

        return yyyy+"-"+MM+"-"+dd;
    }



}
