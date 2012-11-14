/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.highlandersfrc.main.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author alex
 */
public class AirSystem extends Subsystem{
    
    DoubleSolenoid shoulder, claw;
    Compressor airCompressor;
    boolean shoulderLoc = false;
    boolean shoulderTrig = false;
    double shoulderTime = 0;
    
    public AirSystem()
    {
        airCompressor = new Compressor(1,1);
        shoulder = new DoubleSolenoid(1,2);
    }
    public void start()
    {
        airCompressor.start();
    }
    public void stop()
    {
        airCompressor.stop();
    }
    protected void initDefaultCommand() {
    }
    public void runArm(Joystick right)
    {
        if (right.getRawButton(3) ^ right.getRawButton(2) ^ right.getRawButton(1))
        {
            if (shoulderTrig == false || shoulderLoc != right.getRawButton(3) )
            {
                shoulderTime = Timer.getFPGATimestamp();
                shoulderTrig = true;
                if (right.getRawButton(3))
                {
                    shoulderLoc = true;
                    shoulder.set(DoubleSolenoid.Value.kForward);
                }
                else if (right.getRawButton(2))
                {
                    shoulderLoc = false;
                    shoulder.set(DoubleSolenoid.Value.kReverse);
                }
                else if (right.getRawButton(1))
                {
                    shoulderLoc = !shoulderLoc;
                    if (shoulderLoc)
                    {
                        shoulder.set(DoubleSolenoid.Value.kForward);
                    }
                    else
                    {
                        shoulder.set(DoubleSolenoid.Value.kReverse);
                    }
                }
            }
            else if (Timer.getFPGATimestamp() - shoulderTime > 0.25)
            {
                shoulder.set(DoubleSolenoid.Value.kOff);
                shoulderTrig = false;
            }
        }
        if (Timer.getFPGATimestamp() - shoulderTime > 0.25)
        {
            shoulder.set(DoubleSolenoid.Value.kOff);
            shoulderTrig = false;
        }
    }
}
