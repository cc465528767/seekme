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
 * Created by zzzealous on 2018/3/7.
 */

public class TraceDialog extends Dialog implements View.OnClickListener {


    private Context context;
    private String starttime;
    private String date;
    private String realtime;
    private String url;

    private boolean showNegativeButton = true;
    private int requestCode;

    public TraceDialog(@NonNull Context context,String date,String starttime
             ,String realtime , String url)
    {
        super(context,R.style.CenterDialogStyle);

        this.context = context;
        this.date = date;
        this.starttime = starttime;
        this.realtime = realtime;
        this.url = url;
    }


    private SimpleDraweeView trace_image;
    private TextView trace_starttime;
    private TextView trace_realtime;
    private TextView trace_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载dialog的布局
        setContentView(R.layout.trace_dialog);


        //拿到布局控件进行处理
        trace_image = (SimpleDraweeView) findViewById(R.id.trace_dialog_sdv);
        trace_starttime = findViewById(R.id.trace_dialog_start);
        trace_realtime = findViewById(R.id.trace_dialog_real);
        trace_date = findViewById(R.id.trace_dialog_date);

        trace_image.setImageURI(Uri.parse(GlobalValues.baseUrl + "images/" + url));
        trace_date.setText(date);
        trace_starttime.setText(starttime);
        trace_realtime.setText(realtime);

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
