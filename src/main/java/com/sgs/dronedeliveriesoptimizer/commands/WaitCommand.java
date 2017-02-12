package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.simobjects.Drone;
import com.sgs.dronedeliveriesoptimizer.simobjects.Position;

/**
 *
 * @author George Mantakos
 */
public class WaitCommand extends Command {

    private final int turns;

    /**
     *
     * @param drone the drone to perform this command
     * @param turns how many turns to wait
     */
    public WaitCommand(Drone drone, int turns) {
        super(drone);
        this.turns = turns;
        setRemainingTurns();
    }

    @Override
    protected final void setRemainingTurns() {
        this.turnsRemaining = turns;
        drone.increaseTotalTurns(this.turnsRemaining);
    }

    @Override
    protected void performAction() {
        // nothing to be done
    }

    @Override
    public Position getResultingPosition() {
        return drone.getCurrentPosition();
    }

}
