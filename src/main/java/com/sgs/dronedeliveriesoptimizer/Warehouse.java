package com.sgs.dronedeliveriesoptimizer;

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

}
