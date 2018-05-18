package com.fan.my2048;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FanWenLong on 2018/4/19.
 * <p>
 * 游戏控制器
 */

public class GameControl {

    private static GameControl gameControl = null;
    public List<Grid> gridList = new ArrayList<Grid>();

    public static GameControl newInstance() {
        if (gameControl == null) {
            return new GameControl();
        }
        return gameControl;
    }

    private GameControl() {
        for (int i = 0; i < Config.rowNum; i++) {
            for (int j = 0; j < Config.columnNum; j++) {
                int[] num = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
                int p = (int) (Math.random() * 10);
//                Config.L("p=" + p);
                gridList.add(new Grid(i, j, "" + num[p]));
            }
        }
    }

    /**
     * 产生新grid
     */
    public Grid newGrid() {
        Grid grid = new Grid();

        return grid;
    }
}
