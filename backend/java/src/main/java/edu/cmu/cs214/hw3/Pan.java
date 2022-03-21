package edu.cmu.cs214.hw3;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class Pan extends GodCard {
    private static final int ALSO_WIN_HEIGHT = 2;
    private int moveDownHeight;

    // Pan: You also win if your Worker moves down two or more levels.
    public Pan(Grid grid, Player player) {
        super(grid, player);
        this.moveDownHeight = -1;
    }

    @Override
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.player.getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        int prevHeight = moveWorker.getHeight();
        Set<Point> movablePos = this.grid.movablePositions(prevX, prevY);
        Point target = new Point(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.grid.getFieldHeight(x, y));
            this.grid.updateGridAfterMove(worker, prevX, prevY, x, y);
            this.moveDownHeight = prevHeight - moveWorker.getHeight();
            return true;
        }
        return false;
    }

    @Override
    public void checkWin() {
        List<Worker> workers = this.player.getAllWorkers();
        for (Worker w: workers) {
            if (w.getHeight() == WIN_HEIGHT || this.moveDownHeight >= ALSO_WIN_HEIGHT) {
                this.state = WIN;
            }
        }
    }
}
