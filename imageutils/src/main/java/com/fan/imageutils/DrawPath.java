package com.fan.imageutils;

import android.graphics.Path;

/**
 * Created by FanWenLong on 2018/8/14.
 * path类
 */
public class DrawPath {
    private Path path;
    private int color;
    private float width;
    private boolean show = true;

    public DrawPath() {

    }

    public DrawPath(DrawPath path) {
        setPath(new Path());
        setWidth(path.getWidth());
        setColor(path.getColor());
        setShow(path.isShow());
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "DrawPath{" +
                "path=" + path +
                ", color=" + color +
                ", width=" + width +
                ", show=" + show +
                '}';
    }
}
