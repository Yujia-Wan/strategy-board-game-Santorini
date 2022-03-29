package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonGodCardTest {
    private Grid grid;
    private Player player;
    private NonGodCard card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new NonGodCard(grid, player);
    }

    @Test
    public void testMove() {
        player.initWorkerPosition(0,1,1);
        Worker w = player.getWorker(0);
        assertFalse(card.move(w,3,1));
        assertTrue(card.move(w,2,2));
        card.move(w,1,1);
        player.initWorkerPosition(1,2,2);
        assertFalse(card.move(w,2,2));
        card.move(player.getWorker(1),3,3);
        assertTrue(card.move(w,2,2));
        card.move(w,1,1);
        grid.buildTowerLevel(2,2);
        grid.buildTowerLevel(2,2);
        assertFalse(card.move(w,2,2));
    }

    @Test
    public void testBuild() {
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,2,2);
        Worker w = player.getWorker(0);
        assertFalse(card.build(w,2,2));
        grid.buildTowerLevel(2,1);
        grid.buildTowerLevel(2,1);
        card.build(w,2,1);
        assertEquals(3, grid.getFieldHeight(2,1));
        card.build(w,2,1);
        assertFalse(card.build(w,2,1));
    }

    @Test
    public void testCheckWin() {
        assertEquals(-1, card.getState());
        player.getWorker(0).setPositionAndHeight(1,1,1);
        player.getWorker(1).setPositionAndHeight(2,2,3);
        card.checkWin();
        assertEquals(1, card.getState());
    }

    @Test
    public void testNextAction() {
        card.setAction("move");
        card.nextAction();
        assertEquals("build", card.getAction());
        assertTrue(card.getMyTurn());
        card.nextAction();
        assertEquals("move", card.getAction());
        assertFalse(card.getMyTurn());
    }

    @Test
    public void testExecute() {
        Worker w = player.getWorker(0);
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,4,4);
        assertTrue(card.execute(w,2,2));
        assertEquals("build", card.getAction());
        assertTrue(card.execute(w,2,3));
        assertEquals("move", card.getAction());
    }

    @Test
    public void testGetValidPositions() {
        Worker w = player.getWorker(0);
        player.initWorkerPosition(0,1,1);
        player.initWorkerPosition(1,2,2);
        grid.buildTowerLevel(2,0);
        grid.buildTowerLevel(2,0);
        assertEquals(6, card.getValidPositions(w).size());
        card.execute(w,2,1);
        assertEquals(7, card.getValidPositions(w).size());
    }
}
