package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArtemisTest {
    private Grid grid;
    private Player player;
    private Artemis card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new Artemis(grid, player);
    }

    @Test
    public void testSecondMove() {
        player.initWorkerPosition(0,1,1);
        Worker w = player.getWorker(0);
        card.move(w,1,2);
        card.nextAction();
        assertEquals("second move", card.getAction());
        assertFalse(card.secondMove(w,1,1,1,1));
        assertTrue(card.secondMove(w,2,2,1,1));
    }

    @Test
    public void testExecute() {
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,2,1);
        Worker w = player.getWorker(0);
        card.execute(w,1,2);
        assertEquals("second move", card.getAction());
        assertFalse(card.execute(w,1,1));
        assertNull(grid.getFieldWorker(1,1));
        assertTrue(card.execute(w,2,2));
        assertEquals("build", card.getAction());
        card.execute(w,3,2);
        assertEquals("move", card.getAction());
    }

    @Test
    public void testGetValidPositions() {
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,2,1);
        Worker w = player.getWorker(0);
        card.execute(w,1,2);
        assertEquals(7, card.getValidPositions(w).size());
    }
}
