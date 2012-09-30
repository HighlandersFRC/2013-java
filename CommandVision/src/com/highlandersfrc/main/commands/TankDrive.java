package com.highlandersfrc.main.commands;

/**
 *
 * @author David
 */
public class TankDrive extends CommandBase {
    
    public TankDrive() {
        requires(chassis);
    }

    protected void execute() {
        chassis.tankDrive(oi.getJoystick1(), oi.getJoystick2());
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
