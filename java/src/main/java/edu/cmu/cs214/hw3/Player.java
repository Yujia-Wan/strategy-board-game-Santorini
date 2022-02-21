package edu.cmu.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private String playerId;
    private Worker workerA;
    private Worker workerB;
    private Map<String, Worker> workerMap;

    /**
     * Creates a new {@link Player} instance.
     *
     * @param newPlayerId Player's ID.
     */
    public Player(String newPlayerId) {
        this.playerId = newPlayerId;
        this.workerA = new Worker("WorkerA");
        this.workerB = new Worker("WorkerB");
        this.workerMap = new HashMap<>();
        workerMap.put(workerA.getWorkerId(), workerA);
        workerMap.put(workerB.getWorkerId(), workerB);
    }

    /**
     * Picks starting position for a worker.
     *
     * @param workerId ID of worker chosen to set starting position.
     * @param row Row index of starting position.
     * @param column Column index of starting position.
     */
    public void initWorkerPosition(String workerId, int row, int column) {
        if (!this.getWorker(workerId).hasInitPosition()) {
            Worker worker = this.getWorker(workerId);
            worker.setPosition(row, column);
        }
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
    public Worker getWorker(String workerId) {
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
}
