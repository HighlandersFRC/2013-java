package com.highlandersfrc.main;


import com.highlandersfrc.main.commands.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command teleArmCommand;
    Command teleDrive;

    public void robotInit() {
        teleArmCommand = new MoveArm();
        teleDrive = new TankDrive();
        CommandBase.init();
    }
    
    public void autonomousInit() {
    }
    
    public void autonomousPeriodic() {
    }
    
    public void teleopInit() {
        teleArmCommand.start();   
        teleDrive.start();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
}
