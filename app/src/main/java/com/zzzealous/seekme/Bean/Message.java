package com.zzzealous.seekme.Bean;

import java.util.Date;

public class Message {
    public static final int RECEIVER_TYPE_NONE = 1;
    // 发送给群的
    public static final int RECEIVER_TYPE_GROUP = 2;

    public static final int TYPE_STR = 1; // 字符串类型
    public static final int TYPE_PIC = 2; // 图片类型
    public static final int TYPE_FILE = 3; // 文件类型
    public static final int TYPE_AUDIO = 4; // 语音类型

    private String id;
    private String sentid;
    private String receiveid;




    private String content;

    // 附件

    private String attach;

    // 消息类型

    private int type;


    private Date createTime;

    private Date updatetime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getSentid() {
        return sentid;
    }

    public void setSentid(String sentid) {
        this.sentid = sentid;
    }

    public String getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
