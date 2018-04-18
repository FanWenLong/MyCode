package com.fan.my2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 * Created by FanWenLong on 2018/4/18.
 * <p>
 * 主界面
 * <p>
 * step1: onSizeChanged
 */

public class MainView extends View {

    public MainView(Context context) {
        super(context);
        Config.L("MainView---construct");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Config.L("w=" + w + "  h=" + h + " oldw=" + oldw + "  oldh=" + oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Config.L("draw");
        canvas.drawRect(new RectF(0, 0, 100, 100), new Paint());
    }
}
