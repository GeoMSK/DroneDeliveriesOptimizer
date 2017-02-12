package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;
import com.sgs.dronedeliveriesoptimizer.Position;
import com.sgs.dronedeliveriesoptimizer.Warehouse;
import com.sgs.dronedeliveriesoptimizer.exceptions.DroneActionException;
import static org.hamcrest.CoreMatchers.*;
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
public class LoadCommandTest {

    public LoadCommandTest() {
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
        Warehouse warehouse = new Warehouse(new Position(0, 1), new int[]{6, 0, 0});
        drone.addLoadCommand(warehouse, 0, 5);

        assertThat(drone.getCurrentWeight(), is(0));
        assertThat(warehouse.getProducts()[0], is(6));
        assertFalse(drone.step(1));
        assertTrue(drone.step(2));
        assertTrue(drone.step(3));
        assertThat(drone.getCurrentWeight(), is(5));
        assertThat(warehouse.getProducts()[0], is(1));
    }

    @Test(expected = DroneActionException.class)
    public void testNotEnoughItems() {
        Drone drone = new Drone(new Position(0, 0));
        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{6, 0, 0});
        drone.addLoadCommand(warehouse, 0, 7);

        drone.step(1);
    }

    @Test(expected = DroneActionException.class)
    public void testNonExistentitem() {
        Drone drone = new Drone(new Position(0, 0));
        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{6, 0, 0});
        drone.addLoadCommand(warehouse, 10, 2);

        drone.step(1);
    }

    @Test(expected = DroneActionException.class)
    public void testItemNotInWarehouse() {
        Drone drone = new Drone(new Position(0, 0));
        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{6, 0, 0});
        drone.addLoadCommand(warehouse, 1, 2);

        drone.step(1);
    }

}
