/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 * @author GLaDOS
 */
public class Autonomous extends CommandBase {

    double autostarttime;
    int autostate = 0;
    Command articulatorReset = new ArticulateToTop();
    Command articulatorTarget = new FireArticulate();

    public Autonomous() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        new RunInjectorBack().start();
        new StartLaunchMotor().start();
        articulatorReset.start();
        autostarttime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!articulatorReset.isRunning() && !articulatorTarget.isRunning()) {
            articulatorTarget.start();
        }
        if (Timer.getFPGATimestamp() - autostarttime > 5 && autostate == 0) {
            autostate = 1;
            new FireCycle().start();
        }
        if (Timer.getFPGATimestamp() - autostarttime > 9 && autostate == 1) {
            autostate = 2;
            new FireCycle().start();
        }
        if (Timer.getFPGATimestamp() - autostarttime > 13 && autostate == 2) {
            autostate = 3;
            new FireCycle().start();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
