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

    public SimulationParameters getSimulationParameters() {
        return simulationParameters;
    }

    public int[] getProductWeights() {
        return productWeights;
    }

    public int getProductTypesNo() {
        return productTypesNo;
    }

    public int getWarehouseNo() {
        return warehouseNo;
    }

    public Warehouse[] getWarehouses() {
        return warehouses;
    }
    
    

}
