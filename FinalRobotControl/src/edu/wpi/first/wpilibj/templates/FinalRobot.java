/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class FinalRobot extends IterativeRobot {

    Gyro gyro = new Gyro(1);
    boolean driver = true;
    boolean driveLock = false;
    double gunnerX;
    double gunnerY;
    double heading;
    LatchedBoolean absCtrlMode = new LatchedBoolean();
    RobotDrive drive = new RobotDrive(1, 2, 4, 3);
    Victor arm = new Victor(5);
    Victor arm2 = new Victor(6);
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Victor launch = new Victor(7);
    Victor injector = new Victor(8);
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

    public void robotInit() {
        drive.setSafetyEnabled(false);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        System.out.println("init");
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
        SmartDashboard.putNumber("Shoulder Power", 50);
        SmartDashboard.putNumber("Belt Power", 85);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        drive.setLeftRightMotorOutputs(0.1, 0.1);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
//        System.out.println("Joystick: (" + joy1.getX()+", "+joy1.getY()+", "+joy2.getX()+")");
//        System.out.println(accel.getAccelerations());
//        System.out.println("test");
//        System.out.println("(" + accel.getAcceleration(Axes.kX) + ", " + accel.getAcceleration(Axes.kY) + ", " + accel.getAcceleration(Axes.kZ) + ", " + gyro.getAngle() + ")");

        if (!driveLock && joy1.getRawButton(9)) {
//            System.out.println(driver);
            driver = !driver;
            driveLock = true;
//            System.out.println("driver: "+driver+" "+!driver);
            if (driver == false) {
                heading = gyro.getAngle();
            }
//            System.out.println("toggle: " + driver);
        } else if (driveLock && !joy1.getRawButton(9)) {
            driveLock = false;
        }
        if (driver) {
//            System.out.println("trigger");
            absCtrlMode.set(joy1.getRawButton(8));
            drive.mecanumDrive_Cartesian(joy2.getX(), joy2.getY(), joy1.getX(), absCtrlMode.getValue() ? gyro.getAngle() : 0);
        } else {
            if (joy2.getRawButton(4) && !joy2.getRawButton(5)) {
                gunnerX = -0.3;
            } else if (joy2.getRawButton(5) && !joy2.getRawButton(4)) {
                gunnerX = 0.3;
            } else {
                gunnerX = 0;
            }
            if (joy2.getRawButton(2) && !joy2.getRawButton(3)) {
                gunnerY = 0.3;
            } else if (joy2.getRawButton(3) && !joy2.getRawButton(2)) {
                gunnerY = -0.3;
            } else {
                gunnerY = 0;
            }
            heading += joy2.getAxis(Joystick.AxisType.kX);
            drive.mecanumDrive_Cartesian(gunnerX, gunnerY, (-gyro.getAngle() + heading) / 20, 0);
        }
        if (joy2.getRawButton(11)) {
            arm.set(-SmartDashboard.getNumber("Climber Power") / 100);
        } else if (joy2.getRawButton(10)) {
            arm.set(SmartDashboard.getNumber("Climber Power") / 100);
        } else {
            arm.set(0);
        }
        if (joy2.getRawButton(6)) {
            arm2.set(SmartDashboard.getNumber("Shoulder Power") / 100);
        } else if (joy2.getRawButton(7)) {
            arm2.set(-SmartDashboard.getNumber("Shoulder Power") / 100);
        } else {
            arm2.set(0);
        }
        if (!fireControl) {
            launchPwr = -SmartDashboard.getNumber("Launch Power") / 100;
            injPwr = -SmartDashboard.getNumber("Injector Power") / 100;
            if (joy1.getRawButton(7)) {
                feed.set(DoubleSolenoid.Value.kReverse);
            } else if(joy1.getRawButton(6)) {
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
        if (joy1.getRawButton(10)) {
            comp.stop();
        }
        if (joy2.getRawButton(11)) {
            comp.start();
        }
        if (joy1.getRawButton(1) && !fireControl) {
            System.out.println("fireStart");
            injPulsePwr = -SmartDashboard.getNumber("Injector Pulse Power")/100;
            launchPulsePwr = -SmartDashboard.getNumber("Launcher Pulse Power")/100;
            injPulseLen = SmartDashboard.getNumber("Injector Pulse Length");
            launchPulseLen = SmartDashboard.getNumber("Launcher Pulse Length");
            injPulseDel = SmartDashboard.getNumber("Injector Pulse Delay");
            launchPulseDel = SmartDashboard.getNumber("Launcher Pulse Delay");
            feedTime = SmartDashboard.getNumber("Piston Extension Time");
            defaultPistonState = SmartDashboard.getBoolean("Piston Default Extended");
            feed.set(DoubleSolenoid.Value.kReverse);
            fireControl = true;
            fireState = 0;
            startTime = Timer.getFPGATimestamp()+(defaultPistonState?feedTime:0);
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
                    feed.set(defaultPistonState?DoubleSolenoid.Value.kOff:DoubleSolenoid.Value.kReverse);
            }
            if (fireTime >= Math.max(Math.max(injPulseDel + injPulseLen, launchPulseDel + launchPulseLen),feedTime+0.1)) {
                if (!joy1.getRawButton(1)) {
                    fireControl = false;
                    feed.set(DoubleSolenoid.Value.kOff);
                }
            }
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
