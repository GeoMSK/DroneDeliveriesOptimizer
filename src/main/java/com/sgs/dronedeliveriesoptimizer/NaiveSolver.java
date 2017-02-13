package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Order;
import com.sgs.dronedeliveriesoptimizer.simobjects.Warehouse;
import java.util.HashMap;

/**
 *
 * @author George Mantakos
 */
public class NaiveSolver extends Solver {

    private int droneId;
    private CommandLog cl;

    public NaiveSolver(SimulationParameters simulationParameters, int[] productWeights, Warehouse[] warehouses, Order[] orders) {
        super(simulationParameters, productWeights, warehouses, orders);
        droneId = -1;
    }

    @Override
    public CommandLog solve() {
        this.cl = new CommandLog();
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            HashMap<Integer, Integer> q = order.getProductQuantityPerType();
            for (Integer pType : q.keySet()) {
                int pQuantity = q.get(pType);
                int w = findWarehouse(pType, pQuantity);
                if (w != -1) {
                    scheduleDelivery(getNextDroneId(), w, i, pType, pQuantity);
                }
            }
        }

        return cl;
    }

    /**
     *
     * @return the next drone id using a round robin algorithm
     */
    private int getNextDroneId() {
        int d = ++droneId;
        if (d > simulationParameters.getDrones()-1) {
            d = 0;
        }
        return d;
    }

    /**
     * Add commands to the given drone to pickup specified items and deliver them to the location of the order
     *
     * @param drone
     * @param warehouse
     * @param order
     * @param productType
     * @param quantity
     */
    private void scheduleDelivery(int droneId, int warehouseId, int orderId, int productType, int quantity) {
        cl.load(droneId, warehouseId, productType, quantity);
        warehouses[warehouseId].withdrawAction(productType, quantity);
        cl.deliver(droneId, orderId, productType, quantity);
    }

    /**
     * Find a warehouse that has the given product type in the given quantity
     *
     * @param productType the desired product type
     * @param quantity the desired quantity
     * @return the warehouse id or -1 if no such warehouse was found
     */
    private int findWarehouse(int productType, int quantity) {
        int w = -1;
        for (int i = 0; i < warehouses.length; i++) {
            Warehouse warehouse = warehouses[i];
            if (warehouse.contains(productType, quantity)) {
                w = i;
                break;
            }
        }
        return w;
    }

}
