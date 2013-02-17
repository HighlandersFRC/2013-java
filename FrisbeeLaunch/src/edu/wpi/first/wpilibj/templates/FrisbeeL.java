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

    public void robotInit() {
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
        if (joy1.getRawButton(7)) {
            wheel = true;
        } else {
            wheel = false;
        }
        if (wheel == true) {
            launch.set(SmartDashboard.getNumber("Launch power") / 100);
            injector.set(SmartDashboard.getNumber("Injector Power")/100);
        } else {
            launch.set(0);
            injector.set(0);
        }



        if (joy2.getRawButton(2)) {
            comp.stop();
        }
        if (joy2.getRawButton(3)) {
            comp.start();
        }
        hoppercontrol.set(joy2.getRawButton(1));
        if (hoppercontrol.getValue()) {
            feed.set(DoubleSolenoid.Value.kForward);
        }
        else {
            feed.set(DoubleSolenoid.Value.kReverse);
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
