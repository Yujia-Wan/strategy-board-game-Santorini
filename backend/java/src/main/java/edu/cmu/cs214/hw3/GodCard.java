package edu.cmu.cs214.hw3;

import java.util.List;

public abstract class GodCard {
    protected static final String MOVE = "move";
    protected static final String BUILD = "build";
    protected static final int WIN_HEIGHT = 3;
    protected static final int WIN = 1;
    protected static final int LOSE = 0;
    protected static final int PLAY = -1;
    protected Grid grid;
    protected Player player;
    protected boolean myTurn;
    protected int state;
    protected String action;
    protected int movedWorkerId;

    public GodCard(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;
        this.myTurn = false;
        this.state = PLAY;
        this.action = MOVE;
        this.movedWorkerId = -1;
    }

    public boolean isMyTurn() {
        return this.myTurn;
    }

    public int getGameState() {
        return this.state;
    }


    public void checkWin() {
        List<Worker> workers = this.player.getAllWorkers();
        for (Worker w: workers) {
            if (w.getHeight() == WIN_HEIGHT) {
                this.state = WIN;
            }
        }
    }

    public void nextAction() {
        if (this.action.equals(MOVE)) {
            this.action = BUILD;
            this.myTurn = true;
        } else if (this.action.equals(BUILD)) {
            this.action = MOVE;
            this.myTurn = false;
        }
    }

    public boolean execute(Worker worker, int x, int y) {
        if (this.action.equals(MOVE)) {
            if (this.player.hasMovablePositions()) {
                System.out.println("Player " + this.player.getPlayerId() + " cannot move any worker.");
                this.state = LOSE;
                return false;
            }

            if (!this.player.move(worker, x, y)) {
                return false;
            }

            this.movedWorkerId = worker.getWorkerId();
        } else if (this.action.equals(BUILD)) {
            if (!(worker.getWorkerId() == this.movedWorkerId)) {
                return false;
            }

            if (!this.player.hasBuildablePositions(worker)) {
                System.out.println("Worker " + this.movedWorkerId + " cannot build any tower level.");
                this.state = LOSE;
                return false;
            }

            if (!this.player.build(worker, x, y)) {
                return false;
            }
        }

        checkWin();
        nextAction();
        return true;
    }
}
