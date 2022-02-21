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
    private int[][] towerHeight;
    // Occupied means having worker or tower height == 4
    private boolean[][] occupied;

    /**
     * Creates a new {@link Grid} instance.
     */
    public Grid() {
        towerHeight = new int[ROW][COLUMN];
        occupied = new boolean[ROW][COLUMN];
        for (int i = 0 ; i <ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                towerHeight[i][j] = 0;
                occupied[i][j] = false;
            }
        }
    }

    public int getFieldHeight(int x, int y) {
        return towerHeight[x][y];
    }

    public boolean isOccupied(int x, int y) {
        return occupied[x][y];
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
     * @param worker The worker to be moved.
     * @param x X coordinate of worker's position.
     * @param y Y coordinate of worker's position.
     * @return A set of Points in the grid.
     */
    public Set<Point> movablePositions(Worker worker, int x, int y) {
        Set<Point> movable = new HashSet<>();
        for (int i = 0; i < DELTA_X.length; i++) {
            int coordinateX = x + DELTA_X[i];
            int coordinateY = y + DELTA_Y[i];
            if (coordinateX >= 0 && coordinateX < ROW && (coordinateY) >= 0 && (coordinateY) < COLUMN &&
                    !occupied[coordinateX][coordinateY] &&
                    (towerHeight[coordinateX][coordinateY] - worker.getHeight()) <= 1) {
                Point point = new Point(coordinateX, coordinateY);
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
    public void updateAfterMove(int prevX, int prevY, int currX, int currY) {
        occupied[prevX][prevY] = false;
        occupied[currX][currY] = true;
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
            int coordinateX = x + DELTA_X[i];
            int coordinateY = y + DELTA_Y[i];
            if (coordinateX >= 0 && coordinateX < ROW && (coordinateY) >= 0 && (coordinateY) < COLUMN &&
                    !occupied[coordinateX][coordinateY]) {
                Point point = new Point(coordinateX, coordinateY);
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
    public void buildTowerLevel(int x, int y) {
        assert !occupied[x][y];
        assert towerHeight[x][y] < DOME_HEIGHT;

        towerHeight[x][y] += 1;
        if (towerHeight[x][y] == DOME_HEIGHT) {
            occupied[x][y] = true;
        }
    }
}
