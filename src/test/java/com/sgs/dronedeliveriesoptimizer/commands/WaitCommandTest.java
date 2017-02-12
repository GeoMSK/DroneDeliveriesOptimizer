package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author George Mantakos
 */
public class WaitCommandTest {

    public WaitCommandTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Drone.setMaxWeight(10);
        Drone.setProductWeights(new int[]{1, 2, 3});
        Drone.setMAX_TURNS(100);
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

    @Test
    public void test() {
        Drone drone = new Drone(new Position(0, 0));
        drone.addWaitCommand(3);

        assertFalse(drone.step(1));
        assertFalse(drone.step(2));
        assertTrue(drone.step(3));
        assertTrue(drone.step(4));
    }

}
