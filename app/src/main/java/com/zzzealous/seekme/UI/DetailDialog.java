package com.zzzealous.seekme.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zzzealous.seekme.R;

/**
 * Created by zzzealous on 2018/3/17.
 */

public class DetailDialog extends Dialog implements View.OnClickListener {

    public interface OnDialogButtonClickListener {

        /**点击按钮事件的回调方法
         * @param requestCode 传入的用于区分某种情况下的showDialog
         * @param isPositive
         */
        void onDialogButtonClick(int requestCode, boolean isPositive);
    }

    private String userName;
    private String phone;
    private String jobnumber;

    public DetailDialog(@NonNull Context context,String userName,String phone
             ,String jobnumber,   int requestCode, DetailDialog.OnDialogButtonClickListener listener) {
        super(context, R.style.CenterDialogStyle);

        this.userName = userName;
        this.phone = phone;
        this.jobnumber = jobnumber;
        this.requestCode = requestCode;
        this.listener = listener;
    }

    private TextView detail_name;
    private TextView detail_phone;
    private TextView detail_jobnum;
    private Button btnPositive;
    private Button btnNegative;
    private int requestCode;
    private DetailDialog.OnDialogButtonClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载dialog的布局
        setContentView(R.layout.detail_dialog);
        setCanceledOnTouchOutside(true);

        initView();

        //初始化布局的位置
        initLayoutParams();
    }

    private void initView() {
        detail_name = findViewById(R.id.detailDialogName);
        detail_phone = findViewById(R.id.detailDialogPhone);
        detail_jobnum = findViewById(R.id.detailDialogJobNum);

        detail_name.setText(userName);
        detail_phone.setText(phone);
        detail_jobnum.setText(jobnumber);

        btnPositive = findViewById(R.id.btnDetailDialogPositive);
        btnNegative = findViewById(R.id.btnDetailDialogNegative);

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
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
        if (v.getId() == R.id.btnDetailDialogPositive) {
            listener.onDialogButtonClick(requestCode, true);
        } else if (v.getId() == R.id.btnDetailDialogNegative) {
            listener.onDialogButtonClick(requestCode, false);
        }

        dismiss();
    }
}
