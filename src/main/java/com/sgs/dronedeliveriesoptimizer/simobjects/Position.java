package com.sgs.dronedeliveriesoptimizer.simobjects;

/**
 *
 * @author George Mantakos
 */
public class Position {

    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getDistance(Position otherPosition) {
        int ra = row;
        int rb = otherPosition.getRow();
        int ca = col;
        int cb = otherPosition.getCol();

        return (int) Math.ceil(Math.sqrt((Math.pow(ra - rb, 2) + Math.pow(ca - cb, 2))));
    }

}
