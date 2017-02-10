package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;
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
        Drone drone = new Drone();
        WaitCommand waitCommand = new WaitCommand(drone, 3);
        drone.addCommand(waitCommand);

        assertFalse(drone.step(1));
        assertFalse(drone.step(2));
        assertTrue(drone.step(3));
        assertTrue(drone.step(4));
    }

}
