package com.highlandersfrc.main.commands;

/**
 *
 * @author alex
 */
public class MoveArm extends CommandBase {

    public MoveArm() {
        requires(pneumatics);
    }

    protected void initialize() {
    }

    protected void execute() {
        pneumatics.runArm(oi.getJoystick2());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
