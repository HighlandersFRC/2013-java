package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.DriveBase;
import edu.wpi.first.wpilibj.templates.subsystems.ClimberBelt;
import edu.wpi.first.wpilibj.templates.subsystems.ClimberShoulder;
import edu.wpi.first.wpilibj.templates.subsystems.ShooterArticulator;
import edu.wpi.first.wpilibj.templates.subsystems.ShooterIndexer;
import edu.wpi.first.wpilibj.templates.subsystems.ShooterInjector;
import edu.wpi.first.wpilibj.templates.subsystems.ShooterLauncher;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static ClimberBelt climberBelt = new ClimberBelt();
    public static DriveBase drive = new DriveBase();
    public static ClimberShoulder climberShoulder = new ClimberShoulder();
    public static ShooterArticulator shooterArticulator = new ShooterArticulator();
    public static ShooterIndexer shooterIndexer = new ShooterIndexer();
    public static ShooterInjector shooterInjector = new ShooterInjector();
    public static ShooterLauncher shooterLauncher = new ShooterLauncher();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData("Articulator", shooterArticulator);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
