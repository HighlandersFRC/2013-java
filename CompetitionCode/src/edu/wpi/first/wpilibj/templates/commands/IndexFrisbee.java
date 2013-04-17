/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author alex
 */
public class IndexFrisbee extends CommandBase {
    public double endTime = 0;
    
    public IndexFrisbee() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterIndexer);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        endTime = Timer.getFPGATimestamp() + SmartDashboard.getNumber("Indexer Time", 1.0);
        shooterIndexer.set(-1.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() > endTime;
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
