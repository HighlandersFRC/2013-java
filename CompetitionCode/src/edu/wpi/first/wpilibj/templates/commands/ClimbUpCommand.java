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
        requires(climberBelt);
    }
    protected void initialize() {
    }

    protected void execute() {
        climberBelt.climb(1.0);
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
        climberBelt.climb(0.0);
    }

    protected void interrupted() {
    }
    
}
