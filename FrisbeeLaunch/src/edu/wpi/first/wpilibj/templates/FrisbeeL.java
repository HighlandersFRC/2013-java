/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
public class FrisbeeL extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
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



        if (joy2.getRawButton(2)) {
            comp.stop();
        }
        if (joy2.getRawButton(3)) {
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
            defaultPistonState = SmartDashboard.getBoolean("Piston Default State");
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
//        Timer.delay(0.1);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
