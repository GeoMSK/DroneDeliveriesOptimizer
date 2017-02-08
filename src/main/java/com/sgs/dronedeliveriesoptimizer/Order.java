package com.sgs.dronedeliveriesoptimizer;

/**
 *
 * @author George Mantakos
 */
public class Order {

    private final Position deliveryPos;
    private final int productsNo;
    private final int[] productTypes;

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

}
