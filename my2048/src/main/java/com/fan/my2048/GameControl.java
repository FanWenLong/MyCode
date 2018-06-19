package com.fan.my2048;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FanWenLong on 2018/4/19.
 * <p>
 * 游戏控制器
 */

public class GameControl implements MoveControl.MoveListener {

    /**
     * 滑动后的布局数据
     */
    public List<Grid> gridList = null;
    /**
     * 当前分数
     */
    public int curScore = 0;
    /**
     * 界面布局
     */
    private MainView mainView;

    public static GameControl newInstance(MainView mainView) {
        return new GameControl(mainView);
    }

    private GameControl(MainView mainView) {
        this.mainView = mainView;
        gridList = new ArrayList<>();
        int px = (int) (Math.random() * Config.rowNum);
        int pj = (int) (Math.random() * Config.columnNum);
        for (int i = 0; i < Config.rowNum; i++) {
            for (int j = 0; j < Config.columnNum; j++) {
                if (i == px && j == pj) {
                    Config.L("i==" + i + "   j=" + j);
                    gridList.add(new Grid(i, j, "" + 2));
                } else {
                    gridList.add(new Grid(i, j, ""));
                }
            }
        }
    }

    /**
     * 产生新grid
     */
    public void newGrid() {
        Grid grid = null;
        List<Grid> canUse = new ArrayList<>();
        for (int i = 0; i < Config.rowNum; i++) {
            for (int j = 0; j < Config.columnNum; j++) {
                //重置
                gridList.get(i * Config.rowNum + j).isMergin = false;
                if (gridList.get(i * Config.rowNum + j).value.length() == 0) {
                    canUse.add(new Grid(i, j, ""));
                }
            }
        }
        if (canUse.size() == 0) {
            Config.L("game over");
            mainView.gameIsOver();
            return;
        }
        //产生新的值
        int tempValue = (int) (Math.random() * 10);
        tempValue = tempValue > 8 ? 4 : 2;
        //随机位置
        int tempPostion = (int) (Math.random() * canUse.size());
        grid = canUse.get(tempPostion);
        grid.value = tempValue + "";
        //应该
//        grid.showString("应该");
        gridList.get(grid.x * Config.rowNum + grid.y).setGrid(grid);
        //现在
//        grid.showString("现在");
        mainView.postInvalidate();
    }

    @Override
    public void moveLeft() {
        Config.L("left");
        boolean makeNew = false;
        for (int i = 0; i < Config.rowNum; i++) {
            for (int j = 0; j < Config.columnNum - 1; j++) {
                if (gridList.get(i * Config.rowNum + j).value.equals(gridList.get(i * Config.rowNum + j + 1).value)) {//前两个相等
                    if (gridList.get(i * Config.rowNum + j).value.equals("")) {

                    } else if (!gridList.get(i * Config.rowNum + j).isMergin && !gridList.get(i * Config.rowNum + j + 1).isMergin) {//两个不是合成的
                        int value = Integer.parseInt(gridList.get(i * Config.rowNum + j).value);
                        curScore += value;
                        gridList.get(i * Config.rowNum + j).value = value * 2 + "";
                        gridList.get(i * Config.rowNum + j).isMergin = true;
                        gridList.get(i * Config.rowNum + j + 1).value = "";
                        makeNew = true;
                    }
                } else if (gridList.get(i * Config.rowNum + j).value.equals("")) {//第一个空
                    gridList.get(i * Config.rowNum + j).value = gridList.get(i * Config.rowNum + j + 1).value;
                    gridList.get(i * Config.rowNum + j + 1).value = "";
                    gridList.get(i * Config.rowNum + j + 1).isMergin = false;
                    gridList.get(i * Config.rowNum + j).isMergin = false;
                    j = -1;
                    makeNew = true;
                }
            }
        }
        if (makeNew) {
            newGrid();
        }
    }

