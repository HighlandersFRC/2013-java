/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author alex
 */
public class ShooterInjector extends Subsystem {
    private Talon injector = new Talon(RobotMap.shooterInjector);
    private DigitalInput limit = new DigitalInput(RobotMap.injectorLimit);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void setPwr(double pwr) {
        injector.set(pwr);
    }
    public boolean getLimit() {
        return limit.get();
    }
}
