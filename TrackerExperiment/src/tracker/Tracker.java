/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package tracker;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Tracker extends IterativeRobot {

    AxisCamera cam = AxisCamera.getInstance();
    private CriteriaCollection cc;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        SmartDashboard.putNumber("top", 0);
        SmartDashboard.putNumber("bottom", 0);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        try {
            ColorImage img = cam.getImage();

            //frcGetPixelValue
            //0, 59, 96
            //349, 73, 59
            BinaryImage thresholdImg = img.thresholdRGB(127, 255, 230, 255, 230, 255);
//            BinaryImage thresholdImg = img.thresholdHSV(300, 360, 30, 80, 50, 90);

            //BinaryImage thresholdImg = img.thresholdRGB(220, 255, 40, 80, 80, 100);
            BinaryImage bigsImg = thresholdImg.removeSmallObjects(false, 2);
            BinaryImage convexHullImg = bigsImg.convexHull(false);
            BinaryImage filteredImg = convexHullImg.particleFilter(cc);
            ParticleAnalysisReport[] reports = filteredImg.getOrderedParticleAnalysisReports();
            System.out.println(filteredImg.getNumberParticles() + " @ " + Timer.getFPGATimestamp());
            if (filteredImg.getNumberParticles() >= 1) {
//                if (percent == 0) {
//                    percent = reports[0].particleToImagePercent;
//                }
                double offset = reports[0].center_mass_x - (img.getWidth() / 2);
                SmartDashboard.putNumber("Center of mass x", (reports[0].center_mass_x_normalized + 1) * 50);
                SmartDashboard.putNumber("Center of mass y", (reports[0].center_mass_y_normalized + 1) * 50);
                System.out.println(reports[0].center_mass_x + ", " + reports[0].center_mass_y);
                double tanTheta = Math.tan(-reports[0].boundingRectTop * 0.0025452718 + reports[0].boundingRectHeight * 0.0025452718 + 0.6108652382);
                double tanPhi = Math.tan(-reports[0].boundingRectTop * 0.0025452718 + 0.6108652382);
                SmartDashboard.putNumber("Distance", -32 / (tanPhi - tanTheta));
                SmartDashboard.putNumber("Height", -(16 * tanTheta + 16 * tanPhi) / (tanTheta - tanPhi));
                SmartDashboard.putNumber("Phi", -reports[0].boundingRectTop * 0.0025452718 + 0.6108652382);
                SmartDashboard.putNumber("Theta", -reports[0].boundingRectTop * 0.0025452718 + reports[0].boundingRectHeight * 0.0025452718 + 0.6108652382);
                //double forwardMovement = (percent - reports[0].particleToImagePercent) / -50;
                //System.out.println(forwardMovement);
                SmartDashboard.putNumber("ParticleToImagePercent", reports[0].particleToImagePercent);
                System.out.println("Percent: " + reports[0].particleToImagePercent);
                System.out.println(offset);
                //chassis.drive((-offset / 200), (offset / 200));
                System.out.println(offset / 200);
            } else {
//                chassis.drive(0.0, 0.0);
            }
            filteredImg.free();
            convexHullImg.free();
            bigsImg.free();
            thresholdImg.free();
            img.free();
            Timer.delay(0.05);
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
        if (SmartDashboard.getBoolean("Checkbox 1")) {
            double tanTheta = Math.tan(-SmartDashboard.getNumber("bottom") * 0.0025452718 + 0.6108652382);
            double tanPhi = Math.tan(-SmartDashboard.getNumber("top") * 0.0025452718 + 0.6108652382);
            SmartDashboard.putNumber("Distance", -32 / (tanPhi - tanTheta));
            SmartDashboard.putNumber("Height", -(16 * tanTheta + 16 * tanPhi) / (tanTheta - tanPhi));
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