    @Override
    public void moveTop() {
        Config.L("top");
        boolean makeNew = false;
        for (int j = 0; j < Config.columnNum; j++) {
            for (int i = 0; i < Config.rowNum - 1; i++) {
                if (gridList.get(i * Config.rowNum + j).value.equals(gridList.get((i + 1) * Config.rowNum + j).value)) {//前两个相等
                    if (gridList.get(i * Config.rowNum + j).value.equals("")) {

                    } else if (!gridList.get(i * Config.rowNum + j).isMergin && !gridList.get((i + 1) * Config.rowNum + j).isMergin) {//两个不是合成的
                        int value = Integer.parseInt(gridList.get(i * Config.rowNum + j).value);
                        curScore += value;
                        gridList.get(i * Config.rowNum + j).value = value * 2 + "";
                        gridList.get(i * Config.rowNum + j).isMergin = true;
                        gridList.get((i + 1) * Config.rowNum + j).value = "";
                        makeNew = true;
                    }
                } else if (gridList.get(i * Config.rowNum + j).value.equals("")) {//第一个空
                    gridList.get(i * Config.rowNum + j).value = gridList.get((i + 1) * Config.rowNum + j).value;
                    gridList.get((i + 1) * Config.rowNum + j).value = "";
                    gridList.get((i + 1) * Config.rowNum + j).isMergin = false;
                    gridList.get(i * Config.rowNum + j).isMergin = false;
                    i = -1;
                    makeNew = true;
                }
            }
        }
        if (makeNew) {
            newGrid();
        }
    }

    @Override
    public void moveRight() {
        Config.L("right");
        boolean makeNew = false;
        for (int i = 0; i < Config.rowNum; i++) {
            for (int j = Config.columnNum - 1; j > 0; j--) {
                if (gridList.get(i * Config.rowNum + j).value.equals(gridList.get(i * Config.rowNum + j - 1).value)) {//前两个相等
                    if (gridList.get(i * Config.rowNum + j).value.equals("")) {

                    } else if (!gridList.get(i * Config.rowNum + j).isMergin && !gridList.get(i * Config.rowNum + j - 1).isMergin) {//两个不是合成的
                        int value = Integer.parseInt(gridList.get(i * Config.rowNum + j).value);
                        curScore += value;
                        gridList.get(i * Config.rowNum + j).value = value * 2 + "";
                        gridList.get(i * Config.rowNum + j).isMergin = true;
                        gridList.get(i * Config.rowNum + j - 1).value = "";
                        makeNew = true;
                    }
                } else if (gridList.get(i * Config.rowNum + j).value.equals("")) {//第一个空
                    gridList.get(i * Config.rowNum + j).value = gridList.get(i * Config.rowNum + j - 1).value;
                    gridList.get(i * Config.rowNum + j - 1).value = "";
                    gridList.get(i * Config.rowNum + j - 1).isMergin = false;
                    gridList.get(i * Config.rowNum + j).isMergin = false;
                    j = Config.columnNum;
                    makeNew = true;
                }
            }
        }
        if (makeNew) {
            newGrid();
        }
    }

    @Override
    public void moveBottom() {
        Config.L("bottom");
        boolean makeNew = false;
        for (int j = 0; j < Config.columnNum; j++) {
            for (int i = Config.rowNum - 1; i > 0; i--) {
                if (gridList.get(i * Config.rowNum + j).value.equals(gridList.get((i - 1) * Config.rowNum + j).value)) {//前两个相等
                    if (gridList.get(i * Config.rowNum + j).value.equals("")) {

                    } else if (!gridList.get(i * Config.rowNum + j).isMergin && !gridList.get((i - 1) * Config.rowNum + j).isMergin) {//两个不是合成的
                        int value = Integer.parseInt(gridList.get(i * Config.rowNum + j).value);
                        curScore += value;
                        gridList.get(i * Config.rowNum + j).value = value * 2 + "";
                        gridList.get(i * Config.rowNum + j).isMergin = true;
                        gridList.get((i - 1) * Config.rowNum + j).value = "";
                        makeNew = true;
                    }
                } else if (gridList.get(i * Config.rowNum + j).value.equals("")) {//第一个空
                    gridList.get(i * Config.rowNum + j).value = gridList.get((i - 1) * Config.rowNum + j).value;
                    gridList.get((i - 1) * Config.rowNum + j).value = "";
                    gridList.get((i - 1) * Config.rowNum + j).isMergin = false;
                    gridList.get(i * Config.rowNum + j).isMergin = false;
                    i = Config.rowNum;
                    makeNew = true;
                }
            }
        }
        if (makeNew) {
            newGrid();
        }
    }
}
