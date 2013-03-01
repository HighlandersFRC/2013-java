/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class ArmTest extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public class ArmPIDsource implements PIDSource {

        public double pidGet() {
            return armGyro.getAngle();
        }
    }
    private ArmPIDsource armsource = new ArmPIDsource();
    private Victor arm = new Victor(6);
    private PIDController armpid = new PIDController(1,1,1,armsource,arm);
    private Joystick joy1 = new Joystick(1);
    private DigitalInput limit = new DigitalInput(1,2);
    private Gyro armGyro = new Gyro(1);

    public void robotInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        armpid.setInputRange(-180, 180);
        armpid.setOutputRange(-1, 1);
        armpid.setSetpoint(-22);
    }
    public void teleopPeriodic() {
        armpid.setPID(SmartDashboard.getNumber("kP"), SmartDashboard.getNumber("kI"), SmartDashboard.getNumber("kD"));
        if (joy1.getRawButton(1)) {
            armpid.enable();
        } else {
            armpid.disable();
            arm.set(0);
        }
        SmartDashboard.putBoolean("Checkbox 2", limit.get());
        System.out.println("limit "+ limit.get());
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
