/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author alex
 */
public class TeleopDriveCommand extends CommandBase {

    public TeleopDriveCommand() {
        requires(drive);
    }

    protected void initialize() {
    }

    protected void execute() {
        drive.driveRel(oi.joy1.getAxis(Joystick.AxisType.kX), oi.joy1.getAxis(Joystick.AxisType.kY), oi.joy2.getAxis(Joystick.AxisType.kX));
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
