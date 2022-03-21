package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player("A");
    }

    @Test
    public void testInitWorkerPosition() {
        player.initWorkerPosition("WorkerA", 1, 2);
        assertEquals(1, player.getWorker("WorkerA").getX());
        assertEquals(2, player.getWorker("WorkerA").getY());
        assertTrue(player.getWorker("WorkerA").hasInitPosition());
    }
}
