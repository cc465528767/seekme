package com.zzzealous.seekme.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zzzealous on 2018/2/27.
 */

public class ToastUtil {
    public static void setShortToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    public static void setLongToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }

    public static void no(Context context){
        Toast.makeText(context,"这里没毛病",Toast.LENGTH_SHORT).show();
    }
    public static void yes(Context context){
        Toast.makeText(context,"这里有毛病",Toast.LENGTH_SHORT).show();
    }
}
