package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Grid {
    private static final int ROW = 5;
    private static final int COLUMN = 5;
    private static final int DOME_HEIGHT = 4;
    private static final int[] DELTA_X = new int[] {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] DELTA_Y = new int[] {-1, -1, -1, 0, 0, 1, 1, 1};
    private int[][] height;
    private boolean[][] occupied;
    private Worker[][] workerMap;

    /**
     * Creates a new {@link Grid} instance.
     */
    public Grid() {
        this.height = new int[ROW][COLUMN];
        this.occupied = new boolean[ROW][COLUMN];
        this.workerMap = new Worker[ROW][COLUMN];
        for (int i = 0 ; i <ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                this.height[i][j] = 0;
                this.occupied[i][j] = false;
                this.workerMap[i][j] = null;
            }
        }
    }

    public int getFieldHeight(int x, int y) {
        return this.height[x][y];
    }

    public boolean isOccupied(int x, int y) {
        return this.occupied[x][y];
    }

    public Worker getFieldWorker(int x, int y) {
        return this.workerMap[x][y];
    }

    public Set<Point> getAllWorkersPositions() {
        Set<Point> positions = new HashSet<>();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (this.workerMap[i][j] != null) {
                    positions.add(new Point(i, j));
                }
            }
        }
        return positions;
    }

    /**
     * Updates field's status after initializing worker's starting position.
     *
     * @param worker Initialized worker.
     */
    public void updateAfterInitWorkerPos(Worker worker) {
        int x = worker.getX();
        int y = worker.getY();
        assert(0 <= x && x < ROW && 0 <= y && y < COLUMN && !this.occupied[x][y]);

        this.occupied[x][y] = true;
        this.workerMap[x][y] = worker;
    }

    /**
     * Retrieves positions the worker can be moved to.
     *
     * @param x X coordinate of worker's position.
     * @param y Y coordinate of worker's position.
     * @return A set of Points in the grid.
     */
    public Set<Point> getMovablePositions(int x, int y) {
        Set<Point> movable = new HashSet<>();
        for (int i = 0; i < DELTA_X.length; i++) {
            int coordX = x + DELTA_X[i];
            int coordY = y + DELTA_Y[i];
            if (0 <= coordX && coordX < ROW && 0 <= coordY && coordY < COLUMN
                    && !this.occupied[coordX][coordY]
                    && (this.height[coordX][coordY] - this.height[x][y]) <= 1) {
                movable.add(new Point(coordX, coordY));
            }
        }
        return movable;
    }

    /**
     * Updates field's status after move action.
     *
     * @param worker Worker been moved.
     * @param prevX X coordinate of worker's previous position.
     * @param prevY Y coordinate of worker's previous position.
     */
    public void updateGridAfterMove(Worker worker, int prevX, int prevY) {
        int currX = worker.getX();
        int currY = worker.getY();
        this.occupied[prevX][prevY] = false;
        this.occupied[currX][currY] = true;
        this.workerMap[prevX][prevY] = null;
        this.workerMap[currX][currY] = worker;
    }

    /**
     * Retrieves positions the worker can build tower on.
     *
     * @param x X coordinate of worker's position.
     * @param y Y coordinate of worker's position.
     * @return A set of points in the grid.
     */
    public Set<Point> getBuildablePositions(int x, int y) {
        Set<Point> buildable = new HashSet<>();
        for (int i = 0; i < DELTA_X.length; i++) {
            int coordX = x + DELTA_X[i];
            int coordY = y + DELTA_Y[i];
            if (0 <= coordX && coordX < ROW && 0 <= coordY && coordY < COLUMN
                    && !this.occupied[coordX][coordY]
                    && this.height[coordX][coordY] < DOME_HEIGHT) {
                buildable.add(new Point(coordX, coordY));
            }
        }
        return buildable;
    }

    /**
     * Build a new level of the tower at the field.
     *
     * @param x X coordinate of field.
     * @param y Y coordinate of field.
     */
    public void buildTowerLevel(int x, int y) {
        assert(0 <= x && x < ROW && 0 <= y && y < COLUMN && !this.occupied[x][y]);

        this.height[x][y] += 1;
        if (this.height[x][y] == DOME_HEIGHT) {
            this.occupied[x][y] = true;
        }
    }
}
