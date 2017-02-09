package com.sgs.dronedeliveriesoptimizer;

import com.sgs.dronedeliveriesoptimizer.commands.Command;
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
    private int currentWeight;
    private final HashMap<Integer, Integer> inventory;
    private final LinkedList<Command> commandList;
    private Command currentCommand;

    public Drone() {
        this.inventory = new HashMap<>();
        this.commandList = new LinkedList<>();
    }

    /**
     * Executes a simulation step for this drone. Executes a simulation step for the current running command until there
     * are no commands left.
     *
     * @return true if there are no more commands to execute, false otherwise
     */
    public boolean step() {
        if (currentCommand == null && !commandList.isEmpty()) {
            // no running command, get one from the command list
            currentCommand = commandList.pop();
        } else if (currentCommand == null){
            // there are no commands in the command list for this drone
            return true;
        }

        // perform a step for the current command
        if (currentCommand.step()) {
            // running command finished after this step, get a new command
            Command newCmd = commandList.poll();
            if (newCmd == null) {
                // no more commands for this drone
                currentCommand = null;
                return true;
            } else {
                // set the new command as the running command
                currentCommand = newCmd;
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
    public void addCommand(Command command) {
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

}
