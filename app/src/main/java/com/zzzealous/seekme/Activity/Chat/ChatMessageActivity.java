package com.zzzealous.seekme.Activity.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzzealous.seekme.R;

public class ChatMessageActivity extends AppCompatActivity {
private TextView title;
private String name;
private LinearLayout linearLayout_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        title = (TextView) findViewById(R.id.title_name);
        linearLayout_right=(LinearLayout)findViewById(R.id.sos_button);
        linearLayout_right.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
       name=intent.getStringExtra("name");
        Log.d("name is", name);
        title.setText(name);
    }



}
