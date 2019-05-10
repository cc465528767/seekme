package com.zzzealous.seekme.Utils;

import java.util.UUID;

/**
 * Created by zzzealous on 2018/6/13.
 */

public class TextUtil {

    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

}
