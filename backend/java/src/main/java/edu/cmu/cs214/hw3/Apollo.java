package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * Apollo: Your Worker may move into an opponent Worker's space by forcing their Worker
 * to the space you just vacated.
 */
public class Apollo extends GodCard {

    public Apollo(Grid grid, Player player) {
        super(grid, player);
    }

    @Override
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.getPlayer().getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Point target = new Point(x, y);

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
            moveWorker.setPositionAndHeight(x, y, this.getGrid().getFieldHeight(x, y));
            oppWorker.setPositionAndHeight(prevX, prevY,this.getGrid().getFieldHeight(prevX, prevY));
            this.getGrid().updateGridAfterSwapTwoWorkers(moveWorker, oppWorker);
            return true;
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

    @Override
    public Set<Point> getValidPositions(Worker worker) {
        if (this.getAction().equals(MOVE)) {
            Set<Point> allWorkersPos = this.getGrid().getAllWorkersPositions();
            Set<Point> myWorkersPos = this.getPlayer().getAllWorkersPositions();
            Set<Point> oppWorkersPos = new HashSet<>();
            for (Point p: allWorkersPos) {
                if (!myWorkersPos.contains(p)) {
                    oppWorkersPos.add(p);
                }
            }
            Set<Point> moveValidPos = this.getGrid().getMovablePositions(worker.getX(), worker.getY());
            moveValidPos.addAll(oppWorkersPos);
            return moveValidPos;
        } else if (this.getAction().equals((BUILD))) {
            return this.getGrid().getBuildablePositions(worker.getX(), worker.getY());
        }
        return null;
    }
}
