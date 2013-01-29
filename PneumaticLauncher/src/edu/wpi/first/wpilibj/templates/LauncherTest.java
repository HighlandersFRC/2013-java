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

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class LauncherTest extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    Compressor comp = new Compressor(1,1);
    DoubleSolenoid launch = new DoubleSolenoid(1,2);
    Joystick joy1 = new Joystick(1);
    public void robotInit() {
        comp.start();
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
        if (joy1.getRawButton(1)) {
            launch.set(DoubleSolenoid.Value.kForward);
            Timer.delay(1);
            launch.set(DoubleSolenoid.Value.kOff);
            Timer.delay(1);
            launch.set(DoubleSolenoid.Value.kReverse);
            Timer.delay(1);
            launch.set(DoubleSolenoid.Value.kOff);
        } 
        if (joy1.getRawButton(2)) {
            comp.stop();
        }
        if (joy1.getRawButton(3)) {
            comp.start();
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
