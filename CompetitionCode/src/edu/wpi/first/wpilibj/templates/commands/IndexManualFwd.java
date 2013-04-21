/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author alex
 */
public class IndexManualFwd extends CommandBase {
    
    public IndexManualFwd() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterIndexer);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooterIndexer.set(-1.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !oi.index.get();
    }

    // Called once after isFinished returns true
    protected void end() {
        shooterIndexer.set(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
