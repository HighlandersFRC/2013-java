/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.PneumaticClimber;

/**
 *
 * @author alex
 */
public class PneumaticStopCommand extends CommandBase {

    protected void initialize() {
        ((PneumaticClimber)climbArm).stopComp();
    }

    public PneumaticStopCommand() {
        requires(climbArm);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
