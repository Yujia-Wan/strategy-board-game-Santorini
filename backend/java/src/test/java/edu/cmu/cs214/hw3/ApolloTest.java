package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApolloTest {
    private Grid grid;
    private Player player;
    private Apollo card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new Apollo(grid, player);
    }

    @Test
    public void testMove() {
        player.initWorkerPosition(0,2,1);
        player.initWorkerPosition(1,1,2);
        Worker w = player.getWorker(0);
        Player opp = new Player(grid, "B");
        opp.initWorkerPosition(0,3,2);
        opp.initWorkerPosition(1,1,4);
        Worker oppW = opp.getWorker(1);
        assertTrue(card.move(w,1,4));
        assertEquals(1, w.getX());
        assertEquals(4, w.getY());
        assertEquals(2, oppW.getX());
        assertEquals(1, oppW.getY());
    }

    @Test
    public void testGetValidPositions() {
        player.initWorkerPosition(0,2,1);
        player.initWorkerPosition(1,1,2);
        Worker w = player.getWorker(0);
        Player opp = new Player(grid, "B");
        opp.initWorkerPosition(0,3,2);
        opp.initWorkerPosition(1,1,4);
        assertEquals(8, card.getValidPositions(w).size());
        w = player.getWorker(1);
        assertEquals(9, card.getValidPositions(w).size());
    }
}
