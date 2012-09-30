
package com.highlandersfrc.main;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    
    public static final int JOYSTICK_PORT = 1;
    
    private Joystick stick;
    
    public OI() {
        stick = new Joystick(JOYSTICK_PORT);
    }
    
    public Joystick getJoystick() {
        return stick;
    }
}

