/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author alex
 */
public class DriveBase extends PIDSubsystem {

    private static final double Kp = 0.0;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    private RobotDrive drive = new RobotDrive(RobotMap.driveFrontLeft, RobotMap.driveRearLeft, RobotMap.driveFrontRight, RobotMap.driveRearRight);
    private Gyro gyro = new Gyro(RobotMap.baseGyro);
    private double lastPIDoutput = 0.0;

    public DriveBase() {
        super("DriveBase", Kp, Ki, Kd);
    }

    protected void initDefaultCommand() {
        gyro.reset();
    }

    public void driveAbs(double x, double y, double theta, double gyroOffset) {
        drive.mecanumDrive_Cartesian(x, y, theta, gyro.getAngle() + gyroOffset);
    }

    public void driveRel(double x, double y, double theta) {
        drive.mecanumDrive_Cartesian(x, y, theta, 0);
    }

    public void driveTarget(double x, double y) {
        drive.mecanumDrive_Cartesian(x, y, lastPIDoutput, 0);
    }

    protected double returnPIDInput() {
        return gyro.getAngle();
    }

    protected void usePIDOutput(double output) {
        lastPIDoutput = output;
    }
}
