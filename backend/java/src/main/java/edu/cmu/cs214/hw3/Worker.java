package edu.cmu.cs214.hw3;

public class Worker {
    private final String playerId;
    private final int workerId;
    private int x;
    private int y;
    private int height;

    /**
     * Creates a new {@link Worker} instance.
     *
     * @param playerId Player ID.
     * @param workerId Worker ID.
     */
    public Worker(String playerId, int workerId) {
        this.playerId = playerId;
        this.workerId = workerId;
        this.x = -1;
        this.y = -1;
        this.height = 0;
    }

    /**
     * Retrieves player's ID.
     *
     * @return Player ID.
     */
    public String getPlayerId() {
        return this.playerId;
    }

    /**
     * Retrieves worker's ID.
     *
     * @return Worker ID.
     */
    public int getWorkerId() {
        return this.workerId;
    }

    /**
     * Retrieves x index of worker's position.
     *
     * @return X index.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Retrieves y index of worker's position.
     *
     * @return Y index.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Retrieves worker's height at current position.
     *
     * @return Worker's height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets worker's position and height.
     *
     * @param x X index of position.
     * @param y Y index of Position.
     * @param height Worker's height.
     */
    public void setPositionAndHeight(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    /**
     * Checks whether the worker has picked starting position.
     *
     * @return {@code true} if the worker has starting position.
     */
    public boolean hasInitPosition() {
        return this.x >= 0 && this.y >= 0;
    }
}
