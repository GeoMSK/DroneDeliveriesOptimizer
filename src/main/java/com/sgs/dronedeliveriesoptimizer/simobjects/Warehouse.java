package com.sgs.dronedeliveriesoptimizer.simobjects;

import com.sgs.dronedeliveriesoptimizer.exceptions.DroneActionException;

/**
 *
 * @author George Mantakos
 */
public class Warehouse {

    private final Position position;
    private final int[] products;

    /**
     *
     * @param position the warehouse's position in the grid
     * @param products the number of products per product type in this warehouse. eg products[0] holds the number of
     * products of type 0
     */
    public Warehouse(Position position, int[] products) {
        this.position = position;
        this.products = products;
    }

    /**
     *
     * @return the number of products per product type in this warehouse. eg products[0] holds the number of products of
     * type 0
     */
    public int[] getProducts() {
        return products;
    }

    /**
     *
     * @return the warehouse's position in the grid
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Pick up specified products from this warehouse
     *
     * @param productTypeId the id of the type of the product
     * @param productNum the number of products to pick up
     *
     * @throws com.sgs.dronedeliveriesoptimizer.exceptions.DroneActionException if this action cannot be performed
     */
    public void withdrawAction(int productTypeId, int productNum) {
        if (productTypeId < 0 || productTypeId > products.length - 1) {
            throw new DroneActionException("productTypeId " + productTypeId + " does not exist");
        }
        if (products[productTypeId] < productNum) {
            throw new DroneActionException("There are not enough products of type " + productTypeId + " in this warehouse");
        }
        products[productTypeId] -= productNum;
    }

    /**
     * Store specified products in this warehouse
     *
     * @param productTypeId the id of the type of the product
     * @param productNum the number of products to store
     */
    public void depositAction(int productTypeId, int productNum) {
        if (productTypeId < 0 || productTypeId > products.length - 1) {
            throw new DroneActionException("productTypeId " + productTypeId + " does not exist");
        }
        products[productTypeId] += productNum;
    }

}
