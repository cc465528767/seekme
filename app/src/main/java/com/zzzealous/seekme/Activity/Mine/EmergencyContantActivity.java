package com.zzzealous.seekme.Activity.Mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class EmergencyContantActivity extends AppCompatActivity {

    EditText etname1;
    EditText etname2;
    EditText etphone1;
    EditText etphone2;

    Button confirm1;
    Button confirm2;

    String phone1;
    String phone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contant);
        etphone1 = (EditText) findViewById(R.id.emergency_phone1);
        etphone2 = (EditText) findViewById(R.id.emergency_phone2);

        confirm1 = (Button) findViewById(R.id.emergency_confirm1);
        confirm2 = (Button) findViewById(R.id.emergency_confirm2);

        confirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone1 = etphone1.getText().toString();
                if (checkinput(phone1)) {
                    String url = GlobalValues.baseUrl + "user/change/emergency";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject JObject = new JSONObject(s);
                                String msg = JObject.getString("msg");
                                if (msg.equals("修改成功")){
                                    SeekmePreference.save("Emergency_contact1",phone1);
                                }

                                ToastUtil.setLongToast(EmergencyContantActivity.this, msg);
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
                            hashMap.put("ephone", phone1);

                            return hashMap;
                        }
                    };
                    MyApplication.getHttpQueues().add(request);
                }
            }
        });

        confirm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone2 = etphone2.getText().toString();
                if (checkinput(phone2)) {
                    String url = GlobalValues.baseUrl + "user/change/emergency1";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject JObject = new JSONObject(s);
                                String msg = JObject.getString("msg");
                                if (msg.equals("修改成功")){
                                    SeekmePreference.save("Emergency_contact2",phone2);
                                }
                                ToastUtil.setLongToast(EmergencyContantActivity.this, msg);
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
                            headers.put("X-YAuth-Token",SeekmePreference.getString("token"));
                            return headers;
                        }
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> hashMap = new HashMap<>();
                            hashMap.put("phone", SeekmePreference.getString("phone1"));
                            hashMap.put("ephone", phone2);

                            return hashMap;
                        }
                    };
                    MyApplication.getHttpQueues().add(request);
                }
            }
        });
    }



    public boolean checkinput(String phone) {
        if (!phone.isEmpty()) return true;
        else {
            ToastUtil.setLongToast(EmergencyContantActivity.this, "手机不能为空");
            return false;
        }
    }
}
