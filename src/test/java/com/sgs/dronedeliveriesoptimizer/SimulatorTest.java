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
        cl.load(0, 0, 0, 5);
        cl.load(1, 1, 1, 5);
        cl.deliver(0, 0, 0, 5);
        cl.deliver(1, 1, 1, 5);
        Simulator sim = new Simulator(simulationParameters, productWeights, warehouses, orders, cl.getCommands());

        sim.simulate();

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
        cl.load(1, 0, 1, 5); // 2 turns
        cl.wait(0, 1); // sync with drone 1 to reach warehouse 1 in the same turn
        cl.load(0, 1, 1, 5); // 4 turns
        cl.unload(1, 1, 1, 5); // 3 turns
        
        cl.deliver(0, 0, 1, 5); // 10 turns

        Simulator sim = new Simulator(simulationParameters, productWeights, warehouses, orders, cl.getCommands());

        sim.simulate();

        assertEquals(15, sim.getTurns());
    }

}
