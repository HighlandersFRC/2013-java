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
public class ClimbDownCommand extends CommandBase {

    public ClimbDownCommand() {
        requires(climberBelt);
    }
    protected void initialize() {
    }

    protected void execute() {
        climberBelt.climb(-SmartDashboard.getNumber("Belt Power", 100.0)/100);
    }

    protected boolean isFinished() {
        return !oi.climbDownButton.get();
    }

    protected void end() {
        climberBelt.climb(0.0);
    }

    protected void interrupted() {
    }
    
}
