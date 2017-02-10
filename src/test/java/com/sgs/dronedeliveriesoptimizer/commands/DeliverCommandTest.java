package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;
import com.sgs.dronedeliveriesoptimizer.Order;
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
public class DeliverCommandTest {

    public DeliverCommandTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Drone.setMaxWeight(31);
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
    public void testOneType() {
        Drone drone = new Drone();
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 1), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5, 1);
            drone.addCommand(loadCommand);
            drone.step(1);
        }
        Order order = new Order(new Position(1, 1), 5, new int[]{1, 1, 1, 1, 1});
        DeliverCommand deliverCommand = new DeliverCommand(drone, order, 1, 5, 1);
        drone.addCommand(deliverCommand);

        assertFalse(order.isCompleted());
        assertThat(order.getTurnCompleted(), is(-1));
        assertThat(order.getProductsNo(), is(5));
        assertArrayEquals(order.getProductTypes(), new int[]{1, 1, 1, 1, 1});
        assertThat(order.getProductQuantityPerType().get(1), is(5));

        assertTrue(drone.step(2));

        assertTrue(order.isCompleted());
        assertTrue(order.getProductQuantityPerType().isEmpty());
        assertThat(order.getTurnCompleted(), is(2));
    }

    @Test
    public void testTwoTypes() {
        int s = 0;
        Drone drone = new Drone();
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 1), new int[]{0, 5, 7});
            drone.addCommand(new LoadCommand(drone, warehouse, 1, 5, 1));
            drone.addCommand(new LoadCommand(drone, warehouse, 2, 7, 1));
            drone.step(++s);
            drone.step(++s);
        }
        Order order = new Order(new Position(1, 1), 12, new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2});
        drone.addCommand(new DeliverCommand(drone, order, 1, 5, 1));
        drone.addCommand(new DeliverCommand(drone, order, 2, 7, 1));

        assertFalse(order.isCompleted());
        assertThat(order.getTurnCompleted(), is(-1));
        assertThat(order.getProductsNo(), is(12));
        assertArrayEquals(order.getProductTypes(), new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2});
        assertThat(order.getProductQuantityPerType().get(1), is(5));
        assertThat(order.getProductQuantityPerType().get(2), is(7));

        assertFalse(drone.step(++s));

        assertFalse(order.isCompleted());
        assertThat(order.getTurnCompleted(), is(-1));
        assertNull(order.getProductQuantityPerType().get(1));
        assertThat(order.getProductQuantityPerType().get(2), is(7));

        assertTrue(drone.step(++s));

        assertTrue(order.isCompleted());
        assertThat(order.getTurnCompleted(), is(s));
        assertNull(order.getProductQuantityPerType().get(1));
        assertNull(order.getProductQuantityPerType().get(2));
        assertTrue(order.getProductQuantityPerType().isEmpty());
    }

    @Test(expected = DroneActionException.class)
    public void testNonExistentItem() {
        Drone drone = new Drone();
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 1), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5, 1);
            drone.addCommand(loadCommand);
            drone.step(1);
        }
        Order order = new Order(new Position(1, 1), 5, new int[]{1, 1, 1, 1, 1});
        DeliverCommand deliverCommand = new DeliverCommand(drone, order, 5, 5, 1);
        drone.addCommand(deliverCommand);

        drone.step(1);
    }

    @Test(expected = DroneActionException.class)
    public void testItemNotInOrder() {
        Drone drone = new Drone();
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 1), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5, 1);
            drone.addCommand(loadCommand);
            drone.step(1);
        }
        Order order = new Order(new Position(1, 1), 5, new int[]{1, 1, 1, 1, 1});
        DeliverCommand deliverCommand = new DeliverCommand(drone, order, 0, 5, 1);
        drone.addCommand(deliverCommand);

        drone.step(1);
    }

    @Test(expected = DroneActionException.class)
    public void testIncorrectItemQuantity() {
        Drone drone = new Drone();
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 1), new int[]{0, 5, 0});
            LoadCommand loadCommand = new LoadCommand(drone, warehouse, 1, 5, 1);
            drone.addCommand(loadCommand);
            drone.step(1);
        }
        Order order = new Order(new Position(1, 1), 5, new int[]{1, 1, 1, 1, 1});
        DeliverCommand deliverCommand = new DeliverCommand(drone, order, 1, 6, 1);
        drone.addCommand(deliverCommand);

        drone.step(1);
    }

}
