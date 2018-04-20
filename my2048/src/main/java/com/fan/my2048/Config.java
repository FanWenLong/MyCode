package com.fan.my2048;

import android.util.Log;

/**
 * Created by FanWenLong on 2018/4/18.
 * 配置信息
 */

public final class Config {
    /**
     * 输出log信息
     */
    public static boolean showLog = true;
    /**
     * 屏幕宽\高度
     */
    public static int screenWidth = 1080, screenHeight = 1920;
    /**
     * 格子横向数量,纵向数量
     */
    public static int rowNum = 4, columnNum = 4;
    /**
     * 头部左右比例
     */
    public static int headWeight = 3;
    /**
     * 中部的margin
     */
    public static int centerMarginLeft = 0, centerMarginTop = 20;

    /**
     * 临时的log信息
     *
     * @param msg
     */
    public static void L(String msg) {
        if (showLog && msg != null) {
            msg = msg.length() == 0 ? "空信息" : msg;
            Log.e("my2048", msg);
        }
    }
}
