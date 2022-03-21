package edu.cmu.cs214.hw3;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Minotaur extends GodCard {

    // Minotaur: Your worker may move into an opponent Worker's space, if their Worker can
    // be forced one space straight backwards to an unoccupied space at any level.
    public Minotaur(Grid grid, Player player) {
        super(grid, player);
    }

    private void moveTwoWorkers(Worker myWorker, Worker oppWorker, int prevX, int prevY,
                               int myX, int myY, int oppX, int oppY) {
        oppWorker.setPositionAndHeight(oppX, oppY, this.grid.getFieldHeight(oppX, oppY));
        this.grid.updateGridAfterMove(oppWorker, myX, myY, oppX, oppY);
        myWorker.setPositionAndHeight(myX, myY, this.grid.getFieldHeight(myX, myY));
        this.grid.updateGridAfterMove(myWorker, prevX, prevY, myX, myY);
    }

    @Override
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.player.getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Point target = new Point(x, y);

        // first check if target is an opponent worker's space, if it is and valid, change two workers'
        // position; if not, do regular move
        // get opponent worker's positions
        Set<Point> allWorkersPos = this.grid.getAllWorkersPosition();
        Set<Point> myWorkersPos = this.player.getAllWorkersPosition();
        Set<Point> opponentWorkersPos = new HashSet<>();
        for (Point p: allWorkersPos) {
            if (!myWorkersPos.contains(p)) {
                opponentWorkersPos.add(p);
            }
        }
        if (opponentWorkersPos.contains(target)) {
            Worker opponentWorker = this.grid.getFieldWorker(x, y);
            if (prevX == x && prevY != y) {
                int oppTargetX = x;
                int oppTargetY = y + prevY < y? 1: -1;
                if (0 <= oppTargetY && oppTargetY < COLUMN && !this.grid.isOccupied(oppTargetX, oppTargetY)) {
                    // can be forced one space straight backwards
                    moveTwoWorkers(worker, opponentWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }

            if (prevY == y && prevX != x) {
                int oppTargetX = x + prevX < x? 1: -1;
                int oppTargetY = y;
                if (0 <= oppTargetX && oppTargetX < ROW && !this.grid.isOccupied(oppTargetX, oppTargetY)) {
                    moveTwoWorkers(worker, opponentWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }

            if (Math.abs(prevX - x) == Math.abs(prevY - y)) {
                int oppTargetX = x + prevX < x? 1: -1;
                int oppTargetY = y + prevY < y? 1: -1;
                if (0 <= oppTargetX && oppTargetX < ROW && 0 <= oppTargetY && oppTargetY < COLUMN
                        && !this.grid.isOccupied(oppTargetX, oppTargetY)) {
                    moveTwoWorkers(worker, opponentWorker, prevX, prevY, x, y, oppTargetX, oppTargetY);
                    return true;
                }
            }
        }

        Set<Point> movablePos = this.grid.movablePositions(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.grid.getFieldHeight(x, y));
            this.grid.updateGridAfterMove(worker, prevX, prevY, x, y);
            return true;
        }
        return false;
    }
}
