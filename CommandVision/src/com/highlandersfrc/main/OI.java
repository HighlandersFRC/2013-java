
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
    
    public static final int JOYSTICK_PORT_1 = 1;
    public static final int JOYSTICK_PORT_2 = 2;
    
    private Joystick stick1;
    private Joystick stick2;
    
    public OI() {
        stick1 = new Joystick(JOYSTICK_PORT_1);
        stick2 = new Joystick(JOYSTICK_PORT_2);
    }
    
    public Joystick getJoystick1() {
        return stick1;
    }
    
    public Joystick getJoystick2() {
        return stick2;
    }
}

