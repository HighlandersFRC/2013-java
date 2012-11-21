package com.highlandersfrc.main.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author alex
 */
public class Pneumatics extends Subsystem {

    Solenoid one;
    Solenoid two;
    Compressor compressor;
    DigitalOutput out;

    public Pneumatics() {
        one = new Solenoid(1);
        two = new Solenoid(2);
        out = new DigitalOutput(5);
    }

    public void start() {
        //compressor.start();
    }

    public void stop() {
        //compressor.stop();
    }

    protected void initDefaultCommand() {
    }

    public void runArm(Joystick right) {
        if (right.getRawButton(3)) {
            one.set(true);
            two.set(false);
        } else if (right.getRawButton(2)) {
            one.set(false);
            two.set(true);
        }
    }
}
