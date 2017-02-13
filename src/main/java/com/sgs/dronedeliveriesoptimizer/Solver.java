package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.simobjects.Order;
import com.sgs.dronedeliveriesoptimizer.simobjects.Warehouse;

/**
 *
 * @author George Mantakos
 */
public abstract class Solver {

    protected final SimulationParameters simulationParameters;
    protected final int[] productWeights;
    protected final Warehouse[] warehouses;
    protected final Order[] orders;

    public Solver(SimulationParameters simulationParameters, int[] productWeights, Warehouse[] warehouses, Order[] orders) {
        this.simulationParameters = simulationParameters;
        this.productWeights = productWeights;
        this.warehouses = warehouses;
        this.orders = orders;
    }

    /**
     * This function should Arrange the deliveries of the drones and generate appropriate commands
     *
     * @return the {@link CommandLog} with the commands generated
     */
    public abstract CommandLog solve();

}
