/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Robotics
 */
public class InjectorManualBack extends CommandBase {
    
    public InjectorManualBack() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterInjector);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooterInjector.setPwr(0.25);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !oi.injectorBackButton.get();
    }

    // Called once after isFinished returns true
    protected void end() {
        shooterInjector.setPwr(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}