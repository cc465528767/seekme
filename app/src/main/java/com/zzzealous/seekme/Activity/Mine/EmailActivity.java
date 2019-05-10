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

public class EmailActivity extends Activity {

    private EditText etEmail;
    private String mail;

    private Button comfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        etEmail = (EditText) findViewById(R.id.activity_Email_newEmail);
        mail = etEmail.getText().toString();

        comfirm = (Button) findViewById(R.id.activity_Email_confirm);

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = etEmail.getText().toString();
                if (checkmail()) {
                    String url = GlobalValues.baseUrl + "user/change/email";

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject JObject = new JSONObject(s);
                                String msg = JObject.getString("msg");
                                ToastUtil.setLongToast(EmailActivity.this, msg);
                            } catch (JSONException e) {
                                ToastUtil.setLongToast(EmailActivity.this, "修改失败");
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    volleyError.printStackTrace();
                                }
                            }
                    ) { public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new LinkedHashMap<>();
                        // 自定义请求头 X-YAuth-Token
                        headers.put("X-YAuth-Token", SeekmePreference.getString("token"));
                        return headers;
                    }
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> hashMap = new HashMap<>();
                            hashMap.put("phone", SeekmePreference.getString("phone1"));
                            hashMap.put("email", mail);

                            return hashMap;
                        }
                    };
                    MyApplication.getHttpQueues().add(request);
                }
            }
        });

    }

    public boolean checkmail() {
        if (mail.isEmpty()) {
            ToastUtil.setLongToast(EmailActivity.this, "输入为空");
            return false;
        } else return true;
    }
}
