package com.sgs.dronedeliveriesoptimizer.simobjects;

import com.sgs.dronedeliveriesoptimizer.commands.Command;
import com.sgs.dronedeliveriesoptimizer.commands.DeliverCommand;
import com.sgs.dronedeliveriesoptimizer.commands.LoadCommand;
import com.sgs.dronedeliveriesoptimizer.commands.StoreCommand;
import com.sgs.dronedeliveriesoptimizer.commands.WaitCommand;
import com.sgs.dronedeliveriesoptimizer.exceptions.DroneActionException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author George Mantakos
 */
public class Drone {

    private static int MAX_WEIGHT;
    private static int[] PRODUCT_WEIGHTS;
    private static int MAX_TURNS;
    private Position currentPosition;
    private boolean inTransit;
    private int currentWeight;
    private final HashMap<Integer, Integer> inventory;
    private final LinkedList<Command> commandList;
    private Command currentCommand;
    private int totalTurns;

    /**
     *
     * @param startingPosition the starting position of the drone
     */
    public Drone(Position startingPosition) {
        this.currentPosition = startingPosition;
        this.inventory = new HashMap<>();
        this.commandList = new LinkedList<>();
        this.inTransit = false;
        this.totalTurns = 0;
    }

    /**
     *
     * @param turns increase this drone's total turns by this amount
     */
    public void increaseTotalTurns(int turns) {
        this.totalTurns += turns;
        if (this.totalTurns > MAX_TURNS) {
            throw new DroneActionException("Drone turns exceeded maximum allowed value");
        }
    }

    /**
     * Updates the drones position
     *
     * @param newPosition the new position of the drone
     */
    public void updatePosition(Position newPosition) {
        this.currentPosition = newPosition;
    }

    /**
     * Executes a simulation step for this drone. Executes a simulation step for the current running command until there
     * are no commands left.
     *
     * @param simulationStep the number of this step from the beginning of the simulation
     * @return true if there are no more commands to execute, false otherwise
     */
    public boolean step(int simulationStep) {
        if (currentCommand == null && !commandList.isEmpty()) {
            // no running command, get one from the command list
            currentCommand = commandList.pop();
            setInTransit(true);
        } else if (currentCommand == null) {
            // there are no commands in the command list for this drone
            setInTransit(false);
            return true;
        } else {
            // there is a current command, may be its first step, set in transit
            setInTransit(true);
        }

        // perform a step for the current command
        if (currentCommand.step(simulationStep)) {
            // running command finished after this step, get a new command
            Command newCmd = commandList.poll();
            if (newCmd == null) {
                // no more commands for this drone
                currentCommand = null;
                return true;
            } else {
                // set the new command as the running command
                currentCommand = newCmd;
                setInTransit(false);
            }
        }
        // if we arrive here then there is a command waiting for a next step
        return false;
    }

    /**
     * Add a command to this drone's command list
     *
     * @param command the command to add
     */
    private void addCommand(Command command) {
        commandList.add(command);
    }

    /**
     * Unload the specified items from the drone
     *
     * @param productTypeId the id of the type of the product
     * @param productNum the number of products to store
     */
    public void unload(int productTypeId, int productNum) {
        if (!inventory.containsKey(productTypeId)) {
            throw new DroneActionException("The product with type " + productTypeId + " is not in the drone's inventory");
        }
        if (inventory.get(productTypeId) < productNum) {
            throw new DroneActionException("The quantity of the product with type " + productTypeId + " does not match"
                    + " the quantity in the drone's inventory");
        }
        currentWeight -= PRODUCT_WEIGHTS[productTypeId] * productNum;
        assert currentWeight >= 0 : "drone weight dropped below zero";
        inventory.compute(productTypeId, (k, v) -> {
            return (v - productNum) == 0 ? null : v - productNum;
        });
    }

    /**
     * load the specified items to the drone
     *
     * @param productTypeId the id of the type of the product
     * @param productNum the number of products to store
     */
    public void load(int productTypeId, int productNum) {
        currentWeight += PRODUCT_WEIGHTS[productTypeId] * productNum;
        inventory.merge(productTypeId, productNum, (k, v) -> {
            return v + productNum;
        });
        if (currentWeight > MAX_WEIGHT) {
            throw new DroneActionException("Drone weight exceeded maximum allowed weight");
        }
    }

