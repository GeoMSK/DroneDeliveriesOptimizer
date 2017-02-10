package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;
import com.sgs.dronedeliveriesoptimizer.Warehouse;

/**
 *
 * @author George Mantakos
 */
public class StoreCommand extends Command {

    private final Warehouse warehouse;
    private final int productTypeId;
    private final int productNum;

    /**
     *
     * @param drone the drone to perform this command
     * @param warehouse the warehouse that this command will be performed
     * @param productTypeId the type id of the product to store to the warehouse
     * @param productNum how many products to store to the warehouse
     * @param turns how many turns are needed for this command to finish
     */
    public StoreCommand(Drone drone, Warehouse warehouse, int productTypeId, int productNum, int turns) {
        super(drone, turns);
        this.warehouse = warehouse;
        this.productTypeId = productTypeId;
        this.productNum = productNum;
    }

    @Override
    protected void performAction() {
        warehouse.depositAction(productTypeId, productNum);
        drone.unload(productTypeId, productNum);
    }

}
