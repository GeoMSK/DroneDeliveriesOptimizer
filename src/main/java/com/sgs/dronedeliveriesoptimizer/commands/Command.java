package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;

/**
 *
 * @author George Mantakos
 */
public abstract class Command {

    protected final Drone drone;
    protected int turnsRemaining;
    protected int simulationStep;

    /**
     *
     * @param drone the drone to perform this command
     * @param turns how many turns are needed for this command to finish
     */
    public Command(Drone drone, int turns) {
        this.drone = drone;
        this.turnsRemaining = turns;
    }

    /**
     * Executes a simulation step for this command, if this command if finishes during this step 
     * {@link #performAction() } is called
     *
     * @param simulationStep the number of this step from the beginning of the simulation
     * @return true if this command finishes, false if more steps are needed
     */
    public boolean step(int simulationStep) {
        this.simulationStep = simulationStep;
        if (--turnsRemaining == 0) {
            performAction();
            return true;
        }
        return false;
    }

    /**
     * This function will be called when the command reaches the last step
     */
    protected abstract void performAction();
}
