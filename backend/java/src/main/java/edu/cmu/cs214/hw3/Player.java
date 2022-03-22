package edu.cmu.cs214.hw3;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Player {
    private static final int WORKER0 = 0;
    private static final int WORKER1 = 1;
    private final String playerId;
    private Worker worker0;
    private Worker worker1;
    private Map<Integer, Worker> workerMap;
    private Grid grid;
    private GodCard godCard;

    /**
     * Creates a new {@link Player} instance.
     *
     * @param playerId Player's ID.
     * @param grid Game grid.
     */
    public Player(String playerId, Grid grid) {
        this.playerId = playerId;
        this.worker0 = new Worker(WORKER0);
        this.worker1 = new Worker(WORKER1);
        this.workerMap = new HashMap<>();
        workerMap.put(WORKER0, worker0);
        workerMap.put(WORKER1, worker1);
        this.grid = grid;
        this.godCard = null;
    }

    /**
     * Retrieves player's ID.
     *
     * @return Player's ID.
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Retrieves Player's worker.
     *
     * @param workerId Worker's ID to be retrieved.
     * @return The associated {@link edu.cmu.cs214.hw3.Worker}.
     */
    public Worker getWorker(int workerId) {
        if (workerMap.containsKey(workerId)) {
            return workerMap.get(workerId);
        } else {
            System.err.println("Player " + playerId + " has no worker for this Worker ID.");
            return null;
        }
    }

    /**
     * Retrieves player's all workers.
     *
     * @return A list of workers.
     */
    public List<Worker> getAllWorkers() {
        return new ArrayList<>(workerMap.values());
    }

    /**
     * Checks all workers' initial positions' state.
     *
     * @return {@code true} if all workers have initial positions.
     */
    public boolean allWorkersInited() {
        for (Worker worker: this.getAllWorkers()) {
            if (!worker.hasInitPosition()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Picks starting position for a worker.
     *
     * @param workerId ID of worker chosen to set starting position.
     * @param x Row index of starting position.
     * @param y Column index of starting position.
     */
    public void initWorkerPosition(int workerId, int x, int y) {
        Worker worker = this.getWorker(workerId);
        if (!worker.hasInitPosition()) {
            worker.setPositionAndHeight(x, y, 0);
            this.grid.updateAfterInitWorkerPos(worker, x, y);
        } else {
            System.err.println("This worker already has initial position!");
        }
    }

    public Set<Point> getAllWorkersPosition() {
        Set<Point> positions = new HashSet<>();
        positions.add(new Point(this.worker0.getX(), this.worker0.getY()));
        positions.add(new Point(this.worker1.getX(), this.worker1.getY()));
        return positions;
    }

    /**
     * Retrieves all workers' movable positions.
     *
     * @return {@code true} if player has movable position.
     */
    public boolean hasMovablePositions() {
        Set<Point> allMovable = new HashSet<>();
        for (Worker w: this.getAllWorkers()) {
            allMovable.addAll(this.grid.getMovablePositions(w.getX(), w.getY()));
        }
        return !allMovable.isEmpty();
    }

    /**
     * Retrieves all workers' buildable positions.
     *
     * @return {@code true} if player has buildable position.
     */
    public boolean hasBuildablePositions() {
        Set<Point> allBuildable = new HashSet<>();
        for (Worker w: this.getAllWorkers()) {
            allBuildable.addAll(this.grid.getBuildablePositions(w.getX(), w.getY()));
        }
        return !allBuildable.isEmpty();
    }

    public void addGodCard(GodCard godCard) {
        this.godCard = godCard;
    }

    public GodCard getGodCard() {
        return this.godCard;
    }
}
