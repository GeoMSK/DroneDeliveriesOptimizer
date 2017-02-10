package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;
import com.sgs.dronedeliveriesoptimizer.Order;

/**
 *
 * @author George Mantakos
 */
public class DeliverCommand extends Command{
    private final Order order;
    private final int productTypeId;
    private final int productNum;

    /**
     * 
     * @param drone the drone to perform this command
     * @param order the order associated with this command
     * @param productTypeId the type id of the product to deliver
     * @param productNum how many products to deliver
     * @param turns how many turns are needed for this command to finish
     */
    public DeliverCommand(Drone drone, Order order, int productTypeId, int productNum, int turns) {
        super(drone, turns);
        this.productTypeId = productTypeId;
        this.productNum = productNum;
        this.order = order;
    }

    @Override
    protected void performAction() {
        order.productsArrivedAction(productTypeId, productNum, simulationStep);
        drone.unload(productTypeId, productNum);
    }
    
}
