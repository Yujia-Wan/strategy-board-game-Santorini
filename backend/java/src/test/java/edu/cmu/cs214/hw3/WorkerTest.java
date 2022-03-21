package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class WorkerTest {
    private Worker worker;

    @Before
    public void setUp() {
        worker = new Worker("A");
    }

    @Test
    public void testHasNoInitPosition() {
        assertFalse(worker.hasInitPosition());
    }

    @Test
    public void testHasInitPosition() {
        worker.setPositionAndHeight(1,1);
        assertTrue(worker.hasInitPosition());
    }
}
