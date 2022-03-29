package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {
    private Game game;

    @Before
    public void setup() {
        game = new Game();
    }

    @Test
    public void testChangePlayer() {
        assertEquals("A", game.getCurrPlayer().getPlayerId());
        game.changePlayer();
        assertEquals("B", game.getCurrPlayer().getPlayerId());
    }

    @Test
    public void testFinishChooseGodCards() {
        Player player = game.getCurrPlayer();
        game.createCardForPlayer(player, 1);
        game.changePlayer();
        player = game.getCurrPlayer();
        game.createCardForPlayer(player, 2);
        assertTrue(game.finishChooseGodCards());
    }

    @Test
    public void testCreateCardForPlayer() {
        Player player = game.getCurrPlayer();
        game.createCardForPlayer(player, 1);
        assertEquals(Integer.valueOf(1), game.getPlayerIdCardIndexMap().get(player.getPlayerId()));
    }

    @Test
    public void testChooseGodCard() {
        Player p = game.getCurrPlayer();
        game.createCardForPlayer(p, 0);
        game.changePlayer();
        p = game.getCurrPlayer();
        game.createCardForPlayer(p, 0);
        assertEquals(Integer.valueOf(0), game.getPlayerIdCardIndexMap().get(p.getPlayerId()));
    }

    @Test
    public void testSelectWorker() {
        game.selectWorker(1);
        assertEquals(1, game.getCurrWorker().getWorkerId());
    }

    @Test
    public void testAllWorkersInited() {
        game.initAllWorkersPositions(0, 0, 0);
        game.initAllWorkersPositions(1, 1, 1);
        assertFalse(game.allWorkersInited());
        game.initAllWorkersPositions(0, 2, 2);
        game.initAllWorkersPositions(1, 3, 3);
        assertTrue(game.allWorkersInited());
    }

    @Test
    public void testInitAllWorkersPositions() {
        game.initAllWorkersPositions(0, 0, 0);
        assertTrue(game.getCurrPlayer().getWorker(0).hasInitPosition());
    }

    @Test
    public void getWinner() {
        Player p =game.getCurrPlayer();
        game.createCardForPlayer(p, 1);
        p.initWorkerPosition(0,0,0);
        p.initWorkerPosition(1,1,1);
        p.getGodCard().setState(1);
        game.changePlayer();
        p = game.getCurrPlayer();
        game.createCardForPlayer(p, 2);
        p.initWorkerPosition(0,2,2);
        p.initWorkerPosition(1,3,3);
        game.changePlayer();
        p = game.getCurrPlayer();
        assertEquals("A", game.getWinner().getPlayerId());
        p.getGodCard().setState(0);
        assertEquals("B", game.getWinner().getPlayerId());
    }

    @Test
    public void testNewTurn() {
        Player p = game.getCurrPlayer();
        game.createCardForPlayer(p, 0);
        p.getGodCard().setMyTurn(false);
        game.newTurn();
        assertEquals("B", game.getCurrPlayer().getPlayerId());
        assertEquals(0, game.getCurrWorker().getWorkerId());
    }
}
