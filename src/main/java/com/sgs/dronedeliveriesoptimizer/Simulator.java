package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.simobjects.Order;
import com.sgs.dronedeliveriesoptimizer.simobjects.Warehouse;
import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;
import com.sgs.dronedeliveriesoptimizer.commands.StoreCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
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
        while (it.hasNext()) {
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

    /**
     * pase the command log and initialize commands and drones
     *
     * @throws IOException if there is an error during parsing
     */
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
        populateDroneList();
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
        }
        return drones[id];
    }

    /**
     * initialize {@link #droneList} with drones from {@link #drones} array, is that order. Note that all drones in the
     * array must be initialized prior to calling this method
     */
    private void populateDroneList() {
        for (int i = 0; i < drones.length; i++) {
            Drone d = drones[i];
            if (d == null) {
                throw new RuntimeException("There was no command for drone " + i + " so it is not initialized");
            }
            droneList.add(d);
        }
    }

    /**
     *
     * @return the number of turns this simulation took to complete
     */
    public int getTurns() {
        return s;
    }

    /**
     * Finds the minimum distance warehouse which can serve an order. In case
     * there is not enough availability to serve an order from a single warehouse,
     * then null is returned.
     *
     * @param order the order for which we will find a warehouse to serve
     * @param position starting point from which drone will travel to the warehouse
     * @return the minimum distance warehouse which can serve an order
     */
    public Warehouse getWarehouseToServeOrder (Order order, Position position) {
        HashMap<Integer, Integer> productsQuantityPerType = order.getProductQuantityPerType();
        HashMap<Integer, Warehouse> warehousesMap = new HashMap();
        int minDistance = Integer.MAX_VALUE;
        outerloop:
        for (Warehouse warehouse : warehouses) {
            int products[] = warehouse.getProducts();
            for (Map.Entry<Integer, Integer> entry : productsQuantityPerType.entrySet()) {
                int type = entry.getKey();
                int quantity = entry.getValue();
                if (products[type] < quantity) {
                    // this warehouse cannot serve the order on its own, check next warehouse.
                    continue outerloop;
                }
            }
            int distance = position.getDistance(warehouse.getPosition()) + warehouse.getPosition().getDistance(order.getDeliveryPos());
            if (distance < minDistance) {
                minDistance = distance;
            }
            warehousesMap.put(distance, warehouse);
        }

        if (warehousesMap.containsKey(minDistance)) {
            return warehousesMap.get(minDistance);
        }
        return null;
    }

}
