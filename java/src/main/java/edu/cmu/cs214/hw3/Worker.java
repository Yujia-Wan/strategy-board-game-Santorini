package edu.cmu.cs214.hw3;

public class Worker {
    private static final int ROW = 5;
    private static final int COLUMN = 5;
    private static final int WIN_HEIGHT = 5;
    private String workerId;
    private int row;
    private int column;
    private int height;

    /**
     * Creates a new {@link Worker} instance.
     *
     * @param newWorkerId The worker ID.
     */
    public Worker(String newWorkerId) {
        this.workerId = newWorkerId;
        this.row = -1;
        this.column = -1;
        this.height = 0;
    }

    /**
     * Retrieves worker's ID.
     *
     * @return Worker's ID.
     */
    public String getWorkerId() {
        return workerId;
    }

    /**
     * Retrieves row index of worker's position.
     *
     * @return Row index.
     */
    public int getRowIndex() {
        return this.row;
    }

    /**
     * Retrieves column index of worker's position.
     *
     * @return Column index.
     */
    public int getColumnIndex() {
        return this.column;
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
     * @param row Row index of position.
     * @param column Column index of Position.
     */
    public void setPosition(int row, int column) {
        if (row < 0 || row >= ROW || column < 0 || column >= COLUMN) {
            System.err.println("Invalid field position.");
            return;
        }
        this.row = row;
        this.column = column;
    }

    /**
     * Sets worker's height at current position.
     *
     * @param height Worker's height.
     */
    public void setHeight(int height) {
        if (height < 0 || height > WIN_HEIGHT) {
            System.err.println("Invalid worker height.");
            return;
        }
        this.height = height;
    }

    /**
     * Checks whether the worker has picked starting position.
     *
     * @return {@code true} if the worker has starting position.
     */
    public boolean hasInitPosition() {
        if (this.getRowIndex() < 0 || this.getColumnIndex() < 0) {
            return false;
        }
        return true;
    }
}
