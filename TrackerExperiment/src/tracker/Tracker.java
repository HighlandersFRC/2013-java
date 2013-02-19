package tracker;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
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

    private static final double FOCAL_LENGTH = 240.0 / Math.tan((47.0 / 2.0) / 180.0 * Math.PI);
    private static final double CAMERA_ANGLE = 15.0;
    private AxisCamera camera = AxisCamera.getInstance();
    private CriteriaCollection cc;
    private Gyro g = new Gyro(2);

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        SmartDashboard.putNumber("Red Min: ", 0);
        SmartDashboard.putNumber("Red Max: ", 100);
        SmartDashboard.putNumber("Green Min: ", 230);
        SmartDashboard.putNumber("Green Max: ", 255);
        SmartDashboard.putNumber("Blue Min: ", 100);
        SmartDashboard.putNumber("Blue Max: ", 255);
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
            int rMin = (int) SmartDashboard.getNumber("Red Min: ", 0);
            int rMax = (int) SmartDashboard.getNumber("Red Max: ", 100);
            int gMin = (int) SmartDashboard.getNumber("Green Min: ", 230);
            int gMax = (int) SmartDashboard.getNumber("Green Max: ", 255);
            int bMin = (int) SmartDashboard.getNumber("Blue Min: ", 100);
            int bMax = (int) SmartDashboard.getNumber("Blue Max: ", 255);
            ColorImage img = camera.getImage();
            BinaryImage thresholdImg = img.thresholdRGB(rMin, rMax, gMin, gMax, bMin, bMax);
            BinaryImage bigsImg = thresholdImg.removeSmallObjects(false, 2);
            BinaryImage convexHullImg = bigsImg.convexHull(false);
            BinaryImage filteredImg = convexHullImg.particleFilter(cc);
            ParticleAnalysisReport[] reports = filteredImg.getOrderedParticleAnalysisReports();
            if (filteredImg.getNumberParticles() >= 1) {
                ParticleAnalysisReport report = reports[0];
                int rectUpper = 240 - report.boundingRectTop;
                int rectLower = 240 - (report.boundingRectTop + report.boundingRectHeight);
                double phi = MathUtils.atan(rectUpper / FOCAL_LENGTH);
                double theta = MathUtils.atan(rectLower / FOCAL_LENGTH);
                SmartDashboard.putNumber("Rect Upper: ", rectUpper);
                SmartDashboard.putNumber("Rect Lower: ", rectLower);
                SmartDashboard.putNumber("Phi: ", phi / Math.PI * 180.0);
                SmartDashboard.putNumber("Theta: ", theta / Math.PI * 180.0);
                double tanTheta = Math.tan(theta);
                double tanPhi = Math.tan(phi);
                SmartDashboard.putNumber("Distance", -32 / (tanPhi - tanTheta));
                SmartDashboard.putNumber("Height", -(16 * tanTheta + 16 * tanPhi) / (tanTheta - tanPhi));
                //chassis.drive((-offset / 200), (offset / 200));
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
        SmartDashboard.putNumber("Angle: ", g.getAngle());
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
