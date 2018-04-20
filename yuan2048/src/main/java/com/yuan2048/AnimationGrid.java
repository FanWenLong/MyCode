package com.yuan2048;

import java.util.ArrayList;

/**
 * Created by FanWenLong on 2018/4/20.
 * <p>
 * 控制所有格子的动画的执行与取消
 */

public class AnimationGrid {
    /**
     * 保存所有格子的动画信息
     */
    public ArrayList<AnimationCell>[][] field;
    int activeAnimations = 0;
    boolean oneMoreFrame = false;
    public ArrayList<AnimationCell> globalAnimation = new ArrayList<AnimationCell>();

    public AnimationGrid(int x, int y) {
        field = new ArrayList[x][y];
        for (int xx = 0; xx < x; xx++) {
            for (int yy = 0; yy < y; yy++) {
                field[xx][yy] = new ArrayList<AnimationCell>();
            }
        }
    }

    /**
     * 记录需要执行动画的格子，并记录数量
     */
    public void startAnimation(int x, int y, int animationType, long length, long delay, int[] extras) {
        AnimationCell animationToAdd = new AnimationCell(x, y, animationType, length, delay, extras);
        if (x == -1 && y == -1) {
            globalAnimation.add(animationToAdd);
        } else {
            field[x][y].add(animationToAdd);
        }
        activeAnimations = activeAnimations + 1;
    }

    /**
     * 开始执行动画(重复调用)
     */
    public void tickAll(long timeElapsed) {
        ArrayList<AnimationCell> cancelledAnimations = new ArrayList<AnimationCell>();
        for (AnimationCell animation : globalAnimation) {
            animation.tick(timeElapsed);
            if (animation.animationDone()) {
                cancelledAnimations.add(animation);
                activeAnimations = activeAnimations - 1;
            }
        }

        for (ArrayList<AnimationCell>[] array : field) {
            for (ArrayList<AnimationCell> list : array) {
                for (AnimationCell animation : list) {
                    animation.tick(timeElapsed);
                    if (animation.animationDone()) {
                        cancelledAnimations.add(animation);
                        activeAnimations = activeAnimations - 1;
                    }
                }
            }
        }

        for (AnimationCell animation : cancelledAnimations) {
            cancelAnimation(animation);
        }
    }

    /**
     * 从需要执行动画的记录中移除，相当于结束动画
     */
    public void cancelAnimation(AnimationCell animation) {
        if (animation.getX() == -1 && animation.getY() == -1) {
            globalAnimation.remove(animation);
        } else {
            field[animation.getX()][animation.getY()].remove(animation);
        }
    }

    /**
     * 所有动画是否执行完毕
     */
    public boolean isAnimationActive() {
        if (activeAnimations != 0) {
            oneMoreFrame = true;
            return true;
        } else if (oneMoreFrame) {
            oneMoreFrame = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据位置获取需要执行动画的格子
     */
    public ArrayList<AnimationCell> getAnimationCell(int x, int y) {
        return field[x][y];
    }

    /**
     * 清楚需要执行动画的记录
     */
    public void cancelAnimations() {
        for (ArrayList<AnimationCell>[] array : field) {
            for (ArrayList<AnimationCell> list : array) {
                list.clear();
            }
        }
        globalAnimation.clear();
        activeAnimations = 0;
    }
}
