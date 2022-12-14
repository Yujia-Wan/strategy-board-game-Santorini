package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.List;
import java.util.Set;

/**
 * Pan: You also win if your Worker moves down two or more levels.
 */
public class Pan extends GodCard {
    private static final int ALSO_WIN_HEIGHT = 2;
    private int moveDownHeight;

    public Pan(Grid grid, Player player) {
        super(grid, player);
        this.moveDownHeight = -1;
    }

    @Override
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.getPlayer().getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        int prevHeight = moveWorker.getHeight();
        Set<Point> movablePos = this.getGrid().getMovablePositions(prevX, prevY);
        Point target = new Point(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.getGrid().getFieldHeight(x, y));
            this.getGrid().updateGridAfterMove(worker, prevX, prevY);
            this.moveDownHeight = prevHeight - moveWorker.getHeight();
            return true;
        } else {
            System.err.println("Target field[" + x + "][" + y + "] is not movable.");
            return false;
        }
    }

    @Override
    public void checkWin() {
        List<Worker> workers = this.getPlayer().getAllWorkers();
        for (Worker w: workers) {
            if (w.getHeight() == WIN_HEIGHT || this.moveDownHeight >= ALSO_WIN_HEIGHT) {
                this.setState(WIN);
                this.moveDownHeight = -1;
            }
        }
    }
}
