package com.zzzealous.seekme.Bean;

/**
 * Created by zzzealous on 2018/7/4.
 */

public class Notification {
    private String senderName;
    private String sendday;
    private String sendtime;
    private String notiday;
    private String notitime;
    private String title;
    private String content;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSendday() {
        return sendday;
    }

    public void setSendday(String sendday) {
        this.sendday = sendday;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getNotiday() {
        return notiday;
    }

    public void setNotiday(String notiday) {
        this.notiday = notiday;
    }

    public String getNotitime() {
        return notitime;
    }

    public void setNotitime(String notitime) {
        this.notitime = notitime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
