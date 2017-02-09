package com.sgs.dronedeliveriesoptimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
public class CommandLogTest {

    public CommandLogTest() {
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
     * Test of load method, of class CommandLog.
     */
    @Test
    public void testCommandLog() throws IOException {
        CommandLog cm = CommandLog.getInstance();
        cm.load(1, 2, 3, 4);
        cm.unload(5, 6, 7, 8);
        cm.deliver(9, 10, 11, 12);
        cm.wait(13, 14);

        try (BufferedReader br = new BufferedReader(new StringReader(cm.getCommands()))) {
            assertEquals("4", br.readLine());
            assertEquals("1 L 2 3 4", br.readLine());
            assertEquals("5 U 6 7 8", br.readLine());
            assertEquals("9 D 10 11 12", br.readLine());
            assertEquals("13 W 14", br.readLine());
        }
        cm.clear();
        assertEquals("0\n", cm.getCommands());
    }
}
