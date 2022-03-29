package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.List;
import java.util.Set;

public abstract class GodCard {
    protected static final String MOVE = "move";
    protected static final String BUILD = "build";
    protected static final int ROW = 5;
    protected static final int COLUMN = 5;
    protected static final int WIN_HEIGHT = 3;
    protected static final int WIN = 1;
    protected static final int LOSE = 0;
    protected static final int PLAY = -1;
    private Grid grid;
    private Player player;
    private boolean myTurn;
    private String action;
    private int state;
    private int movedWorkerId;

    public GodCard(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;
        this.myTurn = false;
        this.action = MOVE;
        this.state = PLAY;
        this.movedWorkerId = -1;
    }

    public Grid getGrid() {
        return this.grid;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean getMyTurn() {
        return this.myTurn;
    }

    public String getAction() {
        return this.action;
    }

    public int getState() {
        return this.state;
    }

    public int getMovedWorkerId() {
        return this.movedWorkerId;
    }

    public void setMyTurn(boolean value) {
        this.myTurn = value;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setMovedWorkerId(int id) {
        this.movedWorkerId = id;
    }

    /**
     * Player moves the selected worker to an adjacent unoccupied field.
     *
     * @param worker Worker to be moved.
     * @param x Target position's x coordinate.
     * @param y Target position's y coordinate.
     * @return {@code true} if worker moves successfully.
     */
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.player.getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Set<Point> movablePos = this.grid.getMovablePositions(prevX, prevY);
        Point target = new Point(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.grid.getFieldHeight(x, y));
            this.grid.updateGridAfterMove(moveWorker, prevX, prevY);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not movable.");
            return false;
        }
    }

    /**
     * Player adds a block or dome to an unoccupied adjacent field of worker's
     * new position.
     *
     * @param worker Worker to build a tower level.
     * @param x Target position's x coordinate.
     * @param y Target position's y coordinate.
     * @return {@code true} if worker builds tower successfully.
     */
    public boolean build(Worker worker, int x, int y) {
        Worker buildWorker = this.player.getWorker(worker.getWorkerId());
        int workerX = buildWorker.getX();
        int workerY = buildWorker.getY();
        Set<Point> buildablePos = this.grid.getBuildablePositions(workerX, workerY);
        Point target = new Point(x, y);
        if (buildablePos.contains(target)) {
            this.grid.buildTowerLevel(x, y);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not buildable.");
            return false;
        }
    }

    /**
     * Checks if the player has won.
     */
    public void checkWin() {
        List<Worker> workers = this.player.getAllWorkers();
        for (Worker w: workers) {
            if (w.getHeight() == WIN_HEIGHT) {
                this.state = WIN;
                break;
            }
        }
    }

    /**
     * Moves to next action and checks if the player needs to end turn.
     */
    public void nextAction() {
        if (this.action.equals(MOVE)) {
            this.action = BUILD;
            this.myTurn = true;
        } else if (this.action.equals(BUILD)) {
            this.action = MOVE;
            this.myTurn = false;
        }
    }

    /**
     * Executes player's action of move or build.
     *
     * @param worker Worker to be moved or build tower level.
     * @param x Destination x index.
     * @param y Destination y index.
     * @return {@code true} if action is executed successfully.
     */
    public boolean execute(Worker worker, int x, int y) {
        if (this.action.equals(MOVE)) {
            if (!this.player.hasMovablePositions()) {
                System.out.println("Player " + this.player.getPlayerId() + " cannot move any worker.");
                this.state = LOSE;
                return false;
            }

            if (!this.move(worker, x, y)) {
                return false;
            }

            this.movedWorkerId = worker.getWorkerId();
        } else if (this.action.equals(BUILD)) {
            if (worker.getWorkerId() != this.movedWorkerId) {
                return false;
            }

            if (!this.player.hasBuildablePositions()) {
                System.out.println("Player " + this.player.getPlayerId() + " cannot build any tower level.");
                this.state = LOSE;
                return false;
            }

            if (!this.build(worker, x, y)) {
                return false;
            }

            this.movedWorkerId = -1;
        }
        checkWin();
        nextAction();
        return true;
    }

    /**
     * Retrieves all valid positions of the worker before action.
     *
     * @param worker The worker to take action.
     * @return A set of field coordinates.
     */
    public Set<Point> getValidPositions(Worker worker) {
        if (this.action.equals(MOVE)) {
            return this.grid.getMovablePositions(worker.getX(), worker.getY());
        } else if (this.action.equals((BUILD))) {
            return this.grid.getBuildablePositions(worker.getX(), worker.getY());
        }
        return null;
    }
}
