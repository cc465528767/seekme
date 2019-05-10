package com.zzzealous.seekme.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzzealous.seekme.R;


public class ArrowItemView extends RelativeLayout {ImageView ivArrowLabel;
    TextView tvArrowContent;
    ImageView ivArrowRight;
    RelativeLayout rlArrowItem;
    TextView tvArrowRight;


    public ArrowItemView(Context context) {
        this(context, null);
    }

    public ArrowItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ArrowItemView);
        tvArrowContent.setText(typedArray.getString(R.styleable.ArrowItemView_arrowContent));

        ivArrowLabel.setImageResource(typedArray.getResourceId(R.styleable.ArrowItemView_arrowLabel, R.drawable.ic_info));

        boolean labelVisible = typedArray.getBoolean(R.styleable.ArrowItemView_arrowLabelVisible, true);
        ivArrowLabel.setVisibility(labelVisible ? VISIBLE : GONE);

        boolean ivRightVisible = typedArray.getBoolean(R.styleable.ArrowItemView_arrowIvRightVisible, true);
        ivArrowRight.setVisibility(ivRightVisible ? VISIBLE : GONE);

        boolean tvRightVisible = typedArray.getBoolean(R.styleable.ArrowItemView_arrowTvRightVisible,false);
        tvArrowRight.setVisibility(tvRightVisible?VISIBLE:GONE);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_arrow_item, this);

        ivArrowLabel = view.findViewById(R.id.iv_arrow_label);
        tvArrowContent = view.findViewById(R.id.tv_arrow_content);
        ivArrowRight = view.findViewById(R.id.iv_arrow_right);//右侧图片
        rlArrowItem = view.findViewById(R.id.rl_arrow_item);
        tvArrowRight = view.findViewById(R.id.tv_arrow_right);//右侧文字


    }

    public void setArrowContent(String content) {
        tvArrowContent.setText(content);
    }

    public void setArrowLabelVisible(boolean visible) {
        ivArrowLabel.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setArrowRightVisible(boolean visible) {
        ivArrowRight.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setTvArrowRightContent(String rightContent) {
        this.tvArrowRight.setText(rightContent);
    }

    public void setIvArrowRight(Bitmap bitmap) {
        this.ivArrowRight.setBackground(new BitmapDrawable(getResources(),bitmap));
    }

    /**
     * arrow item 事件监听
     */
    private OnArrowListener mOnArrowListener;
    public void setOnArrowListener(OnArrowListener listener) {
        mOnArrowListener = listener;
    }

    public interface OnArrowListener {
        void onSwitchChange(boolean checkable);
    }
}
