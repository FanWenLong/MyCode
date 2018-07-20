package com.fan.slideutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by FanWenLong on 2018/7/13.
 * <p>
 * 可以左滑、右滑的控件。
 */
public class SlidingLayout extends RelativeLayout {

    private final String TAG = "SlidingLayout";
    private final int LEFT_TO_RIGHT = 1;
    private final int RIGHT_TO_LEFT = 2;

    /**
     * 滑动监听
     */
    private SlidingLayoutListener slidingLayoutListener;
    /**
     * 滑动类，帮助滑动
     */
    private Scroller scroller;

    /**
     * 手指滑动速度检测
     */
    private VelocityTracker tracker;
    /**
     * 控件的宽度
     */
    private int parentWidth;

    /**
     * 当前的x坐标
     */
    private int curX;
    /**
     * 当前的y坐标
     */
    private int curY;
    /**
     * 上次的x坐标
     */
    private int lastX = 0;
    /**
     * 上次的y坐标
     */
    private int lastY = 0;
    /**
     * 最小滑动的距离
     */
    private int minCanScroll;
    /**
     * 手指是向右滑
     */
    private int curDirection = 0;
    /**
     * 是否取消
     */
    private boolean isCancle = false;


    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        minCanScroll = ViewConfiguration.get(context).getScaledTouchSlop();
        scroller = new Scroller(context);
        tracker = VelocityTracker.obtain();
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "最小滑动距离，minCanScroll=" + minCanScroll);
        }
    }

    /**
     * 设置滑动监听
     */
    public void setSlidingLayoutListener(SlidingLayoutListener slidingLayoutListener) {
        this.slidingLayoutListener = slidingLayoutListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            parentWidth = this.getWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tracker.addMovement(event);
        curX = (int) event.getRawX();
        curY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取点击的屏幕的位置
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = lastX - curX;
                int deltaY = lastY - curY;
                //x轴滑动距离大于0 并且 y轴滑动距离小于最下滑动距离
                if (Math.abs(deltaX) >= 0
                        && Math.abs(deltaY) < minCanScroll * 2) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "deltaX=" + deltaX + "  deltaY=" + deltaY);
                    }
                    if (deltaX >= 0) {
                        curDirection = RIGHT_TO_LEFT;
                    } else {
                        curDirection = LEFT_TO_RIGHT;
                    }
                    //开始滑动
                    scrollBy(deltaX, 0);
                    //触发监听
                    if (slidingLayoutListener != null) {
                        if (curDirection == LEFT_TO_RIGHT) {
                            slidingLayoutListener.onLeftSliding();
                        } else if (curDirection == RIGHT_TO_LEFT) {
                            slidingLayoutListener.onRightSliding();
                        }
                    }
                } else { //纵向滑动

                }
                break;
            case MotionEvent.ACTION_UP:
                tracker.computeCurrentVelocity(1000);
                float xVelocity = tracker.getXVelocity();
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "横向滑动速度：" + xVelocity);
                }
                if (Math.abs(xVelocity) > 1000) {
                    //继续滑动
                    scrollEnd();
                    tracker.clear();
                    break;
                } else {
                    tracker.clear();
                }
                //滑动距离不超过一半的时候，认为取消
                if (Math.abs(getScrollX()) <= (parentWidth / 2)) {
                    //滑动回原位
                    scrollOrigin();
                } else {
                    //继续滑动
                    scrollEnd();
                }
                break;
        }
        lastX = curX;
        lastY = curY;
        return true;
    }

    /**
     * 滑动回原位
     */
    private void scrollOrigin() {
        isCancle = true;
        int delta = getScrollX();
        scroller.startScroll(delta, 0, -delta, 0, Math.abs(delta));
        invalidate();
    }

    /**
     * 完成滑动
     */
    private void scrollEnd() {
        isCancle = false;
        int delta = getScrollX();
        if (curDirection == LEFT_TO_RIGHT) {
            scroller.startScroll(delta, 0, delta - parentWidth, 0, Math.abs(delta - parentWidth));
        } else if (curDirection == RIGHT_TO_LEFT) {
            scroller.startScroll(delta, 0, parentWidth - delta, 0, Math.abs(delta));
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //没有结束滑动
        if (scroller.computeScrollOffset()) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "computeScroll，scroller.getCurrX()=" + scroller.getCurrX());
            }
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
            if (scroller.isFinished()) {
                if (slidingLayoutListener != null) {
                    if (isCancle) {
                        if (curDirection == LEFT_TO_RIGHT) {
                            slidingLayoutListener.onLeftSlidingCancle();
                        } else if (curDirection == RIGHT_TO_LEFT) {
                            slidingLayoutListener.onRightSlidingCancle();
                        }
                    } else {
                        if (curDirection == LEFT_TO_RIGHT) {
                            slidingLayoutListener.onLeftSlidingFinish();
                        } else if (curDirection == RIGHT_TO_LEFT) {
                            slidingLayoutListener.onRightSlidingFinish();
                        }
                    }
                }
            }
        }
    }
}
