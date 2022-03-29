package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {
    private Game game;

    @Before
    public void setup() {
        game = new Game();
    }

    @Test
    public void testNonGodCardGame() {
        game.chooseGodCard(0);
        game.chooseGodCard(0);
        assertTrue(game.finishChooseGodCards());
        game.play(1,1);
        game.play(3,1);
        game.play(1,3);
        game.play(3,3);
        assertTrue(game.allWorkersInited());
        game.selectWorker(1);
        game.play(2,2);
        game.play(2,1);
        assertEquals("B", game.getCurrPlayer().getPlayerId());
        game.play(1,2);
        game.play(2,3);
        game.play(2,1);
        game.play(3,1);
        game.play(2,3);
        game.play(3,2);
        game.selectWorker(1);
        game.play(3,2);
        game.play(3,1);
        game.play(2,2);
        game.play(1,2);
        game.selectWorker(1);
        game.play(4,2);
        game.play(3,2);
        game.play(2,3);
        game.play(3,2);
        game.play(3,1);
        game.play(3,0);
        game.play(1,2);
        game.play(2,3);
        assertEquals(null, game.getWinner());
        game.play(3,2);
        assertEquals("A", game.getWinner().getPlayerId());
    }

    @Test
    public void testGodCardGame() {
        game.chooseGodCard(1);
        game.chooseGodCard(2);
        assertTrue(game.finishChooseGodCards());
        game.play(1,1);
        game.play(3,1);
        game.play(1,3);
        game.play(3,3);
        assertTrue(game.allWorkersInited());
        game.play(2,2);
        game.play(2,1);
        assertEquals("A", game.getCurrPlayer().getPlayerId());
        game.play(1,1);
        assertEquals("B", game.getCurrPlayer().getPlayerId());
        game.selectWorker(1);
        game.play(2,2);
        game.play(2,1);
        assertEquals(1, game.getCurrPlayer().getWorker(0).getX());
        assertEquals(1, game.getCurrPlayer().getWorker(0).getY());
        game.play(2,1);
        game.play(1,1);
        game.play(2,1);
        assertEquals(2, game.getGrid().getFieldHeight(2,1));
        game.play(1,2);
        game.play(1,1);
        assertEquals(null, game.getWinner());
        game.play(1,1);
        assertEquals("A", game.getWinner().getPlayerId());
    }
}
