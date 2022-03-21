package edu.cmu.cs214.hw3;

import java.awt.*;
import java.util.Set;

public class Demeter extends GodCard {
    private static final String SECOND_BUILD = "second_build";
    private int firstBuildX;
    private int firstBuildY;

    // Demeter: Your worker may build one additional time, but not on the same space.
    // Create a "pass" button or click on the worker's current location, indicating that
    // the player wants to skip the optional second build
    public Demeter(Grid grid, Player player) {
        super(grid, player);
        firstBuildX = -1;
        firstBuildY = -1;
    }

    public boolean secondBuild(Player player, Worker worker, int x, int y,
                               int firstBuildX, int firstBuildY) {
        Worker buildWorker = player.getWorker(worker.getWorkerId());
        int workerX = buildWorker.getX();
        int workerY = buildWorker.getY();
        Set<Point> buildablePos = this.grid.buildablePositions(workerX, workerY);
        Point firstBuild = new Point(firstBuildX, firstBuildY);
        buildablePos.remove(firstBuild);
        Point target = new Point(x, y);
        if (buildablePos.contains(target)) {
            this.grid.buildTowerLevel(x, y);
            return true;
        }

        System.err.println("Target field[" + x + "][" + y + "] is not buildable.");
        return false;
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
            firstBuildX = -1;
            firstBuildY = -1;
            this.myTurn = false;
        }
    }

    @Override
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
        } else if (this.action.equals(BUILD) || this.action.equals(SECOND_BUILD)) {
            if (!(worker.getWorkerId() == this.movedWorkerId)) {
                return false;
            }

            if (!this.player.hasBuildablePositions(worker)) {
                System.out.println("Worker " + this.movedWorkerId + " cannot build any tower level.");
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
                        && !secondBuild(this.player, worker, x, y, firstBuildX, firstBuildY)) {
                    return false;
                }
            }
        }
        checkWin();
        nextAction();
        return true;
    }
}
