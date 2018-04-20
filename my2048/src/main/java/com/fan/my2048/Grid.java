package com.fan.my2048;

import android.view.View;

/**
 * Created by FanWenLong on 2018/4/19.
 * <p>
 * 记录格子的相关信息
 */
public class Grid {

    /**
     * 坐标信息
     */
    public int x, y;
    /**
     * 显示的值
     */
    public String value;

    public Grid() {
    }

    public Grid(int x, int y, String value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

}
