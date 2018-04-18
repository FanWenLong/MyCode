package com.fan.my2048;

import android.util.Log;

/**
 * Created by FanWenLong on 2018/4/18.
 * 配置信息
 */

public class Config {
    public static boolean showLog = true;

    /**
     * 临时的log信息
     * @param msg
     */
    public static void L(String msg) {
        if (showLog && msg != null) {
            msg = msg.length() == 0 ? "空信息" : msg;
            Log.e("my2048", msg);
        }
    }
}
