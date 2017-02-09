package com.sgs.dronedeliveriesoptimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

/**
 *
 * @author George Mantakos
 */
public class Simulator {

    private final SimulationParameters simulationParameters;
    private final int[] productWeights;
    private final Warehouse[] warehouses;
    private final Order[] orders;
    private final String commandLog;
    private int commandNo;

    public Simulator(SimulationParameters simulationParameters, int[] productWeights, Warehouse[] warehouses,
            Order[] orders, String commandLog) {
        this.simulationParameters = simulationParameters;
        this.productWeights = productWeights;
        this.warehouses = warehouses;
        this.orders = orders;
        this.commandLog = commandLog;
    }

    public void simulate() throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(commandLog));
        this.commandNo = Integer.parseInt(br.readLine());
        for (int i = 0; i < commandNo; i++) {
            String cmd = br.readLine();
            try (Scanner scanner = new Scanner(cmd)) {
                scanner.useDelimiter(" ");
                int droneId = scanner.nextInt();
                String tag = scanner.next();
                switch (tag) {
                    case "L":
                        break;
                    case "U":
                        break;
                    case "D":
                        break;
                    case "W":
                        break;

                    default:
                        break;
                }

            }
        }
    }
}
