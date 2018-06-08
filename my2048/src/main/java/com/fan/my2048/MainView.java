package com.fan.my2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.invoke.ConstantCallSite;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.PREPEND;

/**
 * Created by FanWenLong on 2018/4/18.
 * <p>
 * 主界面
 * <p>
 * step1: 通过onSizeChanged方法检测屏幕宽度,并初始化各个宽度。
 * step2: 构造方法初始化背景资源等。
 * step3: draw()
 */

public class MainView extends View {

    /**
     * 画笔
     */
    private Paint paint = new Paint();
    /**
     * 格子的宽,高
     */
    private int gridWidth = 0, gridHeight = 0;
    /**
     * 头部的尺寸
     */
    private int headWidth = 0, headHeight = 0;
    private int headColorLeft, headColorRight;
    /**
     * 中间布局
     */
    private int centerColor;
    /**
     * 中间结束布局
     */
    private int centerGameOverColor;
    /**
     * 中间坐标
     */
    private int centerLeft, centerTop, centerRight, centerBottom;
    /**
     * 文字大小
     */
    private int gridTextSize = 0;

    /**
     * 游戏是否结束
     */
    private boolean isOver = false;

    GameControl control;
    MoveControl moveControl;

    public MainView(Context context) {
        super(context);
        Config.L("MainView---construct");
        Resources resources = context.getResources();
        this.setBackgroundColor(resources.getColor(R.color.backGround));
        headColorLeft = resources.getColor(R.color.headLeft);
        headColorRight = resources.getColor(R.color.headRight);
        centerColor = resources.getColor(R.color.centerBackground);
        centerGameOverColor = resources.getColor(R.color.centerGameOverColor);
        control = GameControl.newInstance(this);
        moveControl = new MoveControl(new MoveControl.MoveListener() {
            @Override
            public void moveLeft() {
                control.leftMove();
            }

            @Override
            public void moveTop() {
                control.topMove();
            }

            @Override
            public void moveRight() {
                control.rightMove();
            }

            @Override
            public void moveBottom() {
                control.bottomMove();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Config.L("w=" + w + "  h=" + h + " oldw=" + oldw + "  oldh=" + oldh);
        Config.screenWidth = w;
        Config.screenHeight = h;
        Config.centerMarginLeft = Config.screenWidth / (Config.rowNum + 1) / 2;
        initLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Config.L("draw---------------");
        //画头部
        drawHead(canvas);
        //画中部
        drawCenter(canvas);
        //画游戏结果
        grawResult(canvas);
    }

    /**
     * 画头部
     */
    private void drawHead(Canvas canvas) {
        //画左边
        paint.setColor(headColorLeft);
        canvas.drawRect(0, 0, headWidth / Config.headWeight, headHeight, paint);
        //画右边
        paint.setColor(headColorRight);
        canvas.drawRect(headWidth / Config.headWeight, 0, headWidth, headHeight, paint);
    }

    /**
     * 画中部
     */
    private void drawCenter(Canvas canvas) {
        //画背景
        paint.setColor(centerColor);
        canvas.drawRect(centerLeft, centerTop, centerRight, centerBottom, paint);

        List<Grid> gridList = null;
        gridList = control.gridList;

        //画grid
        for (int i = 0; i < Config.rowNum; i++) {
            for (int j = 0; j < Config.columnNum; j++) {
                paint.setColor(Color.BLACK);
                Grid grid = gridList.get(i * Config.rowNum + j);
                int left = j * gridWidth + centerLeft + 5;
                int top = i * gridHeight + centerTop + 5;
                int right = 0, bottom = 0;
                if (i == Config.rowNum - 1) {
                    bottom = top + gridHeight - 10;
                } else {
                    bottom = top + gridHeight - 5;
                }
                if (j == Config.columnNum - 1) {
                    right = left + gridWidth - 10;
                } else {
                    right = left + gridWidth - 5;
                }
                canvas.drawRect(left, top, right, bottom, paint);
                paint.setColor(Color.WHITE);
//                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(gridTextSize);
                Config.L("hua:" + "x=" + grid.x + " y=" + grid.y + "  value=" + grid.value);
                canvas.drawText(grid.value, left + (gridWidth - gridTextSize * grid.value.length() / 2) / 2, top + gridHeight / 2 + gridTextSize / 2, paint);
            }
        }
    }

    //画游戏结果
    private void grawResult(Canvas canvas) {
        if (isOver) {
            //画背景
            paint.setColor(centerGameOverColor);
            canvas.drawRect(centerLeft, centerTop, centerRight, centerBottom, paint);

            paint.setColor(Color.RED);
            paint.setTextSize(70);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            float w = paint.measureText("游戏结束");
            canvas.drawText("游戏结束", centerLeft + (centerRight - centerLeft) / 2 - w / 2, centerTop + (centerBottom - centerTop) / 2, paint);
        }
    }

    //游戏结束
    public void gameIsOver() {
        isOver = true;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Config.L("onLayout---------------" + "changed=" + changed + "  left=" + left + "  top=" + top + "  right=" + right + "  bottom=" + bottom);
    }

    /**
     * 初始化大小
     */
    private void initLayout() {
        gridWidth = Math.min(Config.screenWidth / (Config.rowNum + 1), Config.screenHeight / (Config.rowNum + 1));
        if (Config.rowNum == Config.columnNum) {
            gridHeight = gridWidth;
        } else {
            gridHeight = Math.min(Config.screenWidth / (Config.columnNum + 2), Config.screenHeight / (Config.columnNum + 2));
        }
        //计算头部
        headWidth = Config.screenWidth;
        headHeight = Math.min(Config.screenWidth / 2, (Config.screenHeight - gridHeight * Config.columnNum) / 2);
        Config.L("gridWidth=" + gridWidth + "  gridHeight=" + gridHeight + "  headWidth=" + headWidth + "  headHeight=" + headHeight);
        //计算中间位置
        centerLeft = Config.centerMarginLeft;
        centerTop = headHeight + Config.centerMarginTop;
        centerRight = centerLeft + gridWidth * Config.rowNum;
        centerBottom = centerTop + gridHeight * Config.columnNum;
        //字体大小
        gridTextSize = Math.max(gridWidth / 5 * 2, (int) paint.measureText("9999"));
        Config.L("测量：" + paint.measureText("9999"));
        Config.L("gridWidth/5*3：" + gridWidth / 5 * 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOver) {
            return true;
        }
        moveControl.onTouch(event);
        return true;
    }
}
