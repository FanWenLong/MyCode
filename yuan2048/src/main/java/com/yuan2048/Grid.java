package com.yuan2048;

import java.util.ArrayList;

/**
 * Created by FanWenLong on 2018/4/20.
 * <p>
 * 保存界面格子的信息
 */

public class Grid {
    /**
     * 当前的界面的值的分布
     */
    public Tile[][] field;
    /**
     * 点击撤销后的界面的值的分布
     */
    public Tile[][] undoField;
    /**
     * 当前界面的缓存
     */
    private Tile[][] bufferField;

    public Grid(int sizeX, int sizeY) {
        field = new Tile[sizeX][sizeY];
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
        clearGrid();
        clearUndoGrid();
    }

    /**
     * 清空当前值
     */
    public void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }

    /**
     * 清空保存的值
     */
    public void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }

    /**
     * 新生成的数字的位置
     */
    public Cell randomAvailableCell() {
        ArrayList<Cell> availableCells = getAvailableCells();
        if (availableCells.size() >= 1) {
            return availableCells.get((int) Math.floor(Math.random() * availableCells.size()));
        }
        return null;
    }

    /**
     * 获取空着的格子
     */
    public ArrayList<Cell> getAvailableCells() {
        ArrayList<Cell> availableCells = new ArrayList<Cell>();
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    availableCells.add(new Cell(xx, yy));
                }
            }
        }
        return availableCells;
    }


    /**
     * 是否有空白的格子
     */
    public boolean isCellsAvailable() {
        return (getAvailableCells().size() >= 1);
    }

    /**
     * 这个格子是否可用
     */
    public boolean isCellAvailable(Cell cell) {
        return !isCellOccupied(cell);
    }

    /**
     * 这个格子是否被占用
     */
    public boolean isCellOccupied(Cell cell) {
        return (getCellContent(cell) != null);
    }

    /**
     * 返回这个格子中的Tile
     */
    public Tile getCellContent(Cell cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }

    /**
     * 格子是否合法
     */
    public boolean isCellWithinBounds(Cell cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
                && 0 <= cell.getY() && cell.getY() < field[0].length;
    }

    /**
     * 根据坐标返回格子中的Tile
     */
    public Tile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }

    /**
     * 格子是否合法
     */
    public boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }

    /**
     * 格子中插入值
     */
    public void insertTile(Tile tile) {
        field[tile.getX()][tile.getY()] = tile;
    }

    /**
     * 移除格子中的值（Tile）
     */
    public void removeTile(Tile tile) {
        field[tile.getX()][tile.getY()] = null;
    }

    /**
     * 保存可撤销界面
     */
    public void saveTiles() {
        for (int xx = 0; xx < bufferField.length; xx++) {
            for (int yy = 0; yy < bufferField[0].length; yy++) {
                if (bufferField[xx][yy] == null) {
                    undoField[xx][yy] = null;
                } else {
                    undoField[xx][yy] = new Tile(xx, yy, bufferField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * 当前界面写入缓存
     */
    public void prepareSaveTiles() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null;
                } else {
                    bufferField[xx][yy] = new Tile(xx, yy, field[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * 点击撤销后，恢复上一步
     */
    public void revertTiles() {
        for (int xx = 0; xx < undoField.length; xx++) {
            for (int yy = 0; yy < undoField[0].length; yy++) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null;
                } else {
                    field[xx][yy] = new Tile(xx, yy, undoField[xx][yy].getValue());
                }
            }
        }
    }
}
