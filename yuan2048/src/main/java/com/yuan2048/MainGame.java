package com.yuan2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by FanWenLong on 2018/4/20.
 */

public class MainGame {
    /**
     * 新生成动画
     */
    public static final int SPAWN_ANIMATION = -1;
    /**
     * 移动动画
     */
    public static final int MOVE_ANIMATION = 0;
    /**
     * 合并动画
     */
    public static final int MERGE_ANIMATION = 1;
    /**
     * 全局变暗动画
     */
    public static final int FADE_GLOBAL_ANIMATION = 0;

    /**
     * 移动时动画的时间
     */
    public static final long MOVE_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME;
    /**
     * 生成时的滑动时间
     */
    public static final long SPAWN_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME;
    /**
     * 通知的时间
     */
    public static final long NOTIFICATION_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME * 5;
    /**
     * 通知延时的时间
     */
    public static final long NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME;
    private static final String HIGH_SCORE = "high score";

    /**
     * 开始的最大值
     */
    public static final int startingMaxValue = 2048;
    /**
     * 结束的最大值
     */
    public static int endingMaxValue;

    //Odd state = game is not active
    //Even state = game is active
    //Win state = active state + 1
    public static final int GAME_WIN = 1;
    public static final int GAME_LOST = -1;
    public static final int GAME_NORMAL = 0;
    public static final int GAME_NORMAL_WON = 1;
    public static final int GAME_ENDLESS = 2;
    public static final int GAME_ENDLESS_WON = 3;

    public Grid grid = null;
    public AnimationGrid aGrid;
    /**
     * 格子的横向数
     */
    final int numSquaresX = 4;
    /**
     * 格子的纵向数
     */
    final int numSquaresY = 4;
    /**
     * 开始数
     */
    final int startTiles = 2;
    /**
     * 游戏状态
     */
    public int gameState = 0;
    /**
     * 是否撤销
     */
    public boolean canUndo;

    /**
     * 当前分数
     */
    public long score = 0;
    /**
     * 最高分
     */
    public long highScore = 0;

    /**
     * 上次的分数
     */
    public long lastScore = 0;
    /**
     * 上次游戏状态
     */
    public int lastGameState = 0;

    /**
     * 当前分数的缓存
     */
    private long bufferScore = 0;
    /**
     * 当前游戏状态的缓存
     */
    private int bufferGameState = 0;

    private Context mContext;

    private MainView mView;

    public MainGame(Context context, MainView view) {
        mContext = context;
        mView = view;
        endingMaxValue = (int) Math.pow(2, view.numCellTypes - 1);
    }

    /**
     * 开始游戏
     */
    public void newGame() {
        if (grid == null) {
            grid = new Grid(numSquaresX, numSquaresY);
        } else {
            prepareUndoState();
            saveUndoState();
            grid.clearGrid();
        }
        aGrid = new AnimationGrid(numSquaresX, numSquaresY);
        highScore = getHighScore();
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
        score = 0;
        gameState = GAME_NORMAL;
        addStartTiles();
        mView.refreshLastTime = true;
        mView.resyncTime();
        mView.invalidate();
    }

    /**
     * 生成两个格子
     */
    private void addStartTiles() {
        for (int xx = 0; xx < startTiles; xx++) {
            this.addRandomTile();
        }
    }

    /**
     * 生成格子中的值和位置
     */
    private void addRandomTile() {
        if (grid.isCellsAvailable()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Tile tile = new Tile(grid.randomAvailableCell(), value);
            spawnTile(tile);
        }
    }

    /**
     * 产生格子
     */
    private void spawnTile(Tile tile) {
        grid.insertTile(tile);
        aGrid.startAnimation(tile.getX(), tile.getY(), SPAWN_ANIMATION,
                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null); //Direction: -1 = EXPANDING
    }

