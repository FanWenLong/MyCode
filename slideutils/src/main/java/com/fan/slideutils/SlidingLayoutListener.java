package com.fan.slideutils;

/**
 * Created by FanWenLong on 2018/7/13.
 * <p>
 * SlidingLayout的监听{@Link com.fan.slideutils.SlidingLayout}
 */
public abstract class SlidingLayoutListener {
    /**
     * 手指向右滑动：滑动中
     */
    public void onLeftSliding() {
    }

    /**
     * 手指向右滑动：滑动结束
     */
    public abstract void onLeftSlidingFinish();

    /**
     * 手指向右滑动：取消
     */
    public void onLeftSlidingCancle() {
    }

    /**
     * 手指向左滑动：滑动中
     */
    public void onRightSliding() {
    }

    /**
     * 手指向左滑动：滑动结束
     */
    public abstract void onRightSlidingFinish();

    /**
     * 手指向左滑动：滑动取消
     */
    public void onRightSlidingCancle() {
    }
}
