/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author alex
 */
public class ClimbDownCommand extends CommandBase {

    public ClimbDownCommand() {
        requires(climbArm);
    }
    protected void initialize() {
    }

    protected void execute() {
        climbArm.climbDown();
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
