package com.fan.my2048;

import android.view.MotionEvent;

/**
 * Created by FanWenLong on 2018/6/6.
 */
public class MoveControl {

    public interface MoveListener {
        void moveLeft();

        void moveTop();

        void moveRight();

        void moveBottom();
    }

    MoveListener listener = null;
    float firstx = 0;
    float firsty = 0;
    //差值
    float chaX = 0;
    float chaY = 0;
    //是否已处理
    boolean isMove = false;


    public MoveControl(MoveListener listener) {
        this.listener = listener;
    }

    public void onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstx = event.getX();
                firsty = event.getY();
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove) {
                    break;
                }
                chaX = event.getX() - firstx;
                chaY = event.getY() - firsty;
                Config.L("chaX==" + chaX + "    chaY==" + chaY);
                if (Math.abs(chaX) > Math.abs(chaY)) {//横向滑动
                    if (chaX > 150) {
                        isMove = true;
                        listener.moveRight();
                    } else if (chaX < -150) {
                        isMove = true;
                        listener.moveLeft();
                    }
                } else {//纵向滑动
                    if (chaY > 150) {
                        isMove = true;
                        listener.moveBottom();
                    } else if (chaY < -150) {
                        isMove = true;
                        listener.moveTop();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }

}
