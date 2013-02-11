/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author alex
 */
public class PneumaticClimber extends ClimberArm {

    private Compressor comp = new Compressor(1, 1);
    private DoubleSolenoid shortPiston = new DoubleSolenoid(1, 2);
    private DoubleSolenoid longPiston = new DoubleSolenoid(3, 4);
    private int climbState;
    private long lastTimeStamp;

    protected void initDefaultCommand() {
    }

    public void climbUp() {
        setClimb(climbState++);
        climbState = (climbState == 3) ? 0 : climbState;
    }

    public void climbDown() {
        setClimb(climbState--);
        climbState = (climbState == -1) ? 2 : climbState;
    }

    public void setClimb(int climbState) {
        switch (climbState) {
            case 0:
                shortPiston.set(DoubleSolenoid.Value.kReverse);
                longPiston.set(DoubleSolenoid.Value.kReverse);
                break;
            case 1:
                shortPiston.set(DoubleSolenoid.Value.kForward);
                longPiston.set(DoubleSolenoid.Value.kReverse);
                break;
            case 2:
                shortPiston.set(DoubleSolenoid.Value.kReverse);
                longPiston.set(DoubleSolenoid.Value.kForward);
                break;
        }
    }

    public void startComp() {
        comp.start();
    }

    public void stopComp() {
        comp.stop();
    }
}