    /**
     * 刷新最高分
     */
    private void recordHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(HIGH_SCORE, highScore);
        editor.commit();
    }

    /**
     * 获取最高分
     */
    private long getHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        return settings.getLong(HIGH_SCORE, -1);
    }

    /**
     * 准备生成格子
     */
    private void prepareTiles() {
        for (Tile[] array : grid.field) {
            for (Tile tile : array) {
                if (grid.isCellOccupied(tile)) {
                    tile.setMergedFrom(null);
                }
            }
        }
    }

    /**
     * 移动格子
     */
    private void moveTile(Tile tile, Cell cell) {
        grid.field[tile.getX()][tile.getY()] = null;
        grid.field[cell.getX()][cell.getY()] = tile;
        tile.updatePosition(cell);
    }

    /**
     * 保存撤销界面
     */
    private void saveUndoState() {
        grid.saveTiles();
        canUndo = true;
        lastScore = bufferScore;
        lastGameState = bufferGameState;
    }

    /**
     * 当前界面，分数等写入缓存
     */
    private void prepareUndoState() {
        grid.prepareSaveTiles();
        bufferScore = score;
        bufferGameState = gameState;
    }

    /**
     * 点击了撤销
     */
    public void revertUndoState() {
        if (canUndo) {
            canUndo = false;
            aGrid.cancelAnimations();
            grid.revertTiles();
            score = lastScore;
            gameState = lastGameState;
            mView.refreshLastTime = true;
            mView.invalidate();
        }
    }

    /**
     * 游戏胜利
     */
    public boolean gameWon() {
        return (gameState > 0 && gameState % 2 != 0);
    }

    /**
     * 游戏失败
     */
    public boolean gameLost() {
        return (gameState == GAME_LOST);
    }

    /**
     * 是否可以玩
     */
    public boolean isActive() {
        return !(gameWon() || gameLost());
    }

    /**
     * 手势移动
     */
    public void move(int direction) {
        aGrid.cancelAnimations();

        if (!isActive()) {
            return;
        }
        prepareUndoState();
        //获取方向，0: up, 1: right, 2: down, 3: left
        Cell vector = getVector(direction);

        //当向右或向下时，翻转list，默认0,1,2,3
        List<Integer> traversalsX = buildTraversalsX(vector);
        List<Integer> traversalsY = buildTraversalsY(vector);
        boolean moved = false;

        prepareTiles();

        for (int xx : traversalsX) {
            for (int yy : traversalsY) {
                Cell cell = new Cell(xx, yy);
                Tile tile = grid.getCellContent(cell);

                if (tile != null) {
                    Cell[] positions = findFarthestPosition(cell, vector);
                    Tile next = grid.getCellContent(positions[1]);

                    if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
                        Tile merged = new Tile(positions[1], tile.getValue() * 2);
                        Tile[] temp = {tile, next};
                        merged.setMergedFrom(temp);

                        grid.insertTile(merged);
                        grid.removeTile(tile);

                        // Converge the two tiles' positions
                        tile.updatePosition(positions[1]);

                        int[] extras = {xx, yy};
                        aGrid.startAnimation(merged.getX(), merged.getY(), MOVE_ANIMATION,
                                MOVE_ANIMATION_TIME, 0, extras); //Direction: 0 = MOVING MERGED
                        aGrid.startAnimation(merged.getX(), merged.getY(), MERGE_ANIMATION,
                                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);

                        // Update the score
                        score = score + merged.getValue();
                        highScore = Math.max(score, highScore);

                        // The mighty 2048 tile
                        if (merged.getValue() >= winValue() && !gameWon()) {
                            gameState = gameState + GAME_WIN; // Set win state
                            endGame();
                        }
                    } else {
                        moveTile(tile, positions[0]);
                        int[] extras = {xx, yy, 0};
                        aGrid.startAnimation(positions[0].getX(), positions[0].getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras); //Direction: 1 = MOVING NO MERGE
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true;
                    }
                }
            }
        }

        if (moved) {
            saveUndoState();
            addRandomTile();
            checkLose();
        }
        mView.resyncTime();
        mView.invalidate();
    }

    /**
     * 是否结束游戏
     */
    private void checkLose() {
        if (!movesAvailable() && !gameWon()) {
            gameState = GAME_LOST;
            endGame();
        }
    }

    /**
     * 结束游戏
     */
    private void endGame() {
        aGrid.startAnimation(-1, -1, FADE_GLOBAL_ANIMATION, NOTIFICATION_ANIMATION_TIME, NOTIFICATION_DELAY_TIME, null);
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
    }

    /**
     * 返回方向格子
     */
    private Cell getVector(int direction) {
        Cell[] map = {
                new Cell(0, -1), // up
                new Cell(1, 0),  // right
                new Cell(0, 1),  // down
                new Cell(-1, 0)  // left
        };
        return map[direction];
    }

    private List<Integer> buildTraversalsX(Cell vector) {
        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx < numSquaresX; xx++) {
            traversals.add(xx);
        }
        if (vector.getX() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    private List<Integer> buildTraversalsY(Cell vector) {
        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx < numSquaresY; xx++) {
            traversals.add(xx);
        }
        if (vector.getY() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    private Cell[] findFarthestPosition(Cell cell, Cell vector) {
        Cell previous;
        Cell nextCell = new Cell(cell.getX(), cell.getY());
        do {
            previous = nextCell;
            nextCell = new Cell(previous.getX() + vector.getX(),
                    previous.getY() + vector.getY());
        } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

        Cell[] answer = {previous, nextCell};
        return answer;
    }

    private boolean movesAvailable() {
        return grid.isCellsAvailable() || tileMatchesAvailable();
    }

    private boolean tileMatchesAvailable() {
        Tile tile;

        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                tile = grid.getCellContent(new Cell(xx, yy));

                if (tile != null) {
                    for (int direction = 0; direction < 4; direction++) {
                        Cell vector = getVector(direction);
                        Cell cell = new Cell(xx + vector.getX(), yy + vector.getY());

                        Tile other = grid.getCellContent(cell);

                        if (other != null && other.getValue() == tile.getValue()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean positionsEqual(Cell first, Cell second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    /**
     * 返回最大分数
     */
    private int winValue() {
        if (!canContinue()) {
            return endingMaxValue;
        } else {
            return startingMaxValue;
        }
    }

    public void setEndlessMode() {
        gameState = GAME_ENDLESS;
        mView.invalidate();
        mView.refreshLastTime = true;
    }

    public boolean canContinue() {
        return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON);
    }

}
