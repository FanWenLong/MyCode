package com.fan.my2048;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FanWenLong on 2018/4/19.
 * <p>
 * 游戏控制器
 */

public class GameControl {

    public List<Grid> gridList = new ArrayList<Grid>();
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
        grid.showString("应该");
        gridList.get(grid.x * Config.rowNum + grid.y).setGrid(grid);
        //现在
        grid.showString("现在");
        mainView.postInvalidate();
    }

    public void leftMove() {
        Config.L("left");

        newGrid();
    }

    public void topMove() {
        Config.L("top");

        newGrid();
    }

    public void rightMove() {
        Config.L("right");

        newGrid();
    }

    public void bottomMove() {
        Config.L("bottom");

        newGrid();
    }
}
