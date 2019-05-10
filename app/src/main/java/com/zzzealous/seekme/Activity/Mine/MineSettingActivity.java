package com.zzzealous.seekme.Activity.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.zzzealous.seekme.R;
import com.zzzealous.seekme.Utils.ToastUtil;

public class MineSettingActivity extends AppCompatActivity {

    private RadioButton button4location;
    private RadioButton button4voice;
    private RadioButton button4shake;
    private RadioButton button4showmessage;
    boolean voice;
    boolean location;
    boolean shake;
    boolean showmessage;

    private LinearLayout change4password;
    private LinearLayout cachecleanning;
    private LinearLayout newvoice;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting);

        //定位单选按钮
        button4location = (RadioButton) findViewById(R.id.Button_Location);
        location = button4location.isChecked();
        button4location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location) {
                    location = false;
                    button4location.setChecked(false);
                } else {
                    location = true;
                    button4location.setChecked(true);
                }

            }
        });
//        //声音单选按钮
        button4voice = (RadioButton) findViewById(R.id.Button_Voice);
        voice = button4voice.isChecked();
        button4voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voice) {
                    voice = false;
                    button4voice.setChecked(false);
                } else {
                    voice = true;
                    button4voice.setChecked(true);
                }
            }
        });
//
//        //新消息单选按钮
        button4showmessage = (RadioButton) findViewById(R.id.Button_ShowMessage);
        showmessage = button4showmessage.isChecked();
        button4showmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showmessage) {
                    showmessage = false;
                    button4showmessage.setChecked(false);
                } else {
                    showmessage = true;
                    button4showmessage.setChecked(true);
                }
            }
        });

        //震动单选按钮
        button4shake = (RadioButton) findViewById(R.id.Button_Shake);
        shake = button4shake.isChecked();
        button4shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shake) {
                    shake = false;
                    button4shake.setChecked(false);
                } else {
                    shake = true;
                    button4shake.setChecked(true);
                }
            }
        });
        //修改密码
        change4password = (LinearLayout) findViewById(R.id.ChangePassword);
        change4password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineSettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        //清理缓存
        cachecleanning=(LinearLayout)findViewById(R.id.Cachecleanning);
        cachecleanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.setLongToast(MineSettingActivity.this,"清理成功");
            }
        });


        //新消息提示音选择
        newvoice=(LinearLayout) findViewById(R.id.NewVoice);
        newvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MineSettingActivity.this,ChooseNewVioceAcitivity.class);
                startActivity(intent);
            }
        });

    }

}
