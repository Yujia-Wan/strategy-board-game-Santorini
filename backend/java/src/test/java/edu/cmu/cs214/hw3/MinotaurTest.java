package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MinotaurTest {
    private Grid grid;
    private Player player;
    private Minotaur card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new Minotaur(grid, player);
    }

    @Test
    public void testMoveTwoWorkers() {
        player.initWorkerPosition(0,2,1);
        player.initWorkerPosition(1,1,2);
        Worker w = player.getWorker(0);
        Player opp = new Player(grid, "B");
        opp.initWorkerPosition(0,3,2);
        opp.initWorkerPosition(1,2,3);
        Worker oppW = opp.getWorker(0);
        card.moveTwoWorkers(w,oppW,2,1,3,2,4,3);
        assertEquals(3, w.getX());
        assertEquals(2, w.getY());
        assertEquals(4, oppW.getX());
        assertEquals(3, oppW.getY());
    }

    @Test
    public void testMove() {
        player.initWorkerPosition(0,2,1);
        player.initWorkerPosition(1,1,2);
        Worker w = player.getWorker(0);
        Player opp = new Player(grid, "B");
        opp.initWorkerPosition(0,3,2);
        opp.initWorkerPosition(1,2,3);
        Worker oppW = opp.getWorker(0);
        assertTrue(card.move(w,3,2));
        assertEquals(4, oppW.getX());
        assertEquals(3, oppW.getY());
        card.move(w,2,1);
        oppW = opp.getWorker(1);
        card.move(w,2,3);
        assertEquals(2, oppW.getX());
        assertEquals(4, oppW.getY());
    }

    @Test
    public void testGetValidPositions() {
        player.initWorkerPosition(0,2,2);
        player.initWorkerPosition(1,1,0);
        Worker w = player.getWorker(0);
        Player opp = new Player(grid, "B");
        opp.initWorkerPosition(0,3,2);
        opp.initWorkerPosition(1,1,3);
        assertEquals(8, card.getValidPositions(w).size());
        w= player.getWorker(1);
        assertEquals(7, card.getValidPositions(w).size());
    }
}
