package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.simobjects.Order;
import com.sgs.dronedeliveriesoptimizer.simobjects.Warehouse;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;
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
public class SimulatorTest {

    public SimulatorTest() {
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
     * Test of simulate method, of class Simulator.
     */
    @Test
    public void testSimulate() throws Exception {
        SimulationParameters simulationParameters = new SimulationParameters(10, 10, 2, 100, 50);
        int[] productWeights = {1, 2, 3};
        Warehouse[] warehouses = {
            new Warehouse(new Position(0, 1), new int[]{5, 5, 5}),
            new Warehouse(new Position(0, 3), new int[]{5, 5, 5}),};
        Order[] orders = {
            new Order(new Position(9, 1), 5, new int[]{0, 0, 0, 0, 0}),
            new Order(new Position(9, 3), 5, new int[]{1, 1, 1, 1, 1})
        };
        CommandLog cl = new CommandLog();
        cl.load(0, 0, 0, 5); // 2 turns, now at (0,1)
        cl.load(1, 1, 1, 5); // 4 turns, now at (0,3)
        cl.deliver(0, 0, 0, 5); // 10 turns, total 12 turns
        cl.deliver(1, 1, 1, 5); // 10 turns, total 14 turns
        Simulator sim = new Simulator(simulationParameters, productWeights, warehouses, orders, cl.getCommands());

        sim.simulate();

        // Since the 2 drones are moving in parallel, turns are equal to MAX(12,14) = 14
        assertEquals(14, sim.getTurns());
    }

    @Test
    public void testSameturnLoadUnload() throws Exception {
        SimulationParameters simulationParameters = new SimulationParameters(10, 10, 2, 100, 50);
        int[] productWeights = {1, 2, 3};
        Warehouse[] warehouses = {
            new Warehouse(new Position(0, 1), new int[]{5, 5, 5}),
            new Warehouse(new Position(0, 3), new int[]{0, 0, 0})};
        Order[] orders = {
            new Order(new Position(9, 3), 5, new int[]{1, 1, 1, 1, 1})
        };
        CommandLog cl = new CommandLog();
        // order drone 1 to make the unload in purpose to check sortDroneList() function of Simulation class
        // choosing drone 1 this way will make the load command (drone 0) to be first in the execution order if
        // sortDroneList() is not called
        cl.load(1, 0, 1, 5); // 2 turns
        cl.wait(0, 1); // sync with drone 1 to reach warehouse 1 in the same turn
        cl.load(0, 1, 1, 5); // 4 turns
        cl.unload(1, 1, 1, 5); // 3 turns

        cl.deliver(0, 0, 1, 5); // 10 turns

        Simulator sim = new Simulator(simulationParameters, productWeights, warehouses, orders, cl.getCommands());

        sim.simulate();

        assertEquals(15, sim.getTurns());
    }

    @Test
    public void testGetWarehouseToServeOrder () {
        SimulationParameters simulationParameters = new SimulationParameters(10, 10, 2, 100, 50);
        int[] productWeights = {1, 2, 3};
        Warehouse[] warehouses = {
            new Warehouse(new Position(0, 1), new int[]{5, 5, 5}),
            new Warehouse(new Position(0, 3), new int[]{0, 0, 0})};
        Order[] orders = {
            new Order(new Position(9, 1), 6, new int[]{0, 0, 0, 0, 0, 0}),
            new Order(new Position(9, 3), 5, new int[]{1, 1, 1, 1, 1}),
        };

        Simulator sim = new Simulator(simulationParameters, productWeights, warehouses, orders, "");
        Warehouse wh = sim.getWarehouseToServeOrder(orders[1], new Position(0, 0));
        assertEquals(0, wh.getPosition().getRow());
        assertEquals(1, wh.getPosition().getCol());

        wh = sim.getWarehouseToServeOrder(orders[0], new Position(0, 0));
        assertNull(wh);
    }

}
