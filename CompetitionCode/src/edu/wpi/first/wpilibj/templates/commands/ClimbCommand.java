/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author alex
 */
public class ClimbCommand extends CommandBase {

    protected void initialize() {
        requires(climbArm);
    }

    protected void execute() {
        
    }

    protected boolean isFinished() {
        return false; //todo: add logic
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
