/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author alex
 */
public class ClimberShoulder extends Subsystem {
    private Talon shoulder = new Talon(RobotMap.climberShoulder);
    private Solenoid lock = new Solenoid(RobotMap.shoulderLock);
    private boolean locked = true;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setShoulder(double pwr) {
        if (!locked) {
            shoulder.set(pwr);
        }
    }
    
    public void setLock(boolean on) {
        lock.set(!on);
        if (!on) {
            locked = false;
        }
    }
}
