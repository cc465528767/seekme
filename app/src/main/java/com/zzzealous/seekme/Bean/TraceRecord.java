package com.zzzealous.seekme.Bean;

/**
 * Created by zzzealous on 2018/3/1.
 */

public class TraceRecord {
    private String userId;
    private String trace_starttime;
    private String trace_realtime;
    private String trace_pic_url;
    private String date;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrace_starttime() {
        return trace_starttime;
    }

    public void setTrace_starttime(String trace_starttime) {
        this.trace_starttime = trace_starttime;
    }

    public String getTrace_realtime() {
        return trace_realtime;
    }

    public void setTrace_realtime(String trace_realtime) {
        this.trace_realtime = trace_realtime;
    }


    public String getTrace_pic_url() {
        return trace_pic_url;
    }

    public void setTrace_pic_url(String trace_pic_url) {
        this.trace_pic_url = trace_pic_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
