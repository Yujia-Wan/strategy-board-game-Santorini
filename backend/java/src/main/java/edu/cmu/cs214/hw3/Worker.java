package edu.cmu.cs214.hw3;

public class Worker {
    private final int workerId;
    private final String playerId;
    private int x;
    private int y;
    private int height;

    /**
     * Creates a new {@link Worker} instance.
     *
     * @param workerId The worker ID.
     */
    public Worker(int workerId, String playerId) {
        this.workerId = workerId;
        this.playerId = playerId;
        this.x = -1;
        this.y = -1;
        this.height = 0;
    }

    /**
     * Retrieves worker's ID.
     *
     * @return Worker's ID.
     */
    public int getWorkerId() {
        return this.workerId;
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
     * Retrieves row index of worker's position.
     *
     * @return Row index.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Retrieves column index of worker's position.
     *
     * @return Column index.
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
     * Sets worker's position.
     *
     * @param x Row index of position.
     * @param y Column index of Position.
     */
    public void setPositionAndHeight(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    /**
     * Sets worker's height at current position.
     *
     * @param height Worker's height.
     */
//    public void setHeight(int height) {
//        this.height = height;
//    }

    /**
     * Checks whether the worker has picked starting position.
     *
     * @return {@code true} if the worker has starting position.
     */
    public boolean hasInitPosition() {
        if (this.x < 0 || this.y < 0) {
            return false;
        }
        return true;
    }
}
