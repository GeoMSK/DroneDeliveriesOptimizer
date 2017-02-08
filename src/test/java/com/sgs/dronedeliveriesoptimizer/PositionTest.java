package com.sgs.dronedeliveriesoptimizer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gmak
 */
public class PositionTest {
    
    public PositionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    

    /**
     * Test of getDistance method, of class Position.
     */
    @Test
    public void testGetDistance() {
        Position p1 = new Position(1, 3);
        Position p2 = new Position(5, 1);
        
        assertEquals(5, p1.getDistance(p2));
    }
    
}
