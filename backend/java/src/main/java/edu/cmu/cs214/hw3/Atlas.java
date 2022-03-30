package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.Set;

/**
 * Atlas: Your Worker may build a dome at any level. (Click on the worker's
 * current location to skip the optional second build.)
 */
public class Atlas extends GodCard {
    private static final String BUILD_DOME = " build a dome";

    public Atlas(Grid grid, Player player) {
        super(grid, player);
    }

    public boolean buildDome(Worker worker, int x, int y) {
        Worker buildWorker = this.getPlayer().getWorker(worker.getWorkerId());
        int workerX = buildWorker.getX();
        int workerY = buildWorker.getY();
        Set<Point> buildablePos = this.getGrid().getBuildablePositions(workerX, workerY);
        Point target = new Point(x, y);
        if (buildablePos.contains(target)) {
            this.getGrid().buildDomeAtAnyLevel(x, y);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not buildable.");
            return false;
        }
    }

    @Override
    public void nextAction() {
        switch (this.getAction()) {
            case MOVE -> {
                this.setAction(BUILD);
                this.setMyTurn(true);
            }
            case BUILD -> {
                this.setAction(BUILD_DOME);
                this.setMyTurn(true);
            }
            case BUILD_DOME -> {
                this.setAction(MOVE);
                this.setMyTurn(false);
            }
            default -> {
            }
        }
    }

    @Override
    public boolean execute(Worker worker, int x, int y) {
        if (this.getAction().equals(MOVE)) {
            if (!this.getPlayer().hasMovablePositions()) {
                System.out.println("Player " + this.getPlayer().getPlayerId() + " cannot move any worker.");
                this.setState(LOSE);
                return false;
            }

            if (!this.move(worker, x, y)) {
                return false;
            }

            this.setMovedWorkerId(worker.getWorkerId());
        } else if (this.getAction().equals(BUILD) || this.getAction().equals(BUILD_DOME)) {
            if (worker.getWorkerId() != this.getMovedWorkerId()) {
                return false;
            }

            if (!this.getPlayer().hasBuildablePositions()) {
                System.out.println("Player " + this.getPlayer().getPlayerId() + " cannot build any tower level.");
                this.setState(LOSE);
                return false;
            }

            if (this.getAction().equals(BUILD)) {
                if (!this.build(worker, x, y)) {
                    return false;
                }
            } else if (this.getAction().equals(BUILD_DOME)) {
                // skip the optional second build by clicking on the worker's current location
                if ((x != worker.getX() || y != worker.getY())
                        && !this.buildDome(worker, x, y)) {
                    return false;
                }

                this.setMovedWorkerId(-1);
            }
        }
        checkWin();
        nextAction();
        return true;
    }

    @Override
    public Set<Point> getValidPositions(Worker worker) {
        if (this.getAction().equals(MOVE)) {
            return this.getGrid().getMovablePositions(worker.getX(), worker.getY());
        } else if (this.getAction().equals(BUILD)) {
            return this.getGrid().getBuildablePositions(worker.getX(), worker.getY());
        } else if (this.getAction().equals(BUILD_DOME)) {
            Set<Point> buildablePos = this.getGrid().getBuildablePositions(worker.getX(), worker.getY());
            buildablePos.add(new Point(worker.getX(), worker.getY()));
            return buildablePos;
        }
        return null;
    }
}
