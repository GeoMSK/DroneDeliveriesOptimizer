package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;

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
     */
    public Command(Drone drone) {
        this.drone = drone;
    }

    /**
     * This function should set the remaining steps for this command and increase the total turns of the associated
     * drone. Call this in the constructors of the subclass
     */
    protected abstract void setRemainingTurns();

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
     *
     * @return the position of the drone after this command is finished
     */
    public abstract Position getResultingPosition();

    /**
     * This function will be called when the command reaches the last step
     */
    protected abstract void performAction();

    /**
     *
     * @return the turns needed to complete the current command
     */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
