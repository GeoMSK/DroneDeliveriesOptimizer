package com.sgs.dronedeliveriesoptimizer;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author George Mantakos
 */
public class DroneDeliveriesOptimizer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        InputStream is = FileParser.class.getResourceAsStream("/inputfiles/busy_day.in");
        FileParser fp = new FileParser();
        fp.parse(is);

        NaiveSolver naiveSolver = new NaiveSolver(fp.getSimulationParameters(), fp.getProductWeights(),
                fp.getWarehouses(), fp.getOrders());
        CommandLog cl = naiveSolver.solve();

        Simulator simulator = new Simulator(fp.getSimulationParameters(), fp.getProductWeights(),
                fp.getWarehouses(), fp.getOrders(), cl.getCommands());
        simulator.simulate();
        System.out.println("Total turns: " + simulator.getTurns());
    }

}
