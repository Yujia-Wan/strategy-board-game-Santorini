package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Grid {
    private static final int ROW = 5;
    private static final int COLUMN = 5;
    private static final int DOME_HEIGHT = 4;
    private static final int WIN_HEIGHT = 3;
    private static final int[] DELTA_X = new int[] {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] DELTA_Y = new int[] {-1, -1, -1, 0, 0, 1, 1, 1};
    private int[][] height;
    private boolean[][] occupied;
    private Worker[][] workerPosition;

    /**
     * Creates a new {@link Grid} instance.
     */
    public Grid() {
        height = new int[ROW][COLUMN];
        occupied = new boolean[ROW][COLUMN];
        workerPosition = new Worker[ROW][COLUMN];
        for (int i = 0 ; i <ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                height[i][j] = 0;
                occupied[i][j] = false;
                workerPosition[i][j] = null;
            }
        }
    }

    public int getFieldHeight(int x, int y) {
        return this.height[x][y];
    }

    public boolean isOccupied(int x, int y) {
        return this.occupied[x][y];
    }

    public boolean setWorkerPosition(Worker worker, int x, int y) {
        if (x < 0 || x >= ROW || y < 0 || y >= COLUMN || this.occupied[x][y]) {
            return false;
        }

        worker.setPositionAndHeight(x, y, height[x][y]);
        this.occupied[x][y] = true;
        this.workerPosition[x][y] = worker;
        return true;
    }

    /**
     * Updates field's status after initializing worker's starting position.
     *
     * @param x X coordinate of worker's position.
     * @param y Y coordinate of worker's position.
     */
    public void updateAfterInit(int x, int y) {
        occupied[x][y] = true;
    }


    /**
     * Retrieves positions the worker can be moved to.
     *
     * @param x X coordinate of worker's position.
     * @param y Y coordinate of worker's position.
     * @return A set of Points in the grid.
     */
    public Set<Point> movablePositions(int x, int y) {
        Set<Point> movable = new HashSet<>();
        for (int i = 0; i < DELTA_X.length; i++) {
            int coordX = x + DELTA_X[i];
            int coordY = y + DELTA_Y[i];
            if (coordX >= 0 && coordX < ROW && (coordY) >= 0 && (coordY) < COLUMN
                    && !this.occupied[coordX][coordY]
                    && (this.height[coordX][coordY] - this.height[x][y]) <= 1) {
                Point point = new Point(coordX, coordY);
                movable.add(point);
            }
        }
        return movable;
    }

    /**
     * Updates field's status after move action.
     *
     * @param prevX X coordinate of worker's previous position.
     * @param prevY Y coordinate of worker's previous position.
     * @param currX X coordinate of worker's current position.
     * @param currY Y coordinate of worker's current position.
     */
    public void updateGridAfterMove(Worker worker, int prevX, int prevY, int currX, int currY) {
        this.occupied[prevX][prevY] = false;
        this.occupied[currX][currY] = true;
        this.workerPosition[prevX][prevY] = null;
        this.workerPosition[currX][currY] = worker;
    }


    /**
     * Retrieves positions the worker can build tower on.
     *
     * @param x X coordinate of worker's position.
     * @param y Y coordinate of worker's position.
     * @return A set of points in the grid.
     */
    public Set<Point> buildablePositions(int x, int y) {
        Set<Point> buildable = new HashSet<>();
        for (int i = 0; i < DELTA_X.length; i++) {
            int coordX = x + DELTA_X[i];
            int coordY = y + DELTA_Y[i];
            if (coordX >= 0 && coordX < ROW && (coordY) >= 0 && (coordY) < COLUMN
                    && !this.occupied[coordX][coordY]
                    && this.height[coordX][coordY] < DOME_HEIGHT) {
                Point point = new Point(coordX, coordY);
                buildable.add(point);
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
    public boolean buildTowerLevel(int x, int y) {
        if (x < 0 || x >= ROW || y < 0 || y >= COLUMN || this.occupied[x][y]) {
            return false;
        }

        height[x][y] += 1;
        if (height[x][y] == DOME_HEIGHT) {
            occupied[x][y] = true;
        }
        return true;
    }
}
