/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author alex
 */
public class HoldClimb extends CommandBase {
    double startTime;
    
    public HoldClimb() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(climberBelt);
        this.setRunWhenDisabled(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
        System.out.println("disabledTest");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        climberBelt.climb(0.25);
        System.out.println("disabledExecute");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + 7.0;
    }

    // Called once after isFinished returns true
    protected void end() {
        climberBelt.climb(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
