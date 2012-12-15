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

    public Pneumatics() {
        one = new Solenoid(1);
        two = new Solenoid(2);
        compressor = new Compressor(1, 8);
        compressor.start();
    }

    public void start() {
        compressor.start();
    }

    public void stop() {
        compressor.stop();
    }

    protected void initDefaultCommand() {
    }

    public void runArm(Joystick left, Joystick right) {
        if (left.getRawButton(2)) {
            one.set(true);
            two.set(false);
        } else if (left.getRawButton(3)) {
            one.set(false);
            two.set(true);
        }
        if (left.getRawButton(4)) {
            compressor.stop();
        } else if (left.getRawButton(5)) {
            compressor.start();
        }
    }
}
