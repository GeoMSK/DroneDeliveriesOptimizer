package com.sgs.dronedeliveriesoptimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author George Mantakos
 */
public class FileParser {

    private SimulationParameters simulationParameters;
    private int[] productWeights;
    private int productTypesNo;
    private int warehouseNo;
    private Warehouse[] warehouses;
    private int ordersNo;
    private Order[] orders;

    public FileParser() {
    }

    public void parse(File file) throws FileNotFoundException, IOException {
        _parse(new BufferedReader(new FileReader(file)));
    }

    public void parse(InputStream stream) throws FileNotFoundException, IOException {
        _parse(new BufferedReader(new InputStreamReader(stream)));
    }

    private void _parse(BufferedReader br) throws FileNotFoundException, IOException {
        parseFirstLine(br.readLine());
        parseProductWeights(br.readLine(), br.readLine());
        this.warehouseNo = Integer.parseInt(br.readLine());
        this.warehouses = new Warehouse[this.warehouseNo];
        for (int i = 0; i < this.warehouseNo; i++) {
            warehouses[i] = parseWarehouse(br.readLine(), br.readLine());
        }
        this.ordersNo = Integer.parseInt(br.readLine());
        orders = new Order[this.ordersNo];
        for (int i = 0; i < this.ordersNo; i++) {
            orders[i] = parseOrder(br.readLine(), br.readLine(), br.readLine());
        }
    }

    /**
     * The first section of the file describes the parameters of the simulation. This section contains a single line
     * containing the following natural numbers separated by single spaces: <ul>
     * <li> number of rows in the area of the simulation ( 1 ≤ number of rows ≤ 1 0000) </li>
     * <li> number of columns in the area of the simulation ( 1 ≤ number of columns ≤ 10000) </li>
     * <li> D ­ number of drones available ( 1 ≤ D ≤ 1 000) </li>
     * <li> deadline of the simulation ( 1 ≤ deadline of the simulation ≤ 1 000000) </li>
     * <li> maximum load of a drone ( 1 ≤ maximum load of a drone ≤ 1 0000) </li>
     * </ul>
     *
     * @param line the first line
     *
     * @throws InputMismatchException if the next token does not match the Integer regular expression, or is out of
     * range
     * @throws NoSuchElementException if scanner input is exhausted
     * @throws IllegalStateException if scanner is closed
     */
    private void parseFirstLine(String line) {
        try (Scanner scanner = new Scanner(line)) {
            scanner.useDelimiter(" ");
            int rows = scanner.nextInt();
            int columns = scanner.nextInt();
            int drones = scanner.nextInt();
            int deadline = scanner.nextInt();
            int maxLoad = scanner.nextInt();
            this.simulationParameters = new SimulationParameters(rows, columns, drones, deadline, maxLoad);
        }
    }

    /**
     * The next section of the file describes the ​ weights of the products available for orders​ . This section
     * contains: <ul>
     * <li> a line containing the following natural number: </li>
     * <ul><li>P ­ the number of different product types available in warehouses ( 1 ≤ P ≤ 10000) </li></ul>
     * <li> a line containing P natural numbers separated by single spaces denoting weights of subsequent products
     * types, from product type 0 to product type P­1. For each weight, 1 ≤ weight ≤ maximum load of a drone. </li>
     * </ul>
     *
     * @param line1 contains the number of different product types available in warehouses
     * @param line2 a line containing P natural numbers separated by single spaces denoting weights of subsequent
     * products types
     *
     * @throws InputMismatchException if the next token does not match the Integer regular expression, or is out of
     * range
     * @throws NoSuchElementException if scanner input is exhausted
     * @throws IllegalStateException if scanner is closed
     */
    private void parseProductWeights(String line1, String line2) {
        this.productTypesNo = Integer.parseInt(line1);
        this.productWeights = new int[this.productTypesNo];
        try (Scanner scanner = new Scanner(line2)) {
            scanner.useDelimiter(" ");
            for (int i = 0; i < this.productTypesNo; i++) {
                productWeights[i] = scanner.nextInt();
            }
        }
    }

