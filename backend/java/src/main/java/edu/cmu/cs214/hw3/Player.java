package edu.cmu.cs214.hw3;

import java.awt.Point;
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
    private Grid grid;
    private Map<Integer, Worker> workerMap;
    private GodCard godCard;

    /**
     * Creates a new {@link Player} instance.
     *
     * @param grid Game grid.
     * @param playerId Player's ID.
     */
    public Player(Grid grid, String playerId) {
        this.playerId = playerId;
        this.grid = grid;
        Worker worker0 = new Worker(this.playerId, WORKER0);
        Worker worker1 = new Worker(this.playerId, WORKER1);
        this.workerMap = new HashMap<>();
        this.workerMap.put(WORKER0, worker0);
        this.workerMap.put(WORKER1, worker1);
        this.godCard = null;
    }

    /**
     * Retrieves player's ID.
     *
     * @return Player's ID.
     */
    public String getPlayerId() {
        return this.playerId;
    }

    /**
     * Retrieves Player's worker.
     *
     * @param workerId Worker's ID to be retrieved.
     * @return The associated {@link edu.cmu.cs214.hw3.Worker}.
     */
    public Worker getWorker(int workerId) {
        if (this.workerMap.containsKey(workerId)) {
            return this.workerMap.get(workerId);
        } else {
            System.err.println("Player " + this.playerId + " doesn't have Worker " + workerId + ".");
            return null;
        }
    }

    /**
     * Retrieves player's all workers.
     *
     * @return A list of workers.
     */
    public List<Worker> getAllWorkers() {
        return new ArrayList<>(this.workerMap.values());
    }

    /**
     * Checks all workers' initial positions' state.
     *
     * @return {@code true} if all workers have initial positions.
     */
    public boolean allWorkersInited() {
        for (Worker worker: this.workerMap.values()) {
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
            this.grid.updateAfterInitWorkerPos(worker);
        } else {
            System.err.println("Worker " + workerId + " already has starting position!");
        }
    }

    /**
     * Retrieves all workers' positions.
     *
     * @return A set of coordinates.
     */
    public Set<Point> getAllWorkersPositions() {
        Set<Point> positions = new HashSet<>();
        for (Worker worker: this.workerMap.values()) {
            positions.add(new Point(worker.getX(), worker.getY()));
        }
        return positions;
    }

    /**
     * Retrieves all workers' movable positions.
     *
     * @return {@code true} if player has movable position.
     */
    public boolean hasMovablePositions() {
        Set<Point> allMovable = new HashSet<>();
        for (Worker w: this.workerMap.values()) {
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
        for (Worker w: this.workerMap.values()) {
            allBuildable.addAll(this.grid.getBuildablePositions(w.getX(), w.getY()));
        }
        return !allBuildable.isEmpty();
    }

    /**
     * Add the god card for player.
     *
     * @param godCard God card player chooses.
     */
    public void setGodCard(GodCard godCard) {
        this.godCard = godCard;
    }

    /**
     * Retrieves the god card of player.
     *
     * @return Player's god card.
     */
    public GodCard getGodCard() {
        return this.godCard;
    }
}
