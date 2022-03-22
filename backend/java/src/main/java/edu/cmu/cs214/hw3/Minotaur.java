package edu.cmu.cs214.hw3;

import java.awt.*;
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
        oppWorker.setPositionAndHeight(oppX, oppY, this.grid.getFieldHeight(oppX, oppY));
        this.grid.updateGridAfterMove(oppWorker, dstX, dstY, oppX, oppY);
        myWorker.setPositionAndHeight(dstX, dstY, this.grid.getFieldHeight(dstX, dstY));
        this.grid.updateGridAfterMove(myWorker, prevX, prevY, dstX, dstY);
    }

    @Override
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.player.getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Point target = new Point(x, y);

        // first check if target is an opponent worker's space, if it is and valid, change two workers'
        // position; if not, do regular move
        // get opponent workers' positions
        Set<Point> allWorkersPos = this.grid.getAllWorkersPosition();
        Set<Point> myWorkersPos = this.player.getAllWorkersPosition();
        Set<Point> oppWorkersPos = new HashSet<>();
        for (Point p: allWorkersPos) {
            if (!myWorkersPos.contains(p)) {
                oppWorkersPos.add(p);
            }
        }
        if (oppWorkersPos.contains(target)) {
            Worker oppWorker = this.grid.getFieldWorker(x, y);
            if (prevX == x && prevY != y) {
                int oppTargetX = x;
                int oppTargetY = y + (prevY < y? 1: -1);
                if (0 <= oppTargetY && oppTargetY < COLUMN && !this.grid.isOccupied(oppTargetX, oppTargetY)) {
                    // can be forced one space straight backwards
                    moveTwoWorkers(worker, oppWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }

            if (prevY == y && prevX != x) {
                int oppTargetX = x + (prevX < x? 1: -1);
                int oppTargetY = y;
                if (0 <= oppTargetX && oppTargetX < ROW && !this.grid.isOccupied(oppTargetX, oppTargetY)) {
                    moveTwoWorkers(worker, oppWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }

            if (Math.abs(prevX - x) == Math.abs(prevY - y)) {
                int oppTargetX = x + (prevX < x? 1: -1);
                int oppTargetY = y + (prevY < y? 1: -1);
                if (0 <= oppTargetX && oppTargetX < ROW && 0 <= oppTargetY && oppTargetY < COLUMN
                        && !this.grid.isOccupied(oppTargetX, oppTargetY)) {
                    moveTwoWorkers(worker, oppWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }
        }

        Set<Point> movablePos = this.grid.getMovablePositions(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.grid.getFieldHeight(x, y));
            this.grid.updateGridAfterMove(worker, prevX, prevY, x, y);
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not movable.");
            return false;
        }
    }
}
