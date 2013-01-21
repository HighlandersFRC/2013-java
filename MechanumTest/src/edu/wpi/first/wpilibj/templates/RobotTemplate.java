/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXL345_I2C.*;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    RobotDrive drive = new RobotDrive(1, 2, 4, 3);
//    ADXL345_I2C accel = new ADXL345_I2C(1, DataFormat_Range.k2G);
//    Gyro gyro = new Gyro(1);

    public void robotInit() {
        drive.setSafetyEnabled(false);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        System.out.println("init");
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
        System.out.println("Joystick: (" + joy1.getX()+", "+joy1.getY()+", "+joy2.getX()+")");
//        System.out.println(accel.getAccelerations());
//        System.out.println("test");
//        System.out.println("(" + accel.getAcceleration(Axes.kX) + ", " + accel.getAcceleration(Axes.kY) + ", " + accel.getAcceleration(Axes.kZ) + ", " + gyro.getAngle() + ")");
        if (joy1.getButton(Joystick.ButtonType.kTrigger)) {
//            System.out.println("trigger");
            if (!(joy1.getRawButton(7) || joy1.getRawButton(6))) {
                drive.mecanumDrive_Cartesian(joy1.getX(), joy1.getY(), joy2.getX(), 0);
            } else if (joy1.getRawButton(6)) {
                drive.mecanumDrive_Cartesian(1, 0, 0, 0);
            } else if (joy1.getRawButton(7)) {
                drive.mecanumDrive_Cartesian(-1, 0, 0, 0);
            }
        } else {
            drive.mecanumDrive_Cartesian(0, 0, 0, 0);
        }
    }
}
