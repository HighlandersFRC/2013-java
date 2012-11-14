/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.highlandersfrc.main.commands;

/**
 *
 * @author alex
 */
public class armController extends CommandBase{
    public armController() {
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
