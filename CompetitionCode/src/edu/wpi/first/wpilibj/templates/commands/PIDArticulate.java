/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Robotics
 */
public class PIDArticulate extends CommandBase {
    
    public PIDArticulate() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterArticulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooterArticulator.enable();
        shooterArticulator.setSetpoint(15);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        shooterArticulator.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        shooterArticulator.disable();
    }
}