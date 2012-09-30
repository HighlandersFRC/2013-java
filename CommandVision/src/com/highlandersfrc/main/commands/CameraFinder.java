package com.highlandersfrc.main.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author David
 */
public class CameraFinder extends CommandBase {
    
    public static final int DEAD_ZONE = 15;
    
    private double percent;
    
    CriteriaCollection cc;
    
    public CameraFinder() {
        requires(camera);
        requires(chassis);
    }

    protected void initialize() {
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
    }

    protected void execute() {
        try {
            ColorImage img = camera.getImage();
            BinaryImage thresholdImg = img.thresholdRGB(25, 255, 0, 45, 0, 100);
            BinaryImage bigsImg = thresholdImg.removeSmallObjects(false, 2);
            BinaryImage convexHullImg = bigsImg.convexHull(false);
            BinaryImage filteredImg = convexHullImg.particleFilter(cc);
            ParticleAnalysisReport[] reports = filteredImg.getOrderedParticleAnalysisReports();
            for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                //System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x + " Center of mass y: " + r.center_mass_y + " width " + r.boundingRectWidth + " height " + r.boundingRectHeight);
            }
            //System.out.println(filteredImg.getNumberParticles() + "  " + Timer.getFPGATimestamp());
            if (filteredImg.getNumberParticles() >= 1) {
                if (percent == 0) {
                    percent = reports[0].particleToImagePercent;
                }
                double offset = reports[0].center_mass_x - (img.getWidth() / 2);
                double forwardMovement = (percent - reports[0].particleToImagePercent) / 75;
                System.out.println(forwardMovement);
                //System.out.println("Percent: " + reports[0].particleToImagePercent);
                chassis.drive((-offset/400)+forwardMovement, (offset/400)+forwardMovement);
                //System.out.println(offset/400);
//                if (reports[0].center_mass_x > (img.getWidth() / 2 + DEAD_ZONE)) {
//                    chassis.drive(-offset/100, offset/100);
//                    //chassis.drive(-0.15, 0.15);
//                } else if (reports[0].center_mass_x < (img.getWidth() / 2 - DEAD_ZONE)) {
//                    
//                    //chassis.drive(0.15, -0.15);
//                } else {
//                    chassis.drive(0.0, 0.0);
//                }
                
            } else {
                chassis.drive(0.0, 0.0);
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
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
