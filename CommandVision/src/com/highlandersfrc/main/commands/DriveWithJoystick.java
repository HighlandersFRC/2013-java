package com.highlandersfrc.main.commands;

/**
 *
 * @author bradmiller
 */
public class DriveWithJoystick extends CommandBase {

    public DriveWithJoystick() {
        requires(chassis);
    }

    protected void execute() {
        chassis.driveWithJoystick(oi.getJoystick());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }
    
    protected void interrupted() {
    }

    protected void initialize() {
    }
}
