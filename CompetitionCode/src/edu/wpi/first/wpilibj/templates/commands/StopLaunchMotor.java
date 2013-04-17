/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author alex
 */
public class StopLaunchMotor extends CommandBase {
    
    public StopLaunchMotor() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterLauncher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooterLauncher.set(0.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
