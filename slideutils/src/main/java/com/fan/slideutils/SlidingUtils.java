package com.fan.slideutils;

import android.app.Activity;
import android.content.Context;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by FanWenLong on 2018/8/7.
 * 工具类
 */
public class SlidingUtils {

    private Context context;
    private int layoutId;
    private boolean hasLeftAnim = true;
    private boolean hasRightAnim = true;
    private SlidingLayoutListener mListener;
    private SlidingLayout slidingLayout;
    private View view;
    private ViewParent parent;

    public SlidingUtils(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        slidingLayout = new SlidingLayout(context);
        slidingLayout.setLeftAnimate(hasLeftAnim);
        slidingLayout.setRightAnimate(hasRightAnim);
        if (mListener != null) {
            slidingLayout.setSlidingLayoutListener(mListener);
        }
    }

    public SlidingUtils setLeftAnimate(boolean has) {
        this.hasLeftAnim = has;
        slidingLayout.setLeftAnimate(hasLeftAnim);
        return this;
    }

    public SlidingUtils setRightAnimate(boolean has) {
        this.hasRightAnim = has;
        slidingLayout.setRightAnimate(hasRightAnim);
        return this;
    }

    public SlidingUtils setSlidingLayoutListener(SlidingLayoutListener mListener) {
        this.mListener = mListener;
        if (mListener != null) {
            slidingLayout.setSlidingLayoutListener(mListener);
        }
        return this;
    }

    public void show() {
        view = ((Activity) context).getWindow().getDecorView().findViewById(layoutId);
        if (view == null) {
            throw new AndroidRuntimeException("布局没找到");
        }
        parent = view.getParent();
        if (parent instanceof FrameLayout) {
            FrameLayout frameLayout = (FrameLayout) parent;
            frameLayout.removeAllViews();
            SlidingLayout.LayoutParams params = new SlidingLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            slidingLayout.addView(view);
            frameLayout.addView(slidingLayout);
        } else {
            throw new AndroidRuntimeException("必须是跟布局id");
        }
    }

}
