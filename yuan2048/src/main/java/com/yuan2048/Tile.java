package com.yuan2048;

/**
 * Created by FanWenLong on 2018/4/20.
 * <p>
 * 保存格子的值与来源
 */

public class Tile extends Cell {
    private int value;
    private Tile[] mergedFrom = null;

    public Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    public Tile(Cell cell, int value) {
        super(cell.getX(), cell.getY());
        this.value = value;
    }

    /**
     * 更新位置
     */
    public void updatePosition(Cell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    public int getValue() {
        return this.value;
    }

    public Tile[] getMergedFrom() {
        return mergedFrom;
    }

    public void setMergedFrom(Tile[] tile) {
        mergedFrom = tile;
    }
}
