/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author alex
 */
public class ArticulateDown extends CommandBase {
    
    public ArticulateDown() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooterArticulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        shooterArticulator.articulate(-SmartDashboard.getNumber("Articulator Power", 1)/100);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !oi.articulateDownButton1.get() && ! oi.articulateDownButton2.get();
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
