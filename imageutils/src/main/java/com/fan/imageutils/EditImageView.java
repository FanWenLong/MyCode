package com.fan.imageutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by FanWenLong on 2018/8/9.
 * 画板
 */
public class EditImageView extends AppCompatImageView {

    public EditImageView(Context context) {
        super(context);
        initView();
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public static final int INIT_COLOR = Color.BLACK;
    public static final float INIT_WIDTH = 10f;
    /**
     * 是否使用二级缓存
     */
    boolean useCache = true;
    /**
     * 画笔
     */
    Paint paint;
    /**
     * 二级缓存界面
     */
    Bitmap cacheBitmap;
    Canvas cacheCanvas;
    /**
     * 历史路径，用于撤销
     */
    LinkedList<DrawPath> pathList;
    /**
     * 被撤销的
     */
    LinkedList<DrawPath> cancelList;
    /**
     * 当前路径
     */
    DrawPath curPath;

    /**
     * 初始化
     */
    private void initView() {
        pathList = new LinkedList<>();
        cancelList = new LinkedList<>();
        paint = new Paint();
        curPath = new DrawPath();
        curPath.setColor(Color.BLACK);
        curPath.setWidth(5f);
        curPath.setPath(new Path());
    }

    /**
     * 设置当前画笔颜色
     */
    public void setPaintColor(int color) {
        curPath.setColor(color);
    }

    /**
     * 设置当前画笔宽度
     */
    public void setPaintWidth(float width) {
        curPath.setWidth(width);
    }

    /**
     * 撤销
     */
    public void undo() {
        if (pathList.size() == 0) {
            return;
        }
        cancelList.add(pathList.removeLast());
        if (useCache) {
            cacheCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            for (DrawPath path : pathList) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(path.getColor());
                paint.setStrokeWidth(path.getWidth());
                cacheCanvas.drawPath(path.getPath(), paint);
            }
        }
        invalidate();

    }

    /**
     * 还原
     */
    public void redo() {
        if (cancelList.size() == 0) {
            return;
        }
        pathList.add(cancelList.removeLast());
        if (useCache) {
            cacheCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            for (DrawPath path : pathList) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(path.getColor());
                paint.setStrokeWidth(path.getWidth());
                cacheCanvas.drawPath(path.getPath(), paint);
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (useCache) {
            if (cacheBitmap != null) {
                canvas.drawBitmap(cacheBitmap, 0, 0, paint);
            }
        } else {
            for (DrawPath path : pathList) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(path.getColor());
                paint.setStrokeWidth(path.getWidth());
                canvas.drawPath(path.getPath(), paint);
            }
            if (curPath != null) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(curPath.getColor());
                paint.setStrokeWidth(curPath.getWidth());
                canvas.drawPath(curPath.getPath(), paint);
            }
        }

    }

    private void drawCache() {
        if (cacheCanvas == null) {
            initCache();
        }
        if (curPath != null) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(curPath.getColor());
            paint.setStrokeWidth(curPath.getWidth());
            cacheCanvas.drawPath(curPath.getPath(), paint);
        }
        invalidate();
    }

    /**
     * 初始化二级缓存
     */
    private void initCache() {
        cacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cacheBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curPath.getPath().moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                curPath.getPath().lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                pathList.add(curPath);
                DrawPath temp = curPath;
                curPath = null;
                curPath = new DrawPath(temp);
                break;
        }
        if (useCache) {
            drawCache();
        } else {
            invalidate();
        }
        return true;
    }
}
