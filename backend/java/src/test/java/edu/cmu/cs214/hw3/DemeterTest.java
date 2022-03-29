package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DemeterTest {
    private Grid grid;
    private Player player;
    private Demeter card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new Demeter(grid, player);
    }

    @Test
    public void testSecondBuild() {
        player.initWorkerPosition(0,1,1);
        Worker w = player.getWorker(0);
        card.move(w,2,2);
        card.build(w,2,1);
        assertFalse(card.secondBuild(w,2,1,2,1));
        assertTrue(card.secondBuild(w,2,3,2,1));
    }

    @Test
    public void testNextAction() {
        card.setAction("build");
        card.nextAction();
        assertEquals("second build", card.getAction());
        assertTrue(card.getMyTurn());
        card.nextAction();
        assertEquals("move", card.getAction());
        assertFalse(card.getMyTurn());
    }

    @Test
    public void testExecute() {
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,2,2);
        Worker w = player.getWorker(0);
        card.execute(w,2,1);
        card.execute(w,1,1);
        assertEquals("second build", card.getAction());
        assertFalse(card.execute(w,1,1));
        assertEquals("second build", card.getAction());
        card.execute(w,2,0);
        assertEquals(1, grid.getFieldHeight(2,0));
        assertEquals("move", card.getAction());
        card.execute(w,3,1);
        card.execute(w,3,2);
        assertEquals("second build", card.getAction());
        card.execute(w,3,1);
        assertEquals(0, grid.getFieldHeight(3,1));
        assertEquals("move", card.getAction());
    }

    @Test
    public void testGetValidPositions() {
        player.initWorkerPosition(0,1,1);
        Worker w = player.getWorker(0);
        card.setAction("second build");
        assertEquals(9, card.getValidPositions(w).size());
    }
}
