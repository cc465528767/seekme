package com.zzzealous.seekme.Activity.News.Notify;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.Database.DatabaseManager;
import com.zzzealous.seekme.GTPush.GTDataManager;
import com.zzzealous.seekme.R;
import com.zzzealous.seekme.Utils.ToastUtil;

import java.util.Calendar;

public class CreateNoticeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText notiTitle;
    EditText notiContent;
    LinearLayout sendNotifyBtn;
    Button backBtn;
    Calendar calendar = Calendar.getInstance();
    TextView setNoticeDate;
    TextView setNoticeTime;
    String titleText;
    String contentText;

    public static final int SAVE_NOTIFICATION_RECORD = 0x00;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAVE_NOTIFICATION_RECORD :
                    Log.i("notification", "保存记录 到数据库");
                    ToastUtil.setLongToast(CreateNoticeActivity.this,"通知推送成功！");
                    CreateNoticeActivity.this.finish();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice);
        initView();
        Log.d("11", "onCreate"+notiTitle.getText().toString());

    }

    private void initView() {
        notiTitle = findViewById(R.id.edit_notice_title);
        notiContent = findViewById(R.id.edit_notify_content);
        sendNotifyBtn  = findViewById(R.id.send_notice_btn);
        backBtn = findViewById(R.id.create_notice_backbtn);

        setNoticeDate = findViewById(R.id.edit_noti_day);
        setNoticeTime = findViewById(R.id.edit_noti_time);

        setNoticeDate.setOnClickListener(this);
        setNoticeTime.setOnClickListener(this);

        sendNotifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    if(GlobalValues.ClientArray.length()!=0) {
                        try {
                            GTDataManager.getInstance().sendNotifyMsg(GlobalValues.ClientArray,
                                    notiTitle.getText().toString(),
                                    notiContent.getText().toString());
                            DatabaseManager.getInstance().saveNotification(
                                    notiTitle.getText().toString(),
                                    notiContent.getText().toString(),
                                    setNoticeDate.getText().toString(),
                                    setNoticeTime.getText().toString());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(SAVE_NOTIFICATION_RECORD);
                                    Log.d("11", "onCreate"+notiTitle.getText().toString());

                                }

                            },100);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNoticeActivity.this.finish();
            }
        });
    }

    /***
     * 检查输入
     **/
    private boolean checkInput(){
        titleText = notiTitle.getText().toString();
        contentText = notiTitle.getText().toString();

        if (!titleText.isEmpty() && !contentText.isEmpty()) {
            return true;
        } else if (titleText.isEmpty()) {
            ToastUtil.setLongToast(CreateNoticeActivity.this,"标题不能为空");
        } else if (contentText.isEmpty()) {
            ToastUtil.setLongToast(CreateNoticeActivity.this,"内容不能为空");
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_noti_day:

                showDatePickerDialig(CreateNoticeActivity.this,setNoticeDate,calendar);

                break;
            case  R.id.edit_noti_time:

                showTimePickerDialog(CreateNoticeActivity.this,setNoticeTime,calendar);

                break;

        }

    }

    public static void showDatePickerDialig(Activity activity, final TextView tv, Calendar calendar){
        new DatePickerDialog(activity,3,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        monthOfYear = monthOfYear+1;
                        tv.setText(year + "-" + monthOfYear+"-" + dayOfMonth);
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    public static void showTimePickerDialog(Activity activity, final TextView tv, Calendar calendar) {

        new TimePickerDialog( activity,3,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        tv.setText(hourOfDay + " : " + minute );
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }


}
