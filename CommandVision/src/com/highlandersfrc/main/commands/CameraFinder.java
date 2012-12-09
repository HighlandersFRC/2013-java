package com.highlandersfrc.main.commands;

import com.sun.cldc.jna.Pointer;
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
    
    private double percent;
    private CriteriaCollection cc;
    
    public CameraFinder() {
        //requires(camera);
        //requires(chassis);
    }

    protected void initialize() {
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
    }

    protected void execute() {
//        try {
//            ColorImage img = camera.getImage();
//            
//            //frcGetPixelValue
//            //0, 59, 96
//            //349, 73, 59
//            BinaryImage thresholdImg = img.thresholdRGB(230, 255, 40, 120, 30, 100);
//            //BinaryImage thresholdImg = img.thresholdHSV(300, 360, 30, 80, 50, 90);
//            
//            //BinaryImage thresholdImg = img.thresholdRGB(220, 255, 40, 80, 80, 100);
//            BinaryImage bigsImg = thresholdImg.removeSmallObjects(false, 2);
//            BinaryImage convexHullImg = bigsImg.convexHull(false);
//            BinaryImage filteredImg = convexHullImg.particleFilter(cc);
//            ParticleAnalysisReport[] reports = filteredImg.getOrderedParticleAnalysisReports();
//            System.out.println(filteredImg.getNumberParticles() + " @ " + Timer.getFPGATimestamp());
//            if (filteredImg.getNumberParticles() >= 1) {
//                if (percent == 0) {
//                    percent = reports[0].particleToImagePercent;
//                }
//                double offset = reports[0].center_mass_x - (img.getWidth() / 2);
//                
//                System.out.println(reports[0].center_mass_x + ", " + reports[0].center_mass_y);
//                
//                //double forwardMovement = (percent - reports[0].particleToImagePercent) / -50;
//                //System.out.println(forwardMovement);
//                System.out.println("Percent: " + reports[0].particleToImagePercent);
//                System.out.println(offset);
//                //chassis.drive((-offset / 200), (offset / 200));
//                System.out.println(offset/200);
//            } else {
//                chassis.drive(0.0, 0.0);
//            }
//            filteredImg.free();
//            convexHullImg.free();
//            bigsImg.free();
//            thresholdImg.free();
//            img.free();
//            Timer.delay(0.05);
//        } catch (AxisCameraException ex) {
//            ex.printStackTrace();
//        } catch (NIVisionException ex) {
//            ex.printStackTrace();
//        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
