package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PanTest {
    private Grid grid;
    private Player player;
    private Pan card;

    @Before
    public void setUp() {
        grid = new Grid();
        player = new Player(grid, "A");
        card = new Pan(grid, player);
    }

    @Test
    public void testCheckWin() {
        player.initWorkerPosition(0,2,2);
        Worker w = player.getWorker(0);
        card.move(w,1,1);
        card.build(w,1,2);
        card.move(w,1,2);
        card.build(w,2,2);
        grid.buildTowerLevel(2,2);
        card.move(w,2,2);
        card.build(w,1,2);
        card.move(w,2,3);
        card.checkWin();
        assertEquals(1, card.getState());
    }
}
