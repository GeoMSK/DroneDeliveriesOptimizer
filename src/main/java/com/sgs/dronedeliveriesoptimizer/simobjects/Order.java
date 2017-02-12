package com.sgs.dronedeliveriesoptimizer.simobjects;

import com.sgs.dronedeliveriesoptimizer.exceptions.DroneActionException;
import java.util.HashMap;

/**
 *
 * @author George Mantakos
 */
public class Order {

    private final Position deliveryPos;
    private final int productsNo;
    private final int[] productTypes;
    private final HashMap<Integer, Integer> remainingProductQuantityPerType;
    private int turnCompleted;

    /**
     *
     * @param deliveryPos the position in the grid where the delivery should be made
     * @param productsNo the total number of products in this order
     * @param productTypes the type of each product
     */
    public Order(Position deliveryPos, int productsNo, int[] productTypes) {
        this.deliveryPos = deliveryPos;
        this.productsNo = productsNo;
        this.productTypes = productTypes;
        this.remainingProductQuantityPerType = new HashMap<>();

        for (int i = 0; i < productsNo; i++) {
            int typeId = productTypes[i];
            Integer quantity = remainingProductQuantityPerType.get(typeId);
            if (quantity == null) {
                remainingProductQuantityPerType.put(typeId, 1);
            } else {
                remainingProductQuantityPerType.compute(typeId, (k, v) -> {
                    return v + 1;
                });
            }
        }
    }

    /**
     *
     * @return the position in the grid where the delivery should be made
     */
    public Position getDeliveryPos() {
        return deliveryPos;
    }

    /**
     *
     * @return the total number of products in this order
     */
    public int getProductsNo() {
        return productsNo;
    }

    /**
     *
     * @return the type of each product
     */
    public int[] getProductTypes() {
        return productTypes;
    }

    /**
     *
     * @return the remaining product quantity per type
     */
    public HashMap<Integer, Integer> getProductQuantityPerType() {
        return remainingProductQuantityPerType;
    }

    /**
     * Products arrived for this order, remove them from list
     *
     * @param productTypeId the id of the type of the product
     * @param productNum the number of products to pick up
     * @param simulationStep the number of this step from the beginning of the simulation
     */
    public void productsArrivedAction(int productTypeId, int productNum, int simulationStep) {
        if (remainingProductQuantityPerType.isEmpty()) {
            throw new DroneActionException("Trying to deliver to a finished order");
        }
        if (!remainingProductQuantityPerType.containsKey(productTypeId)) {
            throw new DroneActionException("productTypeId " + productTypeId + " is not contained in this order");
        }
        if (remainingProductQuantityPerType.get(productTypeId) != productNum) {
            throw new DroneActionException("Quantity of productTypeId " + productTypeId + " does not match the "
                    + "quantity requested by this order");
        }
        remainingProductQuantityPerType.remove(productTypeId);
        if (remainingProductQuantityPerType.isEmpty()) {
            markComplete(simulationStep);
        }
    }

    /**
     * This should be called during simulation when the order gets completed
     */
    private void markComplete(int simulationStep) {
        turnCompleted = simulationStep;
    }

    /**
     *
     * @return true if this order is completed
     */
    public boolean isCompleted() {
        return remainingProductQuantityPerType.isEmpty();
    }

    /**
     *
     * @return the turn that this order was completed or -1 if this order is not complete yet
     */
    public int getTurnCompleted() {
        return isCompleted() ? turnCompleted : -1;
    }

}
