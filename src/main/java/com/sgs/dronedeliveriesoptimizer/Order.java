package com.sgs.dronedeliveriesoptimizer;

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
    private final HashMap<Integer, Integer> productQuantityPerType;

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
        this.productQuantityPerType = new HashMap<>();

        for (int i = 0; i < productsNo; i++) {
            int typeId = productTypes[i];
            Integer quantity = productQuantityPerType.get(typeId);
            if (quantity == null) {
                productQuantityPerType.put(typeId, 1);
            } else {
                productQuantityPerType.compute(typeId, (k, v) -> {
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
     * Products arrived for this order, remove them from list
     *
     * @param productTypeId the id of the type of the product
     * @param productNum the number of products to pick up
     */
    public void productsArrivedAction(int productTypeId, int productNum) {
        if (!productQuantityPerType.containsKey(productTypeId)) {
            throw new DroneActionException("productTypeId " + productTypeId + " is not contained in this order");
        }
        if (productQuantityPerType.get(productTypeId) != productNum) {
            throw new DroneActionException("Quantity of productTypeId " + productTypeId + " does not match the "
                    + "quantity requested by this order");
        }
        productQuantityPerType.remove(productTypeId);
        if (productQuantityPerType.isEmpty()) {
            markComplete();
        }
    }

    /**
     * This should be called during simulation when the order gets completed
     */
    private void markComplete() {
        // TODO: implement
    }

}
