package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.Set;

/**
 * Artemis: Your Worker may move one additional time, but not back to its initial space.
 * (Click on the worker's current location to skip the optional second move.)
 */
public class Artemis extends GodCard {
    private static final String SECOND_MOVE = "second move";
    private int initX;
    private int initY;

    public Artemis(Grid grid, Player player) {
        super(grid, player);
        this.initX = -1;
        this.initY = -1;
    }

    /**
     * Move one additional time.
     *
     * @param worker Worker to move.
     * @param x Target x coordinate.
     * @param y Target Y coordinate.
     * @param initX X coordinate of worker's position before move.
     * @param initY Y coordinate of worker's position before move.
     * @return {@code true} if second move succeeds.
     */
    public boolean secondMove(Worker worker, int x, int y, int initX, int initY) {
        Worker moveWorker = this.getPlayer().getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Set<Point> movablePos = this.getGrid().getMovablePositions(prevX, prevY);
        Point init = new Point(initX, initY);
        // not back to its initial space
        movablePos.remove(init);
        Point target = new Point(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.getGrid().getFieldHeight(x, y));
            this.getGrid().updateGridAfterMove(moveWorker, prevX, prevY);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not movable.");
            return false;
        }
    }

    @Override
    public void nextAction() {
        switch (this.getAction()) {
            case MOVE -> {
                this.setAction(SECOND_MOVE);
                this.setMyTurn(true);
            }
            case SECOND_MOVE -> {
                this.setAction(BUILD);
                this.setMyTurn(true);
            }
            case BUILD -> {
                this.setAction(MOVE);
                this.setMyTurn(false);
            }
            default -> {
            }
        }
    }

    @Override
    public boolean execute(Worker worker, int x, int y) {
        if (this.getAction().equals(MOVE) || this.getAction().equals(SECOND_MOVE)) {
            if (!this.getPlayer().hasMovablePositions()) {
                System.out.println("Player " + this.getPlayer().getPlayerId() + " cannot move any worker.");
                this.setState(LOSE);
                return false;
            }

            if (this.getAction().equals(MOVE)) {
                this.initX = worker.getX();
                this.initY = worker.getY();

                if (!this.move(worker, x, y)) {
                    return false;
                }
            } else if (this.getAction().equals(SECOND_MOVE)) {
                if ((x != worker.getX() || y != worker.getY())
                        && !this.secondMove(worker, x, y, this.initX, this.initY)) {
                    return false;
                }

                this.initX = -1;
                this.initY = -1;
            }

            this.setMovedWorkerId(worker.getWorkerId());
        } else if (this.getAction().equals(BUILD)) {
            if (worker.getWorkerId() != this.getMovedWorkerId()) {
                return false;
            }

            if (!this.getPlayer().hasBuildablePositions()) {
                System.out.println("Player " + this.getPlayer().getPlayerId() + " cannot build any tower level.");
                this.setState(LOSE);
                return false;
            }

            if (!this.build(worker, x, y)) {
                return false;
            }

            this.setMovedWorkerId(-1);
        }
        checkWin();
        nextAction();
        return true;
    }

    @Override
    public Set<Point> getValidPositions(Worker worker) {
        if (this.getAction().equals(MOVE)) {
            return this.getGrid().getMovablePositions(worker.getX(), worker.getY());
        } else if (this.getAction().equals(SECOND_MOVE)) {
            Set<Point> secondMovablePos = this.getGrid().getMovablePositions(worker.getX(), worker.getY());
            secondMovablePos.remove(new Point(this.initX, this.initY));
            secondMovablePos.add(new Point(worker.getX(), worker.getY()));
            return secondMovablePos;
        } else if (this.getAction().equals((BUILD))) {
            return this.getGrid().getBuildablePositions(worker.getX(), worker.getY());
        }
        return null;
    }
}
