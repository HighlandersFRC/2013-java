/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.Arrays;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
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
public class ImageTargeter extends IterativeRobot {

    private AxisCamera camera = AxisCamera.getInstance();
    private CriteriaCollection cc;
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Victor launch = new Victor(5);
    Victor injector = new Victor(6);
    Compressor comp = new Compressor(1, 1);
    DoubleSolenoid feed = new DoubleSolenoid(1, 2);
    boolean wheel = false;
    LatchedBoolean hoppercontrol = new LatchedBoolean();
    boolean fireControl = false;
    int fireState;
    double injPwr;
    double launchPwr;
    double injPulsePwr;
    double launchPulsePwr;
    double injPulseLen;
    double launchPulseLen;
    double injPulseDel;
    double launchPulseDel;
    double feedTime;
    double startTime;
    boolean defaultPistonState;
    int[] dropFactors = {0, 100};
    double[] partPercents = {100, 10};

    public void robotInit() {
        SmartDashboard.putNumber("Launch Power", 60);
        SmartDashboard.putNumber("Injector Power", 45);
        SmartDashboard.putNumber("Injector Pulse Delay", 0);
        SmartDashboard.putNumber("Launcher Pulse Delay", 0);
        SmartDashboard.putNumber("Injector Pulse Power", 100);
        SmartDashboard.putNumber("Launcher Pulse Power", 100);
        SmartDashboard.putNumber("Injector Pulse Length", 0.25);
        SmartDashboard.putNumber("Launcher Pulse Length", 0.25);
        SmartDashboard.putNumber("Piston Extension Time", 0.75);
        SmartDashboard.putBoolean("Piston Default Extended", false);
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedFlour1);
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
//        System.out.println("teleop");
//        System.out.println(joy1.getRawButton(1));
        System.out.println(camera.getWhiteBalance());
        if (!fireControl) {
            launchPwr = -SmartDashboard.getNumber("Launch Power") / 100;
            injPwr = -SmartDashboard.getNumber("Injector Power") / 100;
            if (joy1.getRawButton(7)) {
                feed.set(DoubleSolenoid.Value.kReverse);
            } else if (joy1.getRawButton(6)) {
                feed.set(DoubleSolenoid.Value.kForward);
            } else {
                feed.set(DoubleSolenoid.Value.kOff);
            }
        }
        if (joy1.getRawButton(3)) {
            wheel = true;
        } else if (joy1.getRawButton(2)) {
            wheel = false;
        }
        if (wheel && !fireControl) {
            launch.set(launchPwr);
            injector.set(injPwr);
        } else if (!fireControl) {
            launch.set(0);
            injector.set(0);
        }



        if (joy2.getRawButton(2)) {
            comp.stop();
        }
        if (joy2.getRawButton(3)) {
            comp.start();
        }
        if (joy1.getRawButton(1) && !fireControl) {
            System.out.println("fireStart");
            injPulsePwr = -SmartDashboard.getNumber("Injector Pulse Power") / 100;
            launchPulsePwr = -SmartDashboard.getNumber("Launcher Pulse Power") / 100;
            injPulseLen = SmartDashboard.getNumber("Injector Pulse Length");
            launchPulseLen = SmartDashboard.getNumber("Launcher Pulse Length");
            injPulseDel = SmartDashboard.getNumber("Injector Pulse Delay");
            launchPulseDel = SmartDashboard.getNumber("Launcher Pulse Delay");
            feedTime = SmartDashboard.getNumber("Piston Extension Time");
            defaultPistonState = SmartDashboard.getBoolean("Piston Default Extended");
            feed.set(DoubleSolenoid.Value.kReverse);
            fireControl = true;
            fireState = 0;
            startTime = Timer.getFPGATimestamp() + (defaultPistonState ? feedTime : 0);
        }
        if (fireControl) {
            System.out.println("firing");
            double currTime = Timer.getFPGATimestamp();
            double fireTime = currTime - startTime;
            if (fireTime >= 0) {
                feed.set(DoubleSolenoid.Value.kForward);
            }
            if (fireTime >= injPulseDel && fireTime < injPulseDel + injPulseLen) {
                injector.set(injPulsePwr);
            }
            if (fireTime >= launchPulseDel && fireTime < launchPulseDel + launchPulseLen) {
                launch.set(launchPulsePwr);
            }
            if (fireTime >= injPulseDel + injPulseLen) {
                injector.set(injPwr);
            }
            if (fireTime >= launchPulseDel + launchPulseLen) {
                launch.set(launchPwr);
            }
            if (fireTime >= feedTime) {
                feed.set(defaultPistonState ? DoubleSolenoid.Value.kOff : DoubleSolenoid.Value.kReverse);
            }
            if (fireTime >= Math.max(Math.max(injPulseDel + injPulseLen, launchPulseDel + launchPulseLen), feedTime + 0.1)) {
                if (!joy1.getRawButton(1)) {
                    fireControl = false;
                    feed.set(DoubleSolenoid.Value.kOff);
                }
            }
        }
        if (!fireControl) {
            try {
                ColorImage img = camera.getImage();
                BinaryImage thresholdImg = img.thresholdRGB(0, 100, 230, 255, 230, 255);
                BinaryImage bigsImg = thresholdImg.removeSmallObjects(false, 2);
                BinaryImage convexHullImg = bigsImg.convexHull(false);
                BinaryImage filteredImg = convexHullImg.particleFilter(cc);
                ParticleAnalysisReport[] reports = filteredImg.getOrderedParticleAnalysisReports();
                if (filteredImg.getNumberParticles() >= 1) {
                    ParticleAnalysisReport report = reports[0];
                    //double offset = reports[0].center_mass_x - (img.getWidth() / 2);
                    int loc = Arrays.binarySearch(partPercents, report.particleToImagePercent);
                    int dropFactor = 0;
                    if (loc > 0) {
                        dropFactor = dropFactors[loc];
                    } else if (loc == -1) {
                        dropFactor = lInterp(report.particleToImagePercent, partPercents[0], dropFactors[0], partPercents[1], dropFactors[1]);
                    } else if (loc == (-dropFactors.length - 1)) {
                        dropFactor = lInterp(report.particleToImagePercent, partPercents[partPercents.length - 2], dropFactors[dropFactors.length - 2], partPercents[partPercents.length - 2], dropFactors[dropFactors.length - 2]);
                    } else {
                        dropFactor = lInterp(report.particleToImagePercent, partPercents[-(loc + 1)], dropFactors[-(loc + 1)], partPercents[-(loc + 2)], dropFactors[-(loc + 2)]);
                    }
                    SmartDashboard.putNumber("required Adjustment", dropFactor - report.boundingRectTop);
                    int rectUpper = 240 - report.boundingRectTop;
                    int rectLower = 240 - (report.boundingRectTop - report.boundingRectHeight);
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
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }

    private int lInterp(double x, double x0, int y0, double x1, int y1) {
        return (int) MathUtils.round((x - x0) * (((double) (y0 - y1)) / (x0 - x1)) + y0);
    }
}
