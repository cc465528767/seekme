package com.zzzealous.seekme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.LocalUser;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.Utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity implements View.OnClickListener {

    EditText etLoginMobile;
    EditText etLoginPassword;
    Button btLogin;
    Button bt1;
    private String loginNumber;//登录手机号码
    private String loginPassword;//登录密码

    private static final int LOGIN_RIGHT = 0x01;
    private static final int LOGIN_WRONG = 0x02;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_RIGHT:

                    ToastUtil.setLongToast(LoginActivity.this,"登陆成功");
                    SharedPreferences sp = getSharedPreferences("loginToken",  Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    /*   LocalUser.getInstance().setGender(JObject.getInt("gender"));
            LocalUser.getInstance().setLeaf(JObject.getInt("leaf"));
            LocalUser.getInstance().setBelong(JObject.getInt("belong"));
            LocalUser.getInstance().setUserId(JObject.getString("userId"));*/
                    editor.putBoolean("tokennotvalue",true);
                    editor.putString("name",LocalUser.getInstance().getName());
                    editor.putInt("gender",LocalUser.getInstance().getGender());
                    editor.putInt("leaf",LocalUser.getInstance().getLeaf());
                    editor.putInt("belong",LocalUser.getInstance().getBelong());
                    editor.putString("jobnumber",LocalUser.getInstance().getJobnumber());
                    editor.putString("job",LocalUser.getInstance().getJob());
                    editor.putString("mail",LocalUser.getInstance().getMail());
                    editor.putString("Emergency_contact1",LocalUser.getInstance().getEmergency_contact1());
                    editor.putString("Emergency_contact2",LocalUser.getInstance().getEmergency_contact2());

                    editor.putString("userId",LocalUser.getInstance().getUserId());
                    editor.putString("phone1",loginNumber);
                    editor.putString("password",loginPassword);
                    editor.putString("token",LocalUser.getInstance().getToken());
                    editor.putBoolean("Loginstate",true);
                    editor.commit();
                    Log.d("lalalala", sp.getString("token",null));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    break;
                case LOGIN_WRONG:
                    ToastUtil.setLongToast(LoginActivity.this, "用户名或密码错误");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("loginPost");
    }

    private void initComponent() {
        etLoginMobile = (EditText) findViewById(R.id.et_login_mobile);

        etLoginPassword = (EditText) findViewById(R.id.et_login_password);

        btLogin = (Button) findViewById(R.id.bt_login);
        bt1 = findViewById(R.id.button1);

        btLogin.setOnClickListener(this);
        bt1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login: // 登录
                if (checkInput()) {
                    // 请求地址
                    final String url = GlobalValues.baseUrl + "jwt/token";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RequestFuture future = RequestFuture.newFuture();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, future, future) {
                                // 定义请求数据
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> hashMap = new HashMap<>();
                                    hashMap.put("phone1", loginNumber);
                                    hashMap.put("password", loginPassword);
                                    return hashMap;
                                }
                            };
                            stringRequest.setTag("loginPost");
                            MyApplication.getHttpQueues().add(stringRequest);
                            String s;
                            try {
                                s = (String) future.get();
                                resolveJSON(s);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                              e.printStackTrace();  }
                            onResponse();
                        }
                    }).start();
                }
                break;

            case R.id.button1:
                File sdDir = null;
                boolean sdCardExist = Environment.getExternalStorageState()
                         .equals(android.os.Environment.MEDIA_MOUNTED);
                if (sdCardExist) {
                    sdDir = Environment.getExternalStorageDirectory();//获取根目录
                    ToastUtil.setShortToast(LoginActivity.this,sdDir.toString());
                }
                break;

        }
    }

    public void resolveJSON(String s){
        try {
            JSONObject JObject = new JSONObject(s);
            String msg = JObject.getString("msg");
            LocalUser.getInstance().setMsg(msg);
            if (msg.equals("查找失败")) {
                return;
            }
           /* else if (msg==null) {
                return;
            }*/
            String token=JObject.getString("token");
            LocalUser.getInstance().setToken(token);
            JObject = JObject.getJSONObject("data");
            LocalUser.getInstance().setName(JObject.getString("name"));
            LocalUser.getInstance().setGender(JObject.getInt("gender"));
            LocalUser.getInstance().setLeaf(JObject.getInt("leaf"));
            LocalUser.getInstance().setBelong(JObject.getInt("belong"));
            LocalUser.getInstance().setUserId(JObject.getString("userId"));
            LocalUser.getInstance().setJobnumber(JObject.getString("jobnumber"));
            LocalUser.getInstance().setJob(JObject.getString("job"));
            LocalUser.getInstance().setMail(JObject.getString("mail"));
            LocalUser.getInstance().setEmergency_contact1(JObject.getString("emergency_contact1"));
            LocalUser.getInstance().setEmergency_contact2(JObject.getString("emergency_contact2"));

            LocalUser.getInstance().setPhone1(loginNumber);
            LocalUser.getInstance().setPassword(loginPassword);


        } catch (JSONException JException){
            JException.printStackTrace();
        }
    }


    public void onResponse(){
        Message msg = new Message();
        if (LocalUser.getInstance().getMsg().equals("查找成功")) {
            msg.what = LOGIN_RIGHT;
        } else {
            msg.what = LOGIN_WRONG;
        }
        mHandler.sendMessage(msg);
    }
    /***
     * 检查输入
     **/
    private boolean checkInput(){
        loginNumber = etLoginMobile.getText().toString();
        loginPassword = etLoginPassword.getText().toString();

        if (!loginNumber.isEmpty() && !loginPassword.isEmpty()) {
            return true;
        } else if (loginNumber.isEmpty()) {
            ToastUtil.setLongToast(LoginActivity.this,"手机号码不能为空");
        } else if (loginPassword.isEmpty()) {
            ToastUtil.setLongToast(LoginActivity.this,"密码不能为空");
        }
        return false;
    }
}
