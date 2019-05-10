package com.zzzealous.seekme.Activity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zzzealous.seekme.Activity.Chat.ChatMessageActivity;
import com.zzzealous.seekme.Adapter.UsersAdapter;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Bean.User;

import com.zzzealous.seekme.Preference.SeekmePreference;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.UI.ClearEditText;
import com.zzzealous.seekme.UI.SideBar;
import com.zzzealous.seekme.Utils.Cn2Spell;
import com.zzzealous.seekme.Utils.PinyinUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MineAddressActivity extends AppCompatActivity implements Callback {
    private List<User> users = new ArrayList<>();
    private List<User> users2 = new ArrayList<>();
    private UsersAdapter usersAdapter;
    private SideBar sideBar;
    private ClearEditText mClearEditText;
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_address);
        sendRequestWithOkhttp();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.userlists);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        usersAdapter = new UsersAdapter(users2);
        usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                    Intent intent = new Intent(MineAddressActivity.this, ChatMessageActivity.class);
                    intent.putExtra("name",users2.get(position).getName());
                    Log.d("点击内容", position+"  "+users2.get(position).getName());
                    startActivity(intent);}


        });
        sideBar = (SideBar) findViewById(R.id.side_bar);
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < users2.size(); i++) {
                    if (selectStr.equalsIgnoreCase(users2.get(i).getFirstLetter())) {
                        //MoveToPosition(linearLayoutManager,i);
                        linearLayoutManager.scrollToPosition(i);// 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });

        recyclerView.setAdapter(usersAdapter);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }



    private void sendRequestWithOkhttp(){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url( GlobalValues.baseUrl + "/user/web/all_user")
                .addHeader("X-YAuth-Token", SeekmePreference.getString("token"))
                .build();
        client.newCall(request).enqueue(this);
    }

    private void refresh(List<User> userList) {
        if (usersAdapter != null) {
            usersAdapter.refresh(userList);
            Collections.sort(userList);
        }
    }

    private void parseUserGson(String responseDate) {
        Gson gson = new Gson();
        users = gson.fromJson(responseDate,
                new TypeToken<List<User>>() {
                }.getType());
        for (User user : users) {
            Log.d("MainActivity", "id is " + user.getId());
            Log.d("MainActivity", "phone is " + user.getPhone1());
            users2.add(new User(user.getName()));

        }
    }

    @Override
    public void onFailure(Request request, IOException e) {

    }

    @Override
    public void onResponse(Response response) throws IOException {
        String responseDate = response.body().string();
        parseUserGson(responseDate);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh(users2);
            }
        });
    }
    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }
    private void filterData(String filterStr) {
//        MoveToPosition(linearLayoutManager,0);
        List<User> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = users2;
        } else {
            filterDateList.clear();
            for (User user : users2) {
                String name = user.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(user);
                }
            }
        }
        refresh(filterDateList);
    }

}
