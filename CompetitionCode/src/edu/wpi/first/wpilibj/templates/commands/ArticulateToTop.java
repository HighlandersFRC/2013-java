/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author alex
 */
public class ArticulateToTop extends CommandBase {
    
    public ArticulateToTop() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterArticulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooterArticulator.articulate(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return shooterArticulator.getTopLimit();
    }

    // Called once after isFinished returns true
    protected void end() {
        shooterArticulator.articulate(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
