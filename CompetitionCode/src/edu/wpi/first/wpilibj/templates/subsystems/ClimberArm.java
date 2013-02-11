/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author alex
 */
public abstract class ClimberArm extends Subsystem {
    
    protected void initDefaultCommand() {
    }
    public abstract void climbUp();
    public abstract void climbDown();
    
}
