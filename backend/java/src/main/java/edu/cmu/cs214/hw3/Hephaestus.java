package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * Hephaestus: Your Worker may build one additional block (not dome) on top of your first block.
 * (Click on the worker's current location to skip the optional second build.)
 */
public class Hephaestus extends GodCard {
    private static final String BUILD_ADDL_BLOCK = "build one additional block";
    private int firstBuildX;
    private int firstBuildY;

    public Hephaestus(Grid grid, Player player) {
        super(grid, player);
        this.firstBuildX = -1;
        this.firstBuildY = -1;
    }

    /**
     * Builds one additional block (not dome) on top of the worker's first block.
     *
     * @param x X coordinate to build.
     * @param y Y coordinate to build.
     * @param firstBuildX X coordinate of first build.
     * @param firstBuildY Y coordinate of first build.
     * @return {@code true} if additional build succeeds.
     */
    public boolean buildAddlBlock(int x, int y, int firstBuildX, int firstBuildY) {
        if (x != firstBuildX || y != firstBuildY || this.getGrid().isOccupied(firstBuildX, firstBuildY) ||
                (this.getGrid().getFieldHeight(firstBuildX, firstBuildY) >= Grid.DOME_HEIGHT - 1)) {
            return false;
        }
        this.getGrid().buildTowerLevel(x, y);
        return true;
    }

    @Override
    public void nextAction() {
        switch (this.getAction()) {
            case MOVE -> {
                this.setAction(BUILD);
                this.setMyTurn(true);
            }
            case BUILD -> {
                this.setAction(BUILD_ADDL_BLOCK);
                this.setMyTurn(true);
            }
            case BUILD_ADDL_BLOCK -> {
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
        } else if (this.getAction().equals(BUILD) || this.getAction().equals(BUILD_ADDL_BLOCK)) {
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

                this.firstBuildX = x;
                this.firstBuildY = y;
            } else if (this.getAction().equals(BUILD_ADDL_BLOCK)) {
                // skip the optional second build by clicking on the worker's current location
                if ((x != worker.getX() || y != worker.getY())
                        && !this.buildAddlBlock(x, y, this.firstBuildX, this.firstBuildY)) {
                    return false;
                }

                this.setMovedWorkerId(-1);
                this.firstBuildX = -1;
                this.firstBuildY = -1;
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
        } else if (this.getAction().equals((BUILD))) {
            return this.getGrid().getBuildablePositions(worker.getX(), worker.getY());
        } else if (this.getAction().equals((BUILD_ADDL_BLOCK))) {
            Set<Point> buildAddlBlockValidPos = new HashSet<>();
            if (this.firstBuildX != -1 && this.firstBuildY!= -1 &&
                    !this.getGrid().isOccupied(this.firstBuildX, this.firstBuildY) &&
                    (this.getGrid().getFieldHeight(this.firstBuildX, this.firstBuildY) < Grid.DOME_HEIGHT - 1)) {
                buildAddlBlockValidPos.add(new Point(this.firstBuildX, this.firstBuildY));
            }
            buildAddlBlockValidPos.add(new Point(worker.getX(), worker.getY()));
            return buildAddlBlockValidPos;
        }
        return null;
    }
}
