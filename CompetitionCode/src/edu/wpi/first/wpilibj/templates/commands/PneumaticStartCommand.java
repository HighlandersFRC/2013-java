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
public class PneumaticStartCommand extends CommandBase {

    protected void initialize() {
        ((PneumaticClimber)climbArm).startComp();
    }

    public PneumaticStartCommand() {
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
