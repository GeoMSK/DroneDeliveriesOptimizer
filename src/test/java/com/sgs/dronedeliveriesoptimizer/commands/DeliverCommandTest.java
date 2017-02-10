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
    public void testOneType() {
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            drone.addLoadCommand(warehouse, 1, 5);
            drone.step(1);
        }
        Order order = new Order(new Position(0, 1), 5, new int[]{1, 1, 1, 1, 1});
        drone.addDeliverCommand(order, 1, 5);

        assertFalse(order.isCompleted());
        assertThat(order.getTurnCompleted(), is(-1));
        assertThat(order.getProductsNo(), is(5));
        assertArrayEquals(order.getProductTypes(), new int[]{1, 1, 1, 1, 1});
        assertThat(order.getProductQuantityPerType().get(1), is(5));

        assertFalse(drone.step(2));
        assertTrue(drone.step(3));

        assertTrue(order.isCompleted());
        assertTrue(order.getProductQuantityPerType().isEmpty());
        assertThat(order.getTurnCompleted(), is(3));
    }

    @Test
    public void testTwoTypes() {
        int s = 0;
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 7});
            drone.addLoadCommand(warehouse, 1, 5);
            drone.addLoadCommand(warehouse, 2, 7);
            drone.step(++s);
            drone.step(++s);
        }
        Order order = new Order(new Position(0, 1), 12, new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2});
        drone.addDeliverCommand(order, 1, 5);
        drone.addDeliverCommand(order, 2, 7);

        assertFalse(order.isCompleted());
        assertThat(order.getTurnCompleted(), is(-1));
        assertThat(order.getProductsNo(), is(12));
        assertArrayEquals(order.getProductTypes(), new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2});
        assertThat(order.getProductQuantityPerType().get(1), is(5));
        assertThat(order.getProductQuantityPerType().get(2), is(7));

        assertFalse(drone.step(++s));
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
        int s = 0;
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            drone.addLoadCommand(warehouse, 1, 5);
            drone.step(1);
        }
        Order order = new Order(new Position(0, 1), 5, new int[]{1, 1, 1, 1, 1});
        drone.addDeliverCommand(order, 5, 5);

        drone.step(++s);
        drone.step(++s);
    }

    @Test(expected = DroneActionException.class)
    public void testItemNotInOrder() {
        int s = 0;
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            drone.addLoadCommand(warehouse, 1, 5);
            drone.step(++s);
        }
        Order order = new Order(new Position(0, 1), 5, new int[]{1, 1, 1, 1, 1});
        drone.addDeliverCommand(order, 0, 5);

        drone.step(++s);
        drone.step(++s);
    }

    @Test(expected = DroneActionException.class)
    public void testIncorrectItemQuantity() {
        int s = 0;
        Drone drone = new Drone(new Position(0, 0));
        { // this is just to load the drone, it is tested in LoadCommandTest
            Warehouse warehouse = new Warehouse(new Position(0, 0), new int[]{0, 5, 0});
            drone.addLoadCommand(warehouse, 1, 5);
            drone.step(++s);
        }
        Order order = new Order(new Position(0, 1), 5, new int[]{1, 1, 1, 1, 1});
        drone.addDeliverCommand(order, 1, 6);

        drone.step(++s);
        drone.step(++s);
    }

}
