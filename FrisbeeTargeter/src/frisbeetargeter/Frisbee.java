/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frisbeetargeter;

/**
 *
 * @author alex
 */
public class Frisbee {

    private static double x;
//The x position of the frisbee.
    private static double y;
//The y position of the frisbee.
    private static double vx;
//The x velocity of the frisbee.
    private static double vy;
//The y velocity of the frisbee.
    private static final double g = -9.81;
//The acceleration of gravity (m/s^2).
    private static final double m = 0.175;
//The mass of a standard frisbee in kilograms.
    private static final double RHO = 1.23;
//The density of air in kg/m^3.
    private static final double AREA = 0.0568;
//The area of a standard frisbee.
    private static final double CL0 = 0.1;
//The lift coefficient at alpha = 0.
    private static final double CLA = 1.4;
//The lift coefficient dependent on alpha.
    private static final double CD0 = 0.08;
//The drag coefficent at alpha = 0.
    private static final double CDA = 2.72;
//The drag coefficient dependent on alpha.
    private static final double ALPHA0 = -4;

    public static class Data {

        public double maxY = Double.NEGATIVE_INFINITY;
        public double maxX = Double.NEGATIVE_INFINITY;
        public double error = Double.POSITIVE_INFINITY;
        double closestApproach = Double.NEGATIVE_INFINITY;
    }

    /**
     * A method that uses Euler’s method to simulate the flight of a frisbee in
     * two dimensions, distance and height (x and y, respectively).
     *     
*/
    public static Data simulate(double y0, double vx0, double vy0, double alpha, double deltaT, double targetX, double targetY, double targetMinY) {
//Calculation of the lift coefficient using the relationship given
//by S. A. Hummel.
        System.out.println("beginning simulation");
        Data out = new Data();
        double cl = CL0 + CLA * alpha * Math.PI / 180;
//Calculation of the drag coefficient (for Prantl’s relationship)
//using the relationship given by S. A. Hummel.
        double cd = CD0 + CDA * Math.pow((alpha - ALPHA0) , 2);
//Initial position x = 0.
        x = 0;
//Initial position y = y0.
        y = y0;
//Initial x velocity vx = vx0.
        vx = vx0;
//Initial y velocity vy = vy0.
        vy = vy0;
        int k = 0;
//A while loop that performs iterations until the y position
//reaches zero (i.e. the frisbee hits the ground).
        while (y > 0 && x < targetX) {
//The change in velocity in the y direction obtained setting the
//net force equal to the sum of the gravitational force and the
//lift force and solving for delta v.
            double deltavy = (RHO * Math.pow(vx, 2) * AREA * cl / 2 / m + g) * deltaT;
//The change in velocity in the x direction, obtained by
//solving the force equation for delta v. (The only force
//present is the drag force).
            double deltavx = -RHO * Math.pow(vx, 2) * AREA * cd * deltaT;
//The new positions and velocities are calculated using
//simple introductory mechanics.
            vx = vx + deltavx;
            vy = vy + deltavy;
            x = x + vx * deltaT;
            y = y + vy * deltaT;
            k++;
            if (y > out.maxY) {
                out.maxY = y;
            }
            if (x > out.maxX) {
                out.maxX = x;
            }
//            if (k % 20 == 0) {
//                System.out.println("simulation step: "+k+"frisbee at ("+x+", "+y+")");
//            }
        }
        out.error = y - targetY;
        return out;
    }
}
