package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    private Player player;
    private Grid grid;

    @Before
    public void setUp() {
        this.grid = new Grid();
        this.player = new Player("A", this.grid);
    }

    @Test
    public void testInitWorkerPosition() {
        this.player.initWorkerPosition(0, 1, 2);
        assertEquals(1, this.player.getWorker(0).getX());
        assertEquals(2, this.player.getWorker(0).getY());
        assertTrue(this.player.getWorker(0).hasInitPosition());
    }

    @Test
    public void testAllWorkersInited() {
        this.player.initWorkerPosition(0, 1, 1);
        this.player.initWorkerPosition(1, 2, 2);
        assertTrue(this.player.allWorkersInited());
    }
}
