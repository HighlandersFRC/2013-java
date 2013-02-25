package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.ClimbDownCommand;
import edu.wpi.first.wpilibj.templates.commands.ClimbUpCommand;
import edu.wpi.first.wpilibj.templates.commands.PneumaticStartCommand;
import edu.wpi.first.wpilibj.templates.commands.PneumaticStopCommand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
    public Joystick joy1 = new Joystick(1);
    public Joystick joy2 = new Joystick(2);
    public Button climbUpButton = new JoystickButton(joy1, 11);
    public Button climbDownButton = new JoystickButton(joy1, 10);
    public Button startCompButton = new JoystickButton(joy1, 3);
    public Button stopCompButton = new JoystickButton(joy1, 2);
    public Button lockonButton = new JoystickButton(joy2, 2);
    public Button fireButton = new JoystickButton(joy2, 1);
    public JoystickAxis drivex = new JoystickAxis(joy2, Joystick.AxisType.kX.value);
    public JoystickAxis drivey = new JoystickAxis(joy2, Joystick.AxisType.kY.value);
    public JoystickAxis driveTheta = new JoystickAxis(joy1, Joystick.AxisType.kX.value);
    

    {
        climbUpButton.whenPressed(new ClimbUpCommand());
        climbDownButton.whenPressed(new ClimbDownCommand());
        startCompButton.whenPressed(new PneumaticStartCommand());
        stopCompButton.whenPressed(new PneumaticStopCommand());
    }
}
