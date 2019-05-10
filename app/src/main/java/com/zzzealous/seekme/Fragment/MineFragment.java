package com.zzzealous.seekme.Fragment;



import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zzzealous.seekme.Activity.LoginActivity;
import com.zzzealous.seekme.Activity.Mine.MineAddressActivity;
import com.zzzealous.seekme.Activity.Mine.MineCompanyActivity;
import com.zzzealous.seekme.Activity.Mine.MineHelpingAcitivity;
import com.zzzealous.seekme.Activity.Mine.MineMineActivity;
import com.zzzealous.seekme.Activity.Mine.MineSettingActivity;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.LocalUser;
import com.zzzealous.seekme.MyApplication;
import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    //我 公司、设置、通讯录、收藏、帮助、退出 初始化
    private LinearLayout mine;
    private LinearLayout mine_company;
    private LinearLayout mine_setting;
    private LinearLayout mine_addressbook;
    //private LinearLayout mine_collection;
    private LinearLayout mine_helping;
    private LinearLayout mine_lognout;
    private String detail;
    private TextView name;

    //评选 投票 暂时制作位置————2018.7.4
    private LinearLayout elect;
    private LinearLayout vote;
    private TextView Show;


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, container, false);


        //评选 投票 暂时制作位置————2018.7.4
        {
            String url = GlobalValues.baseUrl + "vote/test";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject JObject = new JSONObject(s);
//                    zdString msg = JObject.getString("msg");
//                        LocalUser.getInstance().setS("2424");
                        LocalUser.getInstance().setS(s);
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("phone", SeekmePreference.getString("phone1"));
                    return hashMap;
                }
            };

            MyApplication.getHttpQueues().add(request);


            //- - - - - -“我”界面中点击“评选” 跳转进入该界面- - - - -


        }


        //- - - - - -“我”界面中点击“我” 跳转进入该界面- - - - -

        mine = view.findViewById(R.id.mine);
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MineMineActivity.class);
                startActivity(intent);
            }
        });

        //- - - - - -“我”界面中点击“公司” 跳转进入该界面- - - - - -
        mine_company = view.findViewById(R.id.mine_company);
        mine_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MineCompanyActivity.class);
                startActivity(intent);
            }
        });


        //- - - - - -“我”界面中点击“设置” 跳转进入该界面- - - - - -
        mine_setting = view.findViewById(R.id.mine_setting);
        mine_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MineSettingActivity.class);
                startActivity(intent);
            }
        });
        //- - - - - -“我”界面中点击“通讯录” 跳转进入该界面- - - - - -
        mine_addressbook = view.findViewById(R.id.mine_addressbook);
        mine_addressbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MineAddressActivity.class);
                startActivity(intent);
            }
        });


        //- - - - - -“我”界面中点击“收藏” 跳转进入该界面- - - - - -
       /* mine_collection = view.findViewById(R.id.mine_collection);
        mine_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MineCollectionActivity.class);
                startActivity(intent);
            }
        });
*/
        //- - - - - -“我”界面中点击“帮助” 跳转进入该界面- - - - - -
        mine_helping = view.findViewById(R.id.mine_helping);
        mine_helping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MineHelpingAcitivity.class);
                startActivity(intent);
            }
        });

        //- - - - - -“我”界面中点击“退出登录” 跳转进入该界面- - - - - -
        mine_lognout = view.findViewById(R.id.mine_lognout);
        mine_lognout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SeekmePreference.save("userId","");
                SeekmePreference.save("name","");
                SeekmePreference.save("Emergency_contact1","");
                SeekmePreference.save("Emergency_contact2","");
                SeekmePreference.save("jobnumber","");
                SeekmePreference.save("job","");
                SeekmePreference.save("phone1","");
                SeekmePreference.save("password","");
                SeekmePreference.save("token","");
                SeekmePreference.save("Loginstate",false);
                SeekmePreference.save("gender",0);
                SeekmePreference.save("leaf",0);
                SeekmePreference.save("belong",0);
               // LocalUser.getInstance().setPhoto("");

                Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent
                );
            }
        });

        return view;

    }

}
