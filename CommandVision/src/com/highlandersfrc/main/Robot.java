package com.highlandersfrc.main;


import com.highlandersfrc.main.commands.CameraFinder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import com.highlandersfrc.main.commands.CommandBase;
import com.highlandersfrc.main.commands.ArcadeDrive;
import com.highlandersfrc.main.commands.TankDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autoCommand;
    Command teleCommand;
    Command teleArmCommand;

    public void robotInit() {
        autoCommand = new CameraFinder();
        teleCommand = new TankDrive();
        CommandBase.init();
    }
    
    public void autonomousInit() {
        autoCommand.start();
    }
    
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }
    
    public void teleopInit() {
        autoCommand.cancel();
        teleCommand.start();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
}
