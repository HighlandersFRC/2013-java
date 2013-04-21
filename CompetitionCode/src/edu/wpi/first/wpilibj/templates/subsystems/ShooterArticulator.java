/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author alex
 */
public class ShooterArticulator extends PIDSubsystem {

    private static final double Kp = 0.0;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    private static final double bottomAngle = 0.0;
    private static final double topAngle = 45.0;
    private static final boolean articulatorReversed = false;
    
    private DigitalInput topLimit = new DigitalInput(RobotMap.shooterTopLimit);
    private DigitalInput bottomLimit = new DigitalInput(RobotMap.shooterBottomLimit);
    private Encoder quad = new Encoder(RobotMap.shooterEncoderA, RobotMap.shooterEncoderB);
    private Talon articulator = new Talon(8);
    private boolean zeroLimitState = false;

    // Initialize your subsystem here
    public ShooterArticulator() {
        super("ShooterArticulator", Kp, Ki, Kd);
        quad.reset();
        quad.start();
        this.setInputRange(bottomAngle, topAngle);
        SmartDashboard.putData("ElevationTargeting PID", this.getPIDController());
        SmartDashboard.putData("ArticulatorEncoder", quad);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    private void encoderCalibration() {
        if (topLimit.get() && !zeroLimitState) {
            quad.reset();
        }
        zeroLimitState = topLimit.get();
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        encoderCalibration();
        if (topLimit.get()) {
            return topAngle;
        }
        return quad.getDistance();
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        articulate(output);
    }
    public void articulate(double pwr) {
        pwr = (articulatorReversed?-1:1)*pwr;
        SmartDashboard.putBoolean("ArticulatorTopLimit", topLimit.get());
        SmartDashboard.putData("ArticulatorEncoder", quad);
        encoderCalibration();
        if (topLimit.get()) {
            articulator.set(Math.max(pwr, 0.0));
        } else if (bottomLimit.get()&&false) {
            articulator.set(Math.min(pwr, 0.0));
        } else {
            articulator.set(pwr);
        }
    }
}
