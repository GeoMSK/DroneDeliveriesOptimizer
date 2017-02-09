package com.sgs.dronedeliveriesoptimizer.exceptions;

/**
 * This exception should be thrown when a drone performs an action during simulation that results to a fatal error and
 * the simulation is forced to stop
 *
 * @author George Mantakos
 */
public class DroneActionException extends RuntimeException {

    public DroneActionException() {
    }

    public DroneActionException(String message) {
        super(message);
    }

    public DroneActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DroneActionException(Throwable cause) {
        super(cause);
    }

    public DroneActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
