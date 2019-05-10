package com.zzzealous.seekme.Activity.Mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.Utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChangePasswordActivity extends Activity {

    private Button comfirm;

    private EditText etOldPsd;
    private EditText etNewPsd1;
    private EditText etNewPsd2;

    private String oldPsd;
    private String newPsd1;
    private String newPsd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOldPsd = (EditText) findViewById(R.id.activity_changepassword_oldpsd);
        etNewPsd1 = (EditText) findViewById(R.id.activity_changepassword_newpsd1);
        etNewPsd2 = (EditText) findViewById(R.id.activity_changepassword_newpsd2);

        comfirm = (Button) findViewById(R.id.ChangePassword_comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkpassword()) {
                    String url = GlobalValues.baseUrl + "user/change/password";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject JObject = new JSONObject(s);
                                String msg = JObject.getString("msg");
                                if (msg.equals("修改成功")){
                                    SeekmePreference.save("password",newPsd1);
                                }
                                ToastUtil.setLongToast(ChangePasswordActivity.this,msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    volleyError.printStackTrace();
                                }
                            }) {
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new LinkedHashMap<>();
                            // 自定义请求头 X-YAuth-Token
                            headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                            return headers;
                        }
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> hashMap = new HashMap<>();
                            hashMap.put("phone", SeekmePreference.getString("phone1"));
                            hashMap.put("oldpsd", oldPsd);
                            hashMap.put("newpsd", newPsd1);

                            return hashMap;
                        }
                    };
                    MyApplication.getHttpQueues().add(request);
                }
            }
        });
    }
//    public void onClick(View view) {
//        String url = GlobalValues.baseUrl + "user/change/password";
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject JObject = new JSONObject(s);
//                    String msg = JObject.getString("msg");
//                    if (msg.equals("1")) {
//                        Log.i("TAG", "修改成功");
//                    } else
//                        Log.i("TAG", "存储失败");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        volleyError.printStackTrace();
//                    }
//                }) {
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> hashMap = new HashMap<>();
//                hashMap.put("phone", LocalUser.getInstance().getPhone1());
//                hashMap.put("oldpsd", oldPsd);
//                hashMap.put("newpsd", newPsd1);
////               hashMap.put("picurl",traceRecord.getTrace_pic_url());
////               hashMap.put("date",traceRecord.getDate());
//                return hashMap;
//            }
//        };
//    }

    public boolean checkpassword() {
        oldPsd = etOldPsd.getText().toString();
        newPsd1 = etNewPsd1.getText().toString();
        newPsd2 = etNewPsd2.getText().toString();
        if (!oldPsd.isEmpty() && !newPsd2.isEmpty() && !newPsd1.isEmpty() && newPsd1.equals(newPsd2)) {
            return true;
        } else if (oldPsd.isEmpty()) {
            ToastUtil.setLongToast(ChangePasswordActivity.this, "原密码不能为空");
        } else if (newPsd1.isEmpty()) {
            ToastUtil.setLongToast(ChangePasswordActivity.this, "新密码不能为空");
        } else if (oldPsd.isEmpty()) {
            ToastUtil.setLongToast(ChangePasswordActivity.this, "重复新密码不能为空");
        } else if (!newPsd1.equals(newPsd2)) {
            ToastUtil.setLongToast(ChangePasswordActivity.this, "新密码不一致");

        }
        return false;
    }

    public void resolveJSON(String s) {
        try {
            JSONObject JObject = new JSONObject(s);
            String msg = JObject.getString("msg");
            ToastUtil.setLongToast(ChangePasswordActivity.this, msg.toString());
            return;
        } catch (JSONException JException) {
            ToastUtil.setLongToast(ChangePasswordActivity.this, "其他错误");
            JException.printStackTrace();
            return;
        }
    }
}
