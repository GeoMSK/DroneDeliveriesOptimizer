package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Order;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;

/**
 *
 * @author George Mantakos
 */
public class DeliverCommand extends Command {

    private final Order order;
    private final int productTypeId;
    private final int productNum;

    /**
     *
     * @param drone the drone to perform this command
     * @param order the order associated with this command
     * @param productTypeId the type id of the product to deliver
     * @param productNum how many products to deliver
     */
    public DeliverCommand(Drone drone, Order order, int productTypeId, int productNum) {
        super(drone);
        this.productTypeId = productTypeId;
        this.productNum = productNum;
        this.order = order;
        setRemainingTurns();
    }

    @Override
    protected final void setRemainingTurns() {
        Position startingPosition;
        Command cmd = drone.getCommandList().peek();
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
        order.productsArrivedAction(productTypeId, productNum, simulationStep);
        drone.unload(productTypeId, productNum);
        drone.updatePosition(order.getDeliveryPos());
        drone.setInTransit(false);
    }

    @Override
    public Position getResultingPosition() {
        return order.getDeliveryPos();
    }
}
