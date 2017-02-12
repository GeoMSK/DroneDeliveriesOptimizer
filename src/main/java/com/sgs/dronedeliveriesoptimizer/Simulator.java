package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.commands.StoreCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
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
    private final Drone[] drones;
    private final LinkedList<Drone> droneList;
    /**
     * holds the current step number
     */
    private int s;

    public Simulator(SimulationParameters simulationParameters, int[] productWeights, Warehouse[] warehouses,
            Order[] orders, String commandLog) {
        this.simulationParameters = simulationParameters;
        this.productWeights = productWeights;
        this.warehouses = warehouses;
        this.orders = orders;
        this.commandLog = commandLog;
        this.drones = new Drone[simulationParameters.getDrones()];
        this.droneList = new LinkedList<>();
        this.s = 0;
    }

    public void simulate() throws IOException {
        setDroneConstants();
        parseCommandLog();
        while (!droneList.isEmpty()) {
            sortDroneList();
            stepAllDrones();
        }
    }

    /**
     * Brings to the front of the list all drones with an active unload command that has 1 step remaining. This is done
     * to ensure that always all unload commands will be executed before load commands
     */
    private void sortDroneList() {
        for (int i = 0; i < droneList.size(); i++) {
            Drone d = droneList.get(i);
            if (d.getRemainingCommandSteps() == 1 && d.getCurrentCommand() instanceof StoreCommand) {
                droneList.remove(d);
                droneList.addFirst(d);
            }
        }
    }

    /**
     * execute one step for all drones
     */
    private void stepAllDrones() {
        s++;
        Iterator<Drone> it = droneList.iterator();
        while(it.hasNext()) {
            Drone d = it.next();
            if (d.step(s)) {
                // this drone has no more commands
                it.remove();
            }
        }
    }

    /**
     * Sets constants of the {@link Drone} class. Needed for the simulation. This should be called prior to any other
     * simulation activity
     */
    private void setDroneConstants() {
        Drone.setMAX_TURNS(simulationParameters.getDeadline());
        Drone.setMaxWeight(simulationParameters.getMaximumLoad());
        Drone.setProductWeights(productWeights);
    }

    private void parseCommandLog() throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(commandLog));
        int commandNo = Integer.parseInt(br.readLine());
        for (int i = 0; i < commandNo; i++) {
            String cmd = br.readLine();
            try (Scanner scanner = new Scanner(cmd)) {
                scanner.useDelimiter(" ");
                int droneId = scanner.nextInt();
                Drone d = getDrone(droneId);
                String tag = scanner.next();
                switch (tag) {
                    case "L":
                        d.addLoadCommand(warehouses[scanner.nextInt()], scanner.nextInt(), scanner.nextInt());
                        break;
                    case "U":
                        d.addStoreCommand(warehouses[scanner.nextInt()], scanner.nextInt(), scanner.nextInt());
                        break;
                    case "D":
                        d.addDeliverCommand(orders[scanner.nextInt()], scanner.nextInt(), scanner.nextInt());
                        break;
                    case "W":
                        d.addWaitCommand(scanner.nextInt());
                        break;
                    default:
                        throw new RuntimeException("Unrecognized command: \"" + tag + "\"");
                }
            }
        }
    }

    /**
     * Get drone with given id
     *
     * @param id the id of the drone to get
     * @return the {@link Drone}
     */
    private Drone getDrone(int id) {
        if (drones[id] == null) {
            drones[id] = new Drone(new Position(0, 0));
            droneList.add(drones[id]);
        }
        return drones[id];
    }

    /**
     * 
     * @return the number of turns this simulation took to complete
     */
    public int getTurns() {
        return s;
    }
    
}
