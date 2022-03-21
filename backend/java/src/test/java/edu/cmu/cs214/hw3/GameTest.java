package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {
    private Game game;
    private Grid grid;

    @Before
    public void setup() {
        game = new Game("A", "B");
        grid = game.getGrid();
        game.initGame("A", "WorkerA", 1, 1);
        game.initGame("A", "WorkerB", 3, 2);
        game.initGame("B", "WorkerA", 1, 3);
        game.initGame("B", "WorkerB", 4, 3);
    }

    @Test
    public void testGameInit() {
        assertTrue(grid.isOccupied(1, 1));
        assertTrue(grid.isOccupied(3, 2));
        assertTrue(grid.isOccupied(1, 3));
        assertTrue(grid.isOccupied(4, 3));
    }

    @Test
    public void testPlayGame() {
        game.move("A", "WorkerB", 3, 1);
        game.build("A", "WorkerB", 2, 1);
        game.newTurn();
        game.move("B", "WorkerA", 1, 2);
        game.build("B", "WorkerA", 2, 2);
        game.newTurn();
        game.move("A", "WorkerB", 2, 1);
        game.build("A", "WorkerB", 2, 2);
        assertEquals(2, grid.getFieldHeight(2, 2));
        game.newTurn();
        game.move("B", "WorkerA", 1, 3);
        game.build("B", "WorkerA", 2, 3);
        game.newTurn();
        game.move("A", "WorkerB", 2, 2);
        game.build("A", "WorkerB", 2, 3);
        assertEquals(2, grid.getFieldHeight(2, 3));
        game.newTurn();
        game.move("B", "WorkerA", 1, 2);
        game.build("B", "WorkerA", 2, 1);
        game.newTurn();
        game.move("A", "WorkerB", 2, 3);
        game.build("A", "WorkerB", 2, 2);
        game.newTurn();
        game.move("B", "WorkerA", 1, 3);
        game.build("B", "WorkerA", 1, 2);
        game.newTurn();
        assertFalse(game.getWinner("A"));
        game.move("A", "WorkerB", 2, 2);
        assertTrue(game.getWinner("A"));
    }

    @Test
    public void testMove() {
        assertFalse(game.move("A", "WorkerB", 4,3));
        assertFalse(grid.isOccupied(3, 3));
        assertTrue(game.move("A", "WorkerB", 3,3));
        assertTrue(grid.isOccupied(4, 3));
        assertTrue(grid.isOccupied(3, 3));
        assertFalse(grid.isOccupied(3, 2));
    }

    @Test
    public void testBuild() {
        grid.buildTowerLevel(4,2);
        grid.buildTowerLevel(4,2);
        grid.buildTowerLevel(4,2);
        grid.buildTowerLevel(4,2);
        assertFalse(game.build("A", "WorkerB", 4, 2));
        assertTrue(game.build("A", "WorkerB", 2, 2));
        assertEquals(1, grid.getFieldHeight(2, 2));
        game.build("A", "WorkerB", 2, 2);
        game.build("A", "WorkerB", 2, 2);
        assertEquals(3, grid.getFieldHeight(2, 2));
    }
}
