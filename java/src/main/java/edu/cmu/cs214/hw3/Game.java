package edu.cmu.cs214.hw3;

import java.awt.Point;
import java.util.List;
import java.util.Set;

public class Game {
    private static final int WIN_HEIGHT = 3;
    private Player playerA;
    private Player playerB;
    private Grid grid;
    private Player currentPlayer;
    private Player winner;

    /**
     * Creates a new {@link Game} instance.
     *
     * @param playerAId ID of the first player.
     * @param playerBId ID of the second player.
     */
    public Game(String playerAId, String playerBId) {
        this.playerA = new Player(playerAId);
        this.playerB = new Player(playerBId);
        this.grid = new Grid();
        // Assume Player A always starts first
        currentPlayer = playerA;
        winner = null;
    }

    /**
     * Initializes the game. Both players pick starting positions for both
     * their workers on the grid.
     *
     * @param playerId Player's ID.
     * @param workerId Worker's ID
     * @param x X coordinate of worker's starting position.
     * @param y Y coordinate of worker's starting position.
     */
    public void initGame(String playerId, String workerId, int x, int y) {
        if (this.playerA.getPlayerId().equals(playerId)) {
            this.playerA.initWorkerPosition(workerId, x, y);
            grid.updateAfterInit(x, y);
        } else if (this.playerB.getPlayerId().equals(playerId)) {
            this.playerB.initWorkerPosition(workerId, x, y);
            grid.updateAfterInit(x, y);
        } else {
            System.err.println("No player for this ID: " + playerId);
        }
    }

    /**
     * Each player takes turns to play the game.
     *
     * @param workerId Worker's ID.
     * @param x X coordinate of target position.
     * @param y Y coordinate of target position.
     */
    public void playGame(String workerId, int x, int y) {
        while (true) {
            boolean moveSuccess = false;
            do {
                moveSuccess = this.move(currentPlayer.getPlayerId(), workerId, x, y);
            } while (!moveSuccess);

            if (this.checkWin(currentPlayer.getPlayerId())) {
                break;
            }

            boolean buildSuccess = false;
            do {
                buildSuccess = this.build(currentPlayer.getPlayerId(), workerId, x, y);
            } while (!buildSuccess);

            newTurn();
        }
    }

    /**
     * Player moves the selected worker to an adjacent unoccupied field.
     *
     * @param playerId Player's ID.
     * @param workerId Worker's ID.
     * @param x X coordinate of target position.
     * @param y X coordinate of target position.
     * @return {@code true} if worker moves successfully.
     */
    public boolean move(String playerId, String workerId, int x, int y) {
        assert this.currentPlayer.getPlayerId().equals(playerId);

        Worker worker = this.currentPlayer.getWorker(workerId);
        int row = worker.getRowIndex();
        int column = worker.getColumnIndex();
        Set<Point> movable = grid.movablePositions(worker, row, column);
        Point targetPosition = new Point(x, y);

        if (movable.contains(targetPosition)) {
            worker.setPosition(x, y);
            worker.setHeight(grid.getFieldHeight(x, y));
            grid.updateAfterMove(row, column, x, y);
            return true;
        } else {
            System.err.println("Target Filed[" + x + "][" + y + "] is not movable.");
            return false;
        }
    }

    /**
     * Player adds a block or dome to an unoccupied adjacent field of worker's
     * new position.
     *
     * @param playerId Player's ID.
     * @param workerId Worker's ID.
     * @param x X coordinate of target position.
     * @param y Y coordinate of target position.
     * @return {@code true} if worker builds tower successfully.
     */
    public boolean build(String playerId, String workerId, int x, int y) {
        assert this.currentPlayer.getPlayerId().equals(playerId);

        Worker worker = this.currentPlayer.getWorker(workerId);
        int row = worker.getRowIndex();
        int column = worker.getColumnIndex();
        Set<Point> buildable = grid.buildablePositions(row, column);
        Point targetPosition = new Point(x, y);

        if (buildable.contains(targetPosition)) {
            grid.buildTowerLevel(x, y);
            return true;
        } else {
            System.err.println("Target Filed[" + x + "][" + y + "] is not buildable.");
            return false;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Grid getGrid() {
        return grid;
    }

    /**
     * Changes turn to another player.
     */
    public void newTurn() {
        if (currentPlayer == playerA) {
            currentPlayer = playerB;
        } else {
            currentPlayer = playerA;
        }
    }

    /**
     * Checks whether the current player wins the game.
     *
     * @param playerId PLayer's ID.
     * @return {@code true} if player has a worker on top of a level-3 tower.
     */
    public boolean checkWin(String playerId) {
        assert this.currentPlayer.getPlayerId().equals(playerId);

        List<Worker> workers = currentPlayer.getAllWorkers();
        for (Worker worker : workers) {
            if (worker.getHeight() == WIN_HEIGHT) {
                winner = currentPlayer;
                System.out.println("Player " + winner.getPlayerId() + " wins!");
                return true;
            }
        }
        return false;
    }
}
