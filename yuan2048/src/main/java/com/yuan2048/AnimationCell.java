package com.yuan2048;

/**
 * Created by FanWenLong on 2018/4/20.
 * <p>
 * 格子的动画信息
 */

public class AnimationCell extends Cell {

    /**
     * 动画类型
     */
    private int animationType;
    /**
     * 已执行的时间
     */
    private long timeElapsed;
    /**
     * 总时间
     */
    private long animationTime;
    /**
     * 延迟的时间
     */
    private long delayTime;

    public int[] extras;

    public AnimationCell(int x, int y, int animationType, long length, long delay, int[] extras) {
        super(x, y);
        this.animationType = animationType;
        animationTime = length;
        delayTime = delay;
        this.extras = extras;
    }

    /**
     * 返回动画类型
     */
    public int getAnimationType() {
        return animationType;
    }

    /**
     * 计时 (重复调用)
     */
    public void tick(long timeElapsed) {
        this.timeElapsed = this.timeElapsed + timeElapsed;
    }

    /**
     * 判断动画是否执行完毕
     */
    public boolean animationDone() {
        return animationTime + delayTime < timeElapsed;
    }

    /**
     * 获取执行的百分比
     */
    public double getPercentageDone() {
        return Math.max(0, 1.0 * (timeElapsed - delayTime) / animationTime);
    }

    /**
     * 判断动画是否开始
     */
    public boolean isActive() {
        return (timeElapsed >= delayTime);
    }
}
