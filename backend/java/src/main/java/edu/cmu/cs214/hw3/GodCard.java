package edu.cmu.cs214.hw3;

import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class GodCard {
    protected static final String MOVE = "move";
    protected static final String BUILD = "build";
    protected static final int WIN_HEIGHT = 3;
    protected static final int WIN = 1;
    protected static final int LOSE = 0;
    protected static final int PLAY = -1;
    protected static final int ROW = 5;
    protected static final int COLUMN = 5;
    protected Grid grid;
    protected Player player;
    protected boolean myTurn;
    protected int state;
    protected String action;
    protected int movedWorkerId;

    public GodCard(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;
        this.myTurn = false;
        this.state = PLAY;
        this.action = MOVE;
        this.movedWorkerId = -1;
    }

    public boolean isMyTurn() {
        return this.myTurn;
    }

    public int getGameState() {
        return this.state;
    }

    /**
     * Player moves the selected worker to an adjacent unoccupied field.
     *
     * @param worker
     * @param x
     * @param y
     * @return {@code true} if worker moves successfully.
     */
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.player.getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Set<Point> movablePos = this.grid.movablePositions(prevX, prevY);
        Point target = new Point(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.grid.getFieldHeight(x, y));
            this.grid.updateGridAfterMove(worker, prevX, prevY, x, y);
            return true;
        }
        return false;
    }

    /**
     * Player adds a block or dome to an unoccupied adjacent field of worker's
     * new position.
     *
     * @param worker
     * @param x
     * @param y
     * @return {@code true} if worker builds tower successfully.
     */
    public boolean build(Worker worker, int x, int y) {
        Worker buildWorker = this.player.getWorker(worker.getWorkerId());
        int workerX = buildWorker.getX();
        int workerY = buildWorker.getY();
        Set<Point> buildablePos = this.grid.buildablePositions(workerX, workerY);
        Point target = new Point(x, y);
        if (buildablePos.contains(target)) {
            this.grid.buildTowerLevel(x, y);
            return true;
        }

        System.err.println("Target field[" + x + "][" + y + "] is not buildable.");
        return false;
    }

    public void checkWin() {
        List<Worker> workers = this.player.getAllWorkers();
        for (Worker w: workers) {
            if (w.getHeight() == WIN_HEIGHT) {
                this.state = WIN;
            }
        }
    }

    public void nextAction() {
        if (this.action.equals(MOVE)) {
            this.action = BUILD;
            this.myTurn = true;
        } else if (this.action.equals(BUILD)) {
            this.action = MOVE;
            this.myTurn = false;
        }
    }

    public boolean execute(Worker worker, int x, int y) {
        if (this.action.equals(MOVE)) {
            if (this.player.hasMovablePositions()) {
                System.out.println("Player " + this.player.getPlayerId() + " cannot move any worker.");
                this.state = LOSE;
                return false;
            }

            if (!this.move(worker, x, y)) {
                return false;
            }

            this.movedWorkerId = worker.getWorkerId();
        } else if (this.action.equals(BUILD)) {
            if (!(worker.getWorkerId() == this.movedWorkerId)) {
                return false;
            }

            if (!this.player.hasBuildablePositions(worker)) {
                System.out.println("Worker " + this.movedWorkerId + " cannot build any tower level.");
                this.state = LOSE;
                return false;
            }

            if (!this.build(worker, x, y)) {
                return false;
            }
        }
        checkWin();
        nextAction();
        return true;
    }
}
