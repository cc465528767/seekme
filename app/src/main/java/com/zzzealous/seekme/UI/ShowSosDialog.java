package com.zzzealous.seekme.UI;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zzzealous.seekme.Bean.GlobalValues;
import com.zzzealous.seekme.R;

/**
 * Created by zzzealous on 2018/4/1.
 */

public class ShowSosDialog extends Dialog implements View.OnClickListener {


    private Context context;
    private String starttime;
    private String date;
    private String helper_name;
    private String seeker_name;
    private String url;

    private boolean showNegativeButton = true;
    private int requestCode;

    public ShowSosDialog(@NonNull Context context, String date, String starttime
             , String helper_name , String seeker_name
             , String url)
    {
        super(context, R.style.CenterDialogStyle);

        this.context = context;
        this.date = date;
        this.starttime = starttime;
        this.helper_name = helper_name;
        this.seeker_name = seeker_name;
        this.url = url;
    }


    private SimpleDraweeView sos_image;
    private TextView sos_starttime;
    private TextView sos_date;
    private TextView helper;
    private TextView seeker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载dialog的布局
        setContentView(R.layout.sos_dialog);


        //拿到布局控件进行处理
        sos_image = (SimpleDraweeView) findViewById(R.id.sos_dialog_sdv);
        sos_starttime = findViewById(R.id.sos_dialog_start);
        sos_date = findViewById(R.id.sos_dialog_date);
        helper = findViewById(R.id.sos_dialog_helper);
        seeker = findViewById(R.id.sos_dialog_seeker);

        sos_image.setImageURI(Uri.parse(GlobalValues.baseUrl + "images/" + url));
        sos_date.setText(date);
        sos_starttime.setText(starttime);
        helper.setText(helper_name);
        seeker.setText(seeker_name);

        //初始化布局的位置
        initLayoutParams();
    }

    private void initLayoutParams() {
        // 布局的参数
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        params.alpha = 1f;
        getWindow().setAttributes(params);
    }
    @Override
    public void onClick(View v) {
        dismiss();
    }

}
