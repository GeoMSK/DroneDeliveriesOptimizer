package com.sgs.dronedeliveriesoptimizer;

/**
 *
 * @author George Mantakos
 */
public class CommandLog {

    private StringBuilder sb;
    private int commandNum;

    public CommandLog() {
        this.sb = new StringBuilder();
        this.commandNum = 0;
    }

    /**
     * Appends a new Load command to the command log
     *
     * @param droneId the ID of the drone that the command is for
     * @param warehouseId the ID of the warehouse from which we load items
     * @param productTypeId the ID of the product type
     * @param productNum the number of items of the product type to be loaded ­ a positive integer
     */
    public void load(int droneId, int warehouseId, int productTypeId, int productNum) {
        _loadUnload(droneId, CommandTag.LOAD, warehouseId, productTypeId, productNum);
    }

    /**
     * Appends a new Unload command to the command log
     *
     * @param droneId the ID of the drone that the command is for
     * @param warehouseId the ID of the warehouse to which we unload items
     * @param productTypeId the ID of the product type
     * @param productNum the number of items of the product type to be unloaded ­ a positive integer
     */
    public void unload(int droneId, int warehouseId, int productTypeId, int productNum) {
        _loadUnload(droneId, CommandTag.UNLOAD, warehouseId, productTypeId, productNum);
    }

    /**
     * Appends a new Deliver command to the command log
     *
     * @param droneId the ID of the drone that the command is for
     * @param customerOrderId the ID of the customer order we are delivering items for
     * @param productTypeId the ID of the product type
     * @param productNum the number of items of the product type to be delivered ­a positive integer
     */
    public void deliver(int droneId, int customerOrderId, int productTypeId, int productNum) {
        sb.append(String.format("%d %s %d %d %d\n", droneId, CommandTag.DELIVER, customerOrderId, productTypeId, productNum));
        this.commandNum++;
    }

    /**
     * Appends a new Wait command to the command log
     *
     * @param droneId the ID of the drone that the command is for
     * @param turns the number of turns for which the drone needs to wait ­ a positive integer
     */
    public void wait(int droneId, int turns) {
        sb.append(String.format("%d %s %d\n", droneId, CommandTag.WAIT, turns));
        this.commandNum++;
    }

    private void _loadUnload(int droneId, CommandTag commandTag, int warehouseId, int productTypeId, int productNum) {
        sb.append(String.format("%d %s %d %d %d\n", droneId, commandTag, warehouseId, productTypeId, productNum));
        this.commandNum++;
    }
    
    /**
     * Clears the CommandLog
     */
    public void clear() {
        this.sb = new StringBuilder();
        this.commandNum = 0;
    }

    /**
     *
     * @return the commands in this CommandLog
     */
    public String getCommands() {
        return String.valueOf(this.commandNum) + "\n" + sb.toString();
    }

    public static enum CommandTag {
        LOAD("L"),
        UNLOAD("U"),
        DELIVER("D"),
        WAIT("W");

        private final String tag;

        private CommandTag(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return tag;
        }
    }
}
