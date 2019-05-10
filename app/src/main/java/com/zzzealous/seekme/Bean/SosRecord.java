package com.zzzealous.seekme.Bean;

/**
 * Created by zzzealous on 2018/4/1.
 */

public class SosRecord {
    private String userId;
    private String helper_name;
    private String seeker_name;
    private String sos_starttime;
    private String sos_pic_url;
    private String date;

    public String getHelper_name() {
        return helper_name;
    }

    public void setHelper_name(String helper_name) {
        this.helper_name = helper_name;
    }

    public String getSeeker_name() {
        return seeker_name;
    }

    public void setSeeker_name(String seeker_name) {
        this.seeker_name = seeker_name;
    }

    public String getSos_starttime() {
        return sos_starttime;
    }

    public void setSos_starttime(String sos_starttime) {
        this.sos_starttime = sos_starttime;
    }

    public String getSos_pic_url() {
        return sos_pic_url;
    }

    public void setSos_pic_url(String sos_pic_url) {
        this.sos_pic_url = sos_pic_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
