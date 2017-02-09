package com.sgs.dronedeliveriesoptimizer.commands;

import com.sgs.dronedeliveriesoptimizer.Drone;

/**
 *
 * @author George Mantakos
 */
public class WaitCommand extends Command{
    

    public WaitCommand(Drone drone, int turnsRemaining) {
        super(drone, turnsRemaining);
    }

    @Override
    protected void performAction() {
        // nothing to be done
    }
    
}
