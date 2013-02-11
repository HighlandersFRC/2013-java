/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author alex
 */
public class DriveBase extends Subsystem{
    private RobotDrive drive = new RobotDrive(1, 2, 4, 3);
    private Gyro gyro = new Gyro(1);
    protected void initDefaultCommand() {
        gyro.reset();
    }
    public void driveAbs(double x, double y, double theta, double gyroOffset) {
        drive.mecanumDrive_Cartesian(x, y, theta, gyro.getAngle()+gyroOffset);
    }
    public void driveRel(double x, double y, double theta) {
        drive.mecanumDrive_Cartesian(x, y, theta, 0);
    }
}
