/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.communication.FRCControl;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    ADXL345_I2C accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
    I2C acceli2c = new I2C(DigitalModule.getInstance(1),0x1d);
//    Gyro gyro = new Gyro(1);
    int count = 0;
    public void robotInit() {
        System.out.println("robot init");
        FRCControl.setErrorData(new byte[] {0x41}, 1, 0);
        acceli2c.write(0x2d, 0x08);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        if(count == 100) {
            count = 0;
            byte[] data = new byte[1];
            System.out.println(acceli2c.read(0, 1, data));
            for (int i = 0; i < data.length; i++) {
                System.out.print(" " + data[i]);

            }
            System.out.println();
            System.out.println(accel.getAccelerations().ZAxis);
            System.out.println("test");
            System.out.println("(" + accel.getAcceleration(ADXL345_I2C.Axes.kX) + ", " + accel.getAcceleration(ADXL345_I2C.Axes.kY) + ", " + accel.getAcceleration(ADXL345_I2C.Axes.kZ) + ", "/* + gyro.getAngle()*/ + ")");
        }
        ++ count;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
