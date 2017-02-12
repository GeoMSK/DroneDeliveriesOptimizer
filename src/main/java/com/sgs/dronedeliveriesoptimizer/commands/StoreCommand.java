package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;
import com.sgs.dronedeliveriesoptimizer.simobjects.Warehouse;

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
     */
    public StoreCommand(Drone drone, Warehouse warehouse, int productTypeId, int productNum) {
        super(drone);
        this.warehouse = warehouse;
        this.productTypeId = productTypeId;
        this.productNum = productNum;
        setRemainingTurns();
    }

    @Override
    protected final void setRemainingTurns() {
        Position startingPosition;
        Command cmd = getLastNonWaitCommand();
        if (cmd == null) {
            startingPosition = new Position(0, 0);
        } else {
            startingPosition = cmd.getResultingPosition();
        }
        this.turnsRemaining = startingPosition.getDistance(getResultingPosition()) + 1;
        drone.increaseTotalTurns(this.turnsRemaining);
    }

    @Override
    protected void performAction() {
        warehouse.depositAction(productTypeId, productNum);
        drone.unload(productTypeId, productNum);
        drone.updatePosition(warehouse.getPosition());
        drone.setInTransit(false);
    }

    @Override
    public Position getResultingPosition() {
        return warehouse.getPosition();
    }

}
