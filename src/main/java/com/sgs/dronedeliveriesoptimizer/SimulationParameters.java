package com.sgs.dronedeliveriesoptimizer;

/**
 *
 * @author George Mantakos
 */
public class SimulationParameters {

    private final int rows;
    private final int columns;
    private final int drones;
    private final int deadline;
    private final int maxLoad;

    /**
     *
     * @param rows number of rows in the area of the simulation ( 1 ≤ number of rows ≤ 10000)
     * @param columns number of columns in the area of the simulation ( 1 ≤ number of columns ≤ 10000)
     * @param drones number of drones available ( 1 ≤ D ≤ 1000)
     * @param deadline deadline of the simulation ( 1 ≤ deadline of the simulation ≤ 1000000)
     * @param maxLoad maximum load of a drone ( 1 ≤ maximum load of a drone ≤ 10000)
     */
    public SimulationParameters(int rows, int columns, int drones, int deadline, int maxLoad) {
        this.rows = rows;
        this.columns = columns;
        this.drones = drones;
        this.deadline = deadline;
        this.maxLoad = maxLoad;
    }

    /**
     *
     * @return the number of rows in the area of the simulation ( 1 ≤ number of rows ≤ 10000)
     */
    public int getRows() {
        return rows;
    }

    /**
     *
     * @return the number of columns in the area of the simulation ( 1 ≤ number of columns ≤ 10000)
     */
    public int getColumns() {
        return columns;
    }

    /**
     *
     * @return the number of drones available ( 1 ≤ D ≤ 1000)
     */
    public int getDrones() {
        return drones;
    }

    /**
     *
     * @return the deadline of the simulation ( 1 ≤ deadline of the simulation ≤ 1000000)
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     *
     * @return the maximum load of a drone ( 1 ≤ maximum load of a drone ≤ 10000)
     */
    public int getMaximumLoad() {
        return maxLoad;
    }

}
