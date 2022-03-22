package edu.cmu.cs214.hw3;

import java.awt.*;
import java.util.Set;

/**
 * Demeter: Your worker may build one additional time, but not on the same space.
 * Create a "pass" button or click on the worker's current location, indicating that
 * the player wants to skip the optional second build.
 */
public class Demeter extends GodCard {
    private static final String SECOND_BUILD = "secondBuild";
    private int firstBuildX;
    private int firstBuildY;

    public Demeter(Grid grid, Player player) {
        super(grid, player);
        this.firstBuildX = -1;
        this.firstBuildY = -1;
    }

    /**
     * Builds one additional time.
     *
     * @param worker Worker to build tower.
     * @param x X coordinate to build.
     * @param y Y coordinate to build.
     * @param firstBuildX X coordinate of first build.
     * @param firstBuildY Y coordinate of first build.
     * @return
     */
    public boolean secondBuild(Worker worker, int x, int y,
                               int firstBuildX, int firstBuildY) {
        Worker buildWorker = this.player.getWorker(worker.getWorkerId());
        int workerX = buildWorker.getX();
        int workerY = buildWorker.getY();
        Set<Point> buildablePos = this.grid.getBuildablePositions(workerX, workerY);
        Point firstBuild = new Point(firstBuildX, firstBuildY);
        // not on the same space
        buildablePos.remove(firstBuild);
        Point target = new Point(x, y);
        if (buildablePos.contains(target)) {
            this.grid.buildTowerLevel(x, y);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not buildable.");
            return false;
        }
    }

    @Override
    public void nextAction() {
        if (this.action.equals(MOVE)) {
            this.action = BUILD;
            this.myTurn = true;
        } else if (this.action.equals(BUILD)) {
            this.action = SECOND_BUILD;
            this.myTurn = true;
        } else if (this.action.equals(SECOND_BUILD)) {
            this.action = MOVE;
            this.myTurn = false;
            this.firstBuildX = -1;
            this.firstBuildY = -1;
        }
    }

    @Override
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
        } else if (this.action.equals(BUILD) || this.action.equals(SECOND_BUILD)) {
            if (worker.getWorkerId() != this.movedWorkerId) {
                return false;
            }

            if (!this.player.hasBuildablePositions()) {
                System.out.println("Player " + this.player.getPlayerId() + " cannot build any tower level.");
                this.state = LOSE;
                return false;
            }

            if (this.action.equals(BUILD)) {
                if (!this.build(worker, x, y)) {
                    return false;
                }

                this.firstBuildX = x;
                this.firstBuildY = y;
            } else if (this.action.equals(SECOND_BUILD)) {
                // skip the optional second build by clicking on the worker's current location
                if ((x != worker.getX() || y != worker.getY())
                        && !secondBuild(worker, x, y, this.firstBuildX, this.firstBuildY)) {
                    return false;
                }
            }
        }
        checkWin();
        nextAction();
        return true;
    }
}