    /**
     * Adds a load command to this drone
     *
     * @param warehouse the warehouse that this command will be performed
     * @param productTypeId the type id of the product to load to the drone
     * @param productNum how many products to load to the drone
     */
    public void addLoadCommand(Warehouse warehouse, int productTypeId, int productNum) {
        addCommand(new LoadCommand(this, warehouse, productTypeId, productNum));
    }

    /**
     * Adds a store command to this drone
     *
     * @param warehouse the warehouse that this command will be performed
     * @param productTypeId the type id of the product to store to the warehouse
     * @param productNum how many products to store to the warehouse
     */
    public void addStoreCommand(Warehouse warehouse, int productTypeId, int productNum) {
        addCommand(new StoreCommand(this, warehouse, productTypeId, productNum));
    }

    /**
     * Adds a deliver command to this drone
     *
     * @param order the order associated with this command
     * @param productTypeId the type id of the product to deliver
     * @param productNum how many products to deliver
     */
    public void addDeliverCommand(Order order, int productTypeId, int productNum) {
        addCommand(new DeliverCommand(this, order, productTypeId, productNum));
    }

    /**
     * Adds a wait command to this drone
     *
     * @param turns how many turns to wait
     */
    public void addWaitCommand(int turns) {
        addCommand(new WaitCommand(this, turns));
    }

    /**
     * Get the maximum allowed weight of each drone
     *
     * @return maximum allowed weight of each drone
     */
    public static int getMaxWeight() {
        return MAX_WEIGHT;
    }

    /**
     * Set the maximum allowed weight of each drone. This should be set before the simulation begins
     *
     * @param maxWeight maximum allowed weight of each drone
     */
    public static void setMaxWeight(int maxWeight) {
        MAX_WEIGHT = maxWeight;
    }

    /**
     *
     * @return the maximum allowed turns for a drone. This should be set before the simulation begins
     */
    public static int getMAX_TURNS() {
        return MAX_TURNS;
    }

    /**
     *
     * @param MAX_TURNS the maximum allowed turns for a drone. This should be set before the simulation begins
     */
    public static void setMAX_TURNS(int MAX_TURNS) {
        Drone.MAX_TURNS = MAX_TURNS;
    }

    /**
     *
     * @return an array with the product weights
     */
    public static int[] getProductWeights() {
        return PRODUCT_WEIGHTS;
    }

    /**
     * Set the array with the product weights. This should be set before the simulation begins
     *
     * @param productWeights an array with the product weights
     */
    public static void setProductWeights(int[] productWeights) {
        PRODUCT_WEIGHTS = productWeights;
    }

    /**
     *
     * @return the current weight of the drone based on its inventory
     */
    public int getCurrentWeight() {
        return currentWeight;
    }

    /**
     *
     * @return true if this drone does not carry anything
     */
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    /**
     *
     * @return this drone's inventory
     */
    public HashMap<Integer, Integer> getInventory() {
        return inventory;
    }

    /**
     *
     * @return the command list of this drone
     */
    public LinkedList<Command> getCommandList() {
        return commandList;
    }

    /**
     * The current position of the drone. <b>This is valid only if {@link #isInTransit() } returns false.</b>
     * When the drone is in transit it returns the last valid position
     *
     * @return the current position of the drone
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * A drone is in transit when it travels between its starting point and a command destination or a previous command
     * destination which becomes a new starting point and the next destination. {@link #getCurrentPosition() } returns
     * valid results only when the drone is not in transit. When it is in transit it returns the last valid position
     *
     * @param inTransit true if the drone is in transit
     */
    public void setInTransit(boolean inTransit) {
        this.inTransit = inTransit;
    }

    /**
     * A drone is in transit when it travels between its starting point and a command destination or a previous command
     * destination which becomes a new starting point and the next destination. {@link #getCurrentPosition() } returns
     * valid results only when the drone is not in transit. When it is in transit it returns the last valid position
     *
     * @return true if the drone is in transit
     */
    public boolean isInTransit() {
        return inTransit;
    }

    /**
     *
     * @return the number of turns this drone needs to complete all its commands
     */
    public int getTotalTurns() {
        return totalTurns;
    }

    /**
     *
     * @return the current command being executed by the drone or null if there is no such command
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }

    /**
     *
     * @return the steps needed to complete the current command or -1 if there is no current command
     */
    public int getRemainingCommandSteps() {
        if (currentCommand == null) {
            return -1;
        }
        return currentCommand.getTurnsRemaining();
    }

}
