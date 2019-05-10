package com.zzzealous.seekme.Bean;

/**
 * Created by zzzealous on 2018/2/6.
 */

// 本地用户 单例模式
//{"id":3,"user_id":"1333",
// "name":"李组长","phone1":"13333","password":"123",
// "leaf":2,"belong":120,"gender":1}

public class LocalUser extends User {

    private String msg;
    private String token;
    private static final LocalUser instance = new LocalUser();

    public static LocalUser getInstance() {
        return instance;
    }
    private String s="13131313";

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
