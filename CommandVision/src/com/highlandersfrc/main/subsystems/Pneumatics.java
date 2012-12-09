package com.highlandersfrc.main.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author alex
 */
public class Pneumatics extends Subsystem {

    Solenoid one;
    Solenoid two;
    Compressor compressor;
    //Relay relay;
    //DigitalInput pressureSwitch;

    public Pneumatics() {
        one = new Solenoid(1);
        two = new Solenoid(2);
        //pressureSwitch = new DigitalInput(1);
        compressor = new Compressor(1, 8);
        //relay = new Relay(8);
        //relay.setDirection(Relay.Direction.kForward);
    }

    public void start() {
        System.out.println("Called");
        compressor.start();
    }

    public void stop() {
        compressor.stop();
    }

    protected void initDefaultCommand() {
    }

    public void runArm(Joystick left, Joystick right) {
        if (right.getRawButton(1)) {
            one.set(true);
            two.set(false);
        } else if (right.getRawButton(2)) {
            one.set(false);
            two.set(true);
        }
        System.out.println(compressor.enabled());
        if (compressor.getPressureSwitchValue()) {
            System.out.println("Above");
        } else {
            System.out.println("Below");
        }
        if (left.getRawButton(1)) {
            compressor.start();
        } else if (left.getRawButton(2)) {
            compressor.stop();
        }
//        if (pressureSwitch.get()) {
//            System.out.println("Above");
//        } else {
//            System.out.println("Below");
//        }
    }
}
