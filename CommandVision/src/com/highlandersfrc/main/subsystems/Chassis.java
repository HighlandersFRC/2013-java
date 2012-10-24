package com.highlandersfrc.main.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author David
 */
public class Chassis extends Subsystem {
    
    double startTime = 0;
    boolean turbo = false;
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
    
    public void arcadeDrive(Joystick stick) {
        drive.arcadeDrive(stick);
    }
    
    public void tankDrive(Joystick left, Joystick right) {
        if (left.getRawButton(1))
        {
            if (turbo == false)
            {
                startTime = Timer.getFPGATimestamp();
                turbo = true;
            }
            else if (Timer.getFPGATimestamp() - startTime < 0.25)
            {
                drive.tankDrive(left.getY()/(2 - 4*(Timer.getFPGATimestamp()-startTime)), right.getY()/(2-4*(Timer.getFPGATimestamp()-startTime)));
                //System.out.println("start: " + startTime + " time: " + Timer.getFPGATimestamp() + " scale: " + (2 - 4*(startTime - Timer.getFPGATimestamp())));
            }
            else
            {
                drive.tankDrive(left, right);
            }
        }
        else
        {
            turbo = false;
            drive.tankDrive(left.getY()/2, right.getY()/2);
        }
    }
}
