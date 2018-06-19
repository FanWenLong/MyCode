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

    /**
     * 是否合成过
     */
    public boolean isMergin = false;

    public Grid() {
    }

    public Grid(int x, int y, String value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public void setGrid(Grid grid) {
        this.x = grid.x;
        this.y = grid.y;
        this.value = grid.value;
    }

    public void showString(String tag) {
        Config.L(tag + ":   x=" + this.x + "   y=" + this.y + "   value=" + this.value);
    }

}
