package com.highlandersfrc.main.commands;

/**
 *
 * @author alex
 */
public class MoveArm extends CommandBase {

    public MoveArm() {
        requires(pneumatics);
        //requires(chassis);
    }

    protected void initialize() {
    }

    protected void execute() {
        //chassis.drive(0, 0);
        pneumatics.runArm(oi.getJoystick1(), oi.getJoystick2());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
