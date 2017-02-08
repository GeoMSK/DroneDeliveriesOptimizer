package com.sgs.dronedeliveriesoptimizer;

import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author George Mantakos
 */
public class FileParserTest {

    public FileParserTest() {
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
     * Test of parse method, of class FileParser.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        InputStream is = FileParser.class.getResourceAsStream("/inputfiles/busy_day.in");
        FileParser fileParser = new FileParser();
        fileParser.parse(is);

        /*
         *  Simulation Parameters Test
         */
        SimulationParameters sp = fileParser.getSimulationParameters();
        assertEquals(400, sp.getRows());
        assertEquals(600, sp.getColumns());
        assertEquals(30, sp.getDrones());
        assertEquals(112993, sp.getDeadline());
        assertEquals(200, sp.getMaximumLoad());

        /*
         *  Product Weights Test
         */
        int[] pw = fileParser.getProductWeights();
        assertEquals(73, pw[0]);
        assertEquals(60, pw[pw.length - 1]);
        
        /*
         *  Warehouses Test
         */
        Warehouse[] warehouses = fileParser.getWarehouses();
        assertEquals(10, warehouses.length);
        assertEquals(113, warehouses[0].getPosition().getRow());
        assertEquals(179, warehouses[0].getPosition().getCol());
        assertEquals(0, warehouses[0].getProducts()[0]);
        assertEquals(5, warehouses[0].getProducts()[2]);
        assertEquals(7, warehouses[0].getProducts()[warehouses[0].getProducts().length-2]);
        
        assertEquals(297, warehouses[9].getPosition().getRow());
        assertEquals(423, warehouses[9].getPosition().getCol());
        assertEquals(1, warehouses[9].getProducts()[0]);
        assertEquals(0, warehouses[9].getProducts()[warehouses[9].getProducts().length-1]);
        
    }

}
