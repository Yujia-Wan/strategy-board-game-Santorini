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
     */
    public Player(String playerId, Grid grid) {
        this.playerId = playerId;
        this.worker0 = new Worker(WORKER0, this.playerId);
        this.worker1 = new Worker(WORKER1, this.playerId);
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
     * Picks starting position for a worker.
     *
     * @param workerId ID of worker chosen to set starting position.
     * @param x Row index of starting position.
     * @param y Column index of starting position.
     */
    public boolean initWorkerPosition(int workerId, int x, int y) {
        if (!this.getWorker(workerId).hasInitPosition()) {
            Worker worker = this.getWorker(workerId);
            worker.setPositionAndHeight(x, y, 0);
            this.grid.setWorkerPosition(worker, x, y);
            return true;
        }
        return false;
    }

    public boolean allWorkersInited() {
        for (Worker worker: this.getAllWorkers()) {
            if (!worker.hasInitPosition()) {
                return false;
            }
        }
        return true;
    }

    public Set<Point> getAllWorkersPosition() {
        Set<Point> positions = new HashSet<>();
        positions.add(new Point(this.worker0.getX(), this.worker0.getY()));
        positions.add(new Point(this.worker1.getX(), this.worker1.getY()));
        return positions;
    }


    public boolean hasMovablePositions() {
        Set<Point> allMovable = new HashSet<>();
        for (Worker w: this.getAllWorkers()) {
            allMovable.addAll(this.grid.movablePositions(w.getX(), w.getY()));
        }
        if (allMovable.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Player moves the selected worker to an adjacent unoccupied field.
     *
     * @param worker
     * @param x
     * @param y
     * @return {@code true} if worker moves successfully.
     */
    public boolean move(Worker worker, int x, int y) {
        Worker moveWorker = this.getWorker(worker.getWorkerId());
        int prevX = moveWorker.getX();
        int prevY = moveWorker.getY();
        Set<Point> movablePos = this.grid.movablePositions(prevX, prevY);
        Point target = new Point(x, y);
        if (movablePos.contains(target)) {
            moveWorker.setPositionAndHeight(x, y, this.grid.getFieldHeight(x, y));
            this.grid.updateGridAfterMove(worker, prevX, prevY, x, y);
            return true;
        }
        return false;
    }


    public boolean hasBuildablePositions(Worker worker) {
        return !this.grid.buildablePositions(worker.getX(), worker.getY()).isEmpty();
    }

    /**
     * Player adds a block or dome to an unoccupied adjacent field of worker's
     * new position.
     *
     * @param worker
     * @param x
     * @param y
     * @return {@code true} if worker builds tower successfully.
     */
    public boolean build(Worker worker, int x, int y) {
        Worker buildWorker = this.getWorker(worker.getWorkerId());
        int workerX = buildWorker.getX();
        int workerY = buildWorker.getY();
        Set<Point> buildablePos = this.grid.buildablePositions(workerX, workerY);
        Point target = new Point(x, y);
        if (buildablePos.contains(target)) {
            this.grid.buildTowerLevel(x, y);
            return true;
        }

        System.err.println("Target field[" + x + "][" + y + "] is not buildable.");
        return false;
    }


    public void addGodCard(GodCard godCard) {
        this.godCard = godCard;
    }

    public GodCard getGodCard() {
        return this.godCard;
    }
}
