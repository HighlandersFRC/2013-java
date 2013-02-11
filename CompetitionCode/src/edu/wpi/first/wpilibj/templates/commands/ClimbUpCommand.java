/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author alex
 */
public class ClimbUpCommand extends CommandBase {

    public ClimbUpCommand() {
        requires(climbArm);
    }
    protected void initialize() {
    }

    protected void execute() {
        climbArm.climbUp();
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
