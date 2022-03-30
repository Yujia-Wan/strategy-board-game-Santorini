package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HephaestusTest {
    private Grid grid;
    private Player player;
    private Hephaestus card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new Hephaestus(grid, player);
    }

    @Test
    public void testBuildAddlBlock() {
        player.initWorkerPosition(0,1,1);
        Worker w = player.getWorker(0);
        card.move(w,2,1);
        card.nextAction();
        card.build(w,2,2);
        card.nextAction();
        assertEquals("build one additional block", card.getAction());
        assertTrue(card.buildAddlBlock(2,2,2,2));
        card.nextAction();
        assertEquals("move", card.getAction());
    }

    @Test
    public void testExecute() {
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,1,3);
        Worker w = player.getWorker(0);
        card.execute(w,2,1);
        card.execute(w,2,2);
        assertEquals("build one additional block", card.getAction());
        assertTrue(card.execute(w,2,2));
        assertEquals("move", card.getAction());
        assertEquals(2, grid.getFieldHeight(2,2));
        card.execute(w,3,1);
        card.execute(w,3,2);
        grid.buildTowerLevel(3,2);
        grid.buildTowerLevel(3,2);
        grid.buildTowerLevel(3,2);
        assertFalse(card.execute(w,2,2));
    }

    @Test
    public void testGetValidPositions() {
        player.initWorkerPosition(0,1,1);
        Worker w = player.getWorker(0);
        card.setAction("build one additional block");
        assertEquals(1, card.getValidPositions(w).size());
    }
}
