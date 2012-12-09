package com.highlandersfrc.main.commands;

/**
 *
 * @author bradmiller
 */
public class ArcadeDrive extends CommandBase {

    public ArcadeDrive() {
        //requires(chassis);
    }

    protected void execute() {
        //chassis.arcadeDrive(oi.getJoystick1());
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
