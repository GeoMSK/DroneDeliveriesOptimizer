package com.sgs.dronedeliveriesoptimizer;

/**
 *
 * @author George Mantakos
 */
public class Warehouse {

    private final Position position;
    private final int[] products;

    public Warehouse(Position position, int[] products) {
        this.position = position;
        this.products = products;
    }

    public int[] getProducts() {
        return products;
    }

    public Position getPosition() {
        return position;
    }

}
