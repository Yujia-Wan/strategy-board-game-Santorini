package edu.cmu.cs214.hw3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class WorkerTest {
    private Worker worker;

    @Before
    public void setUp() {
        this.worker = new Worker("A",0);
    }

    @Test
    public void testHasNoInitPosition() {
        assertFalse(this.worker.hasInitPosition());
    }

    @Test
    public void testHasInitPosition() {
        this.worker.setPositionAndHeight(1,1, 0);
        assertTrue(this.worker.hasInitPosition());
    }
}
