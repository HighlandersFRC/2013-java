/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frisbeetargeter;

import java.util.Scanner;

/**
 *
 * @author alex
 */
public class FrisbeeTargeter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        double minAngle = s.nextDouble();
        double maxAngle = s.nextDouble();
        double precision = s.nextDouble();
        double targetX = s.nextDouble();
        double targetY = s.nextDouble();
        double targetMinY = s.nextDouble();
        if (runSim(maxAngle, targetX, targetY, targetMinY).maxY < targetMinY) {
            System.out.println("no such shot possible");
            return;
        }
        double stepAngle = (maxAngle - minAngle) / 2;
        double angle = minAngle + stepAngle;
        double error = runSim(angle, targetX, targetY, targetMinY).error;
        for (int n = 0; n < 8; n++) {
            System.out.println("angle: " + angle);
            System.out.println("error: " + error);
            stepAngle = stepAngle / 2;
            double upAngle = angle + stepAngle;
            double downAngle = angle - stepAngle;
            double absError = Math.abs(error);
            double upError = runSim(upAngle, targetX, targetY, targetMinY).error;
            System.out.println("upError: "+upError);
            double upAbsError = Math.abs(upError);
            double downError = runSim(downAngle, targetX, targetY, targetMinY).error;
            System.out.println("downError: "+downError);
            double downAbsError = Math.abs(downError);
            if (upAbsError > absError && downAbsError < absError) {
                error = downError;
                angle = downAngle;
            } else if (downAbsError > absError && upAbsError < absError) {
                error = upError;
                angle = upAngle;
            } else {
                double deltaUpError = error - upError;
                double deltaDownError = error - downError;
                if (deltaDownError <= 0) {
                    error = downError;
                    angle = downAngle;
                } else if (deltaUpError > deltaDownError) {
                    error = downError;
                    angle = downAngle;
                } else {
                    error = upError;
                    angle = upAngle;
                }
            }
        }
        if (Math.abs(error) > precision) {
            System.out.println("no shot possible with acceptable error.");
        }
        System.out.println("best shot was: [angle:" + angle + ", error:" + error + "]");
    }

    public static Frisbee.Data runSim(double angle, double targetX, double targetY, double targetMinY) {
        return Frisbee.simulate(1, 5 * Math.cos(angle), 5 * Math.sin(angle), angle, 0.001, targetX, targetY, targetMinY);
    }
}
