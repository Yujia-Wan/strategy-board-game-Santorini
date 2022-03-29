package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * Minotaur: Your worker may move into an opponent Worker's space, if their Worker can
 * be forced one space straight backwards to an unoccupied space at any level.
 */
public class Minotaur extends GodCard {

    public Minotaur(Grid grid, Player player) {
        super(grid, player);
    }

    /**
     * Moves current player's worker to an opponent worker's space, and forces the opponent
     * worker one space straight backwards to an unoccupied space at any level.
     *
     * @param myWorker Current player's worker
     * @param oppWorker Opponent player's worker
     * @param prevX X coordinate of current player's worker before move.
     * @param prevY Y coordinate of current player's worker before move.
     * @param dstX X coordinate of current player's worker's target as well as opponent worker's origin position.
     * @param dstY Y coordinate of current player's worker's target as well as opponent worker's origin position.
     * @param oppX X coordinate of opponent worker's target position.
     * @param oppY Y coordinate of opponent worker's target position.
     */
    private void moveTwoWorkers(Worker myWorker, Worker oppWorker, int prevX, int prevY,
                               int dstX, int dstY, int oppX, int oppY) {
        oppWorker.setPositionAndHeight(oppX, oppY, this.getGrid().getFieldHeight(oppX, oppY));
        this.getGrid().updateGridAfterMove(oppWorker, dstX, dstY);
        myWorker.setPositionAndHeight(dstX, dstY, this.getGrid().getFieldHeight(dstX, dstY));
        this.getGrid().updateGridAfterMove(myWorker, prevX, prevY);
    }

    @Override
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.getPlayer().getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Point target = new Point(x, y);

        // First check if target is an opponent worker's space, if it is and valid, change two
        // workers' positions; if not, do regular move.
        // get opponent workers' positions
        Set<Point> allWorkersPos = this.getGrid().getAllWorkersPositions();
        Set<Point> myWorkersPos = this.getPlayer().getAllWorkersPositions();
        Set<Point> oppWorkersPos = new HashSet<>();
        for (Point p: allWorkersPos) {
            if (!myWorkersPos.contains(p)) {
                oppWorkersPos.add(p);
            }
        }
        if (oppWorkersPos.contains(target)) {
            Worker oppWorker = this.getGrid().getFieldWorker(x, y);
            if (prevX == x && prevY != y) {
                int oppTargetX = x;
                int oppTargetY = y + (prevY < y? 1: -1);
                if (0 <= oppTargetY && oppTargetY < COLUMN && !this.getGrid().isOccupied(oppTargetX, oppTargetY)) {
                    // can be forced one space straight backwards
                    moveTwoWorkers(worker, oppWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }

            if (prevY == y && prevX != x) {
                int oppTargetX = x + (prevX < x? 1: -1);
                int oppTargetY = y;
                if (0 <= oppTargetX && oppTargetX < ROW && !this.getGrid().isOccupied(oppTargetX, oppTargetY)) {
                    moveTwoWorkers(worker, oppWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }

            if (Math.abs(prevX - x) == Math.abs(prevY - y)) {
                int oppTargetX = x + (prevX < x? 1: -1);
                int oppTargetY = y + (prevY < y? 1: -1);
                if (0 <= oppTargetX && oppTargetX < ROW && 0 <= oppTargetY && oppTargetY < COLUMN
                        && !this.getGrid().isOccupied(oppTargetX, oppTargetY)) {
                    moveTwoWorkers(worker, oppWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }
        }

        Set<Point> movablePos = this.getGrid().getMovablePositions(prevX, prevY);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.getGrid().getFieldHeight(x, y));
            this.getGrid().updateGridAfterMove(worker, prevX, prevY);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not movable.");
            return false;
        }
    }

    private Set<Point> getValidOppWorkerPos(Worker worker) {
        int myWorkerX = worker.getX();
        int myWorkerY = worker.getY();
        Set<Point> allWorkersPos = this.getGrid().getAllWorkersPositions();
        Set<Point> myWorkersPos = this.getPlayer().getAllWorkersPositions();
        Set<Point> validOppWorkerPos = new HashSet<>();
        for (Point p: allWorkersPos) {
            if (!myWorkersPos.contains(p)) {
                int oppTargetX = p.x + (myWorkerX < p.x? 1: -1);
                int oppTargetY = p.y + (myWorkerY < p.y? 1: -1);
                if ((myWorkerX == p.x || myWorkerY == p.y || Math.abs(myWorkerX - p.x) == Math.abs(myWorkerY - p.y))
                        && 0 <= oppTargetX && oppTargetX < ROW && 0 <= oppTargetY && oppTargetY < COLUMN
                        && !this.getGrid().isOccupied(oppTargetX, oppTargetY)) {
                    validOppWorkerPos.add(p);
                }
            }
        }
        return validOppWorkerPos;
    }

    @Override
    public Set<Point> getValidPositions(Worker worker) {
        if (this.getAction().equals(MOVE)) {
            Set<Point> moveValidPos = this.getGrid().getMovablePositions(worker.getX(), worker.getY());
            moveValidPos.addAll(this.getValidOppWorkerPos(worker));
            return moveValidPos;
        } else if (this.getAction().equals((BUILD))) {
            return this.getGrid().getBuildablePositions(worker.getX(), worker.getY());
        }
        return null;
    }
}
