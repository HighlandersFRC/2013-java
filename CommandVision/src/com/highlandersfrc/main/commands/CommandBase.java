package com.highlandersfrc.main.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.highlandersfrc.main.OI;
import com.highlandersfrc.main.subsystems.Pneumatics;
import com.highlandersfrc.main.subsystems.Camera;
import com.highlandersfrc.main.subsystems.Chassis;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author David Gronlund
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    public static Chassis chassis = new Chassis();
    public static Camera camera = new Camera();
    public static Pneumatics pneumatics = new Pneumatics();
    
    public CommandBase(String name) {
        super(name);
    }
    
    public CommandBase() {
        super();
    }

    public static void init() {
        oi = new OI();
        //SmartDashboard.putData(chassis);
        //SmartDashboard.putData(camera);
    }
}
