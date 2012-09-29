package com.highlandersfrc.main.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author David
 */
public class Chassis extends Subsystem {
    
    RobotDrive drive;

    public void initDefaultCommand() {
    }
    
    public Chassis() {
        drive = new RobotDrive(1, 2, 3, 4);
        drive.setSafetyEnabled(true);
        drive.setExpiration(10.0);
    }
    
    public void drive(double left, double right) {
        drive.setLeftRightMotorOutputs(left, right);
    }
    
    public void driveWithJoystick(Joystick stick) {
        drive.arcadeDrive(stick);
    }
}
