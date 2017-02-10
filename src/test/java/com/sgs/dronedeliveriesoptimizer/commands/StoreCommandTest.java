package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;
import com.sgs.dronedeliveriesoptimizer.Position;
import com.sgs.dronedeliveriesoptimizer.Warehouse;
import com.sgs.dronedeliveriesoptimizer.exceptions.DroneActionException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author George Mantakos
 */
public class StoreCommandTest {

    public StoreCommandTest() {
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
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5);
            drone.addCommand(loadCommand);
            drone.step(1);
        }

        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 0, 0});
        StoreCommand storeCommand = new StoreCommand(drone, warehouse, 1, 5);
        drone.addCommand(storeCommand);

        assertThat(warehouse.getProducts()[0], is(0));
        assertThat(warehouse.getProducts()[1], is(0));
        assertThat(warehouse.getProducts()[2], is(0));
        assertFalse(drone.isEmpty());
        assertThat(drone.getInventory().get(1), is(5));
        assertThat(drone.getCurrentWeight(), is(10));

        assertTrue(drone.step(1));

        assertThat(warehouse.getProducts()[0], is(0));
        assertThat(warehouse.getProducts()[1], is(5));
        assertThat(warehouse.getProducts()[2], is(0));
        assertTrue(drone.isEmpty());
        assertThat(drone.getCurrentWeight(), is(0));
    }

    @Test(expected = DroneActionException.class)
    public void testNotEnoughItemsToStore() {
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5);
            drone.addCommand(loadCommand);
            drone.step(1);
        }

        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 0, 0});
        StoreCommand storeCommand = new StoreCommand(drone, warehouse, 1, 6);
        drone.addCommand(storeCommand);

        drone.step(1);
    }

    @Test(expected = DroneActionException.class)
    public void testNonExistentItem() {
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5);
            drone.addCommand(loadCommand);
            drone.step(1);
        }

        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 0, 0});
        StoreCommand storeCommand = new StoreCommand(drone, warehouse, 4, 6);
        drone.addCommand(storeCommand);

        drone.step(1);
    }

    @Test(expected = DroneActionException.class)
    public void testItemNotInDronesInventory() {
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5);
            drone.addCommand(loadCommand);
            drone.step(1);
        }

        Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 0, 0});
        StoreCommand storeCommand = new StoreCommand(drone, warehouse, 2, 6);
        drone.addCommand(storeCommand);

        drone.step(1);
    }

}
