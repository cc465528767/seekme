package com.zzzealous.seekme.Activity.Mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElectActivity extends AppCompatActivity {

    LinearLayout newElect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elect);
        String url = GlobalValues.baseUrl + "vote/test";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                try {
//                    JSONObject JObject = new JSONObject(s);
////                    String msg = JObject.getString("msg");
//                    Show = (TextView) findViewById(R.id.show);
//                    Show.setText("123");
//                } catch (JSONException e) {
//                    Show = (TextView) findViewById(R.id.show);
//                    Show.setText("321");
//                    e.printStackTrace();
//                }

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
                return hashMap;
            }
        };
        MyApplication.getHttpQueues().add(request);



    }
}