    /**
     * The next section of the file describes the ​ warehouses and availability of individual product types at each
     * warehouse. This section contains:<ul>
     * <li> a line containing the following natural number: </li>
     * <ul><li> W ­ the number of warehouses ( 1 ≤ W ≤ 10000) </li></ul>
     * <li>two lines for each warehouse, each two lines describing the subsequent warehouses from warehouse 0 to
     * warehouse W − 1 : </li>
     * <ul><li>a line containing two natural numbers separated by a single space: the row and the column in which the
     * warehouse is located ( 0 ≤ row &lt number of rows; 0 ≤ column &lt number of columns) </li>
     * <li> a line containing P natural numbers separated by single spaces: number of items of the subsequent product
     * types available at the warehouse, from product type 0 to product type P − 1 . For each product type, 0 ≤ number
     * of items ≤ 10000 holds. </li>
     * </ul>
     *
     * @param line1 a line containing the row and column of the warehouse
     * @param line2 a line containing the number of products per product type
     *
     * @return a {@link Warehouse} object
     */
    private Warehouse parseWarehouse(String line1, String line2) {
        int row, col;
        int[] products = new int[this.productTypesNo];
        try (Scanner scanner = new Scanner(line1)) {
            scanner.useDelimiter(" ");
            row = scanner.nextInt();
            col = scanner.nextInt();
        }
        try (Scanner scanner = new Scanner(line2)) {
            for (int i = 0; i < this.productTypesNo; i++) {
                products[i] = scanner.nextInt();
            }
        }
        return new Warehouse(new Position(row, col), products);
    }

    /**
     * The next section of the file describes the customer​orders. This section contains:
     * <ul><li> a line containing the following natural number </li>
     * <ul><li> C ­ the number of customer orders ( 1 ≤ C ≤ 10000) </li></ul>
     * <li> three lines for each order, each three lines describing the subsequent orders from order 0 to C ­ 1: </li>
     * <ul><li> a line containing two natural numbers separated by a single space: the row of the delivery cell and the
     * column of the delivery cell ( 0 ≤ row &lt number of rows; 0 ≤ column &lt number of columns) </li>
     * <li> a line containing one natural number Li ­ the number of the ordered product items ( 1 ≤ Li &lt 10000) </li>
     * <li> a line containing Li integers separated by single spaces: the product types of the individual product items.
     * For each of the product types, 0 ≤ product type &lt P holds. </li>
     * </ul>
     * </ul>
     *
     * @param line1 a line containing the the row and column of the delivery position
     * @param line2 a line containing the number of ordered products
     * @param line3 a line containing the type of each product
     * @return
     */
    private Order parseOrder(String line1, String line2, String line3) {
        int row, col;
        try (Scanner scanner = new Scanner(line1)) {
            scanner.useDelimiter(" ");
            row = scanner.nextInt();
            col = scanner.nextInt();
        }
        int productsNo = Integer.parseInt(line2);
        int[] productTypes = new int[productsNo];
        try (Scanner scanner = new Scanner(line3)) {
            scanner.useDelimiter(" ");
            for (int i = 0; i < productsNo; i++) {
                productTypes[i] = scanner.nextInt();
            }
        }
        return new Order(new Position(row, col), productsNo, productTypes);
    }

    /**
     *
     * @return the {@link SimulationParameters} object
     */
    public SimulationParameters getSimulationParameters() {
        return simulationParameters;
    }

    /**
     *
     * @return the weight of given product type
     */
    public int[] getProductWeights() {
        return productWeights;
    }

    /**
     *
     * @return the total number of product types
     */
    public int getProductTypesNo() {
        return productTypesNo;
    }

    /**
     *
     * @return the total number of warehouses
     */
    public int getWarehouseNo() {
        return warehouseNo;
    }

    /**
     *
     * @return the warehouses
     */
    public Warehouse[] getWarehouses() {
        return warehouses;
    }

    /**
     *
     * @return the total number of orders
     */
    public int getOrdersNo() {
        return ordersNo;
    }

    /**
     *
     * @return the orders
     */
    public Order[] getOrders() {
        return orders;
    }

}
