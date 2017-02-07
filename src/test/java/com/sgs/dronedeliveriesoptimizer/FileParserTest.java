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
    }

}
