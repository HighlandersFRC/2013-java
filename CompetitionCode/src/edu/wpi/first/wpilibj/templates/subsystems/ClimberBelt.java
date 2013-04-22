/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author alex
 */
public class ClimberBelt extends Subsystem {
    private Talon belt = new Talon(RobotMap.climberBelt);

    protected void initDefaultCommand() {
    }

    public void climb(double pwr) {
        belt.set(-pwr);
    }
}

    

