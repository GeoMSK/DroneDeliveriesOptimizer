package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.commands.LoadCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author George Mantakos
 */
public class DroneTest {

    public DroneTest() {
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
    public void dronePositioning() {
        int s = 0;
        Drone drone = new Drone(new Position(0, 0));
        
        assertThat(drone.getCurrentPosition().getRow(), is(0));
        assertThat(drone.getCurrentPosition().getCol(), is(0));
        assertFalse(drone.isInTransit());
        
        Warehouse w1 = new Warehouse(new Position(0, 2), new int[]{6, 0, 0});
        Warehouse w2 = new Warehouse(new Position(0, 5), new int[]{6, 4, 0});
        drone.addCommand(new LoadCommand(drone, w1, 0, 1));
        drone.addCommand(new LoadCommand(drone, w2, 0, 1));
        
        assertThat(drone.getTotalTurns(), is(3+4));
        
        assertFalse(drone.step(++s));
        assertThat(drone.getCurrentPosition().getRow(), is(0));
        assertThat(drone.getCurrentPosition().getCol(), is(0));
        assertTrue(drone.isInTransit());
        assertFalse(drone.step(++s));
        assertThat(drone.getCurrentPosition().getRow(), is(0));
        assertThat(drone.getCurrentPosition().getCol(), is(0));
        assertTrue(drone.isInTransit());
        assertFalse(drone.step(++s));
        // first command completed here
        assertThat(drone.getCurrentPosition().getRow(), is(0));
        assertThat(drone.getCurrentPosition().getCol(), is(2));
        assertFalse(drone.isInTransit());
        assertFalse(drone.step(++s));
        assertFalse(drone.step(++s));
        assertFalse(drone.step(++s));
        assertThat(drone.getCurrentPosition().getRow(), is(0));
        assertThat(drone.getCurrentPosition().getCol(), is(2));
        assertTrue(drone.isInTransit());
        assertTrue(drone.step(++s));
        // second command completed here
        assertThat(drone.getCurrentPosition().getRow(), is(0));
        assertThat(drone.getCurrentPosition().getCol(), is(5));
        assertFalse(drone.isInTransit());
    }

}
