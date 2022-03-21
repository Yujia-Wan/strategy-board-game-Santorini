package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GridTest {
    private Grid grid;
    private Worker worker;

    @Before
    public void setUp() {
        grid = new Grid();
        worker = new Worker("A");
    }

    @Test
    public void testUpdateAfterInit() {
        assertFalse(grid.isOccupied(3, 3));
        worker.setPositionAndHeight(3, 3);
        grid.updateAfterInit(3, 3);
        assertTrue(grid.isOccupied(3, 3));
    }

    @Test
    public void testMovablePositions() {
        assertEquals(8, grid.movablePositions(worker, 3, 3).size());

        grid.buildTowerLevel(2,2);
        grid.buildTowerLevel(2,2);

        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);

        grid.buildTowerLevel(4,3);
        grid.buildTowerLevel(4,3);
        grid.buildTowerLevel(4,3);
        grid.buildTowerLevel(4,3);

        grid.buildTowerLevel(4,4);
        grid.buildTowerLevel(4,4);
        grid.buildTowerLevel(4,4);

        assertEquals(4, grid.movablePositions(worker, 3, 3).size());

        worker.setHeight(2);
        assertEquals(6, grid.movablePositions(worker, 3, 3).size());
    }

    @Test
    public void testUpdateAfterMove() {
        grid.updateGridAfterMove(0, 1, 1, 2);
        assertFalse(grid.isOccupied(0, 1));
        assertTrue(grid.isOccupied(1, 2));
    }

    @Test
    public void testBuildablePositions() {
        assertEquals(8, grid.buildablePositions(3, 3).size());

        grid.buildTowerLevel(2,2);
        grid.buildTowerLevel(2,2);

        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);

        grid.buildTowerLevel(4,3);
        grid.buildTowerLevel(4,3);
        grid.buildTowerLevel(4,3);
        grid.buildTowerLevel(4,3);

        grid.buildTowerLevel(4,4);
        grid.buildTowerLevel(4,4);
        grid.buildTowerLevel(4,4);

        assertEquals(6, grid.buildablePositions(3, 3).size());

        worker.setPositionAndHeight(3, 2);
        grid.updateGridAfterMove(3,1,3, 2);
        assertEquals(5, grid.buildablePositions(3, 3).size());
    }

    @Test
    public void testBuildTowerLevel() {
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);
        grid.buildTowerLevel(2,3);

        boolean assertError = false;
        try {
            grid.buildTowerLevel(2, 3);
        } catch (AssertionError e) {
            assertError = true;
        }
        assertTrue(assertError);

        grid.buildTowerLevel(3,3);
        assertEquals(1, grid.getFieldHeight(3, 3));

        grid.buildTowerLevel(3,3);
        grid.buildTowerLevel(3,3);
        assertEquals(3, grid.getFieldHeight(3, 3));
    }
}
