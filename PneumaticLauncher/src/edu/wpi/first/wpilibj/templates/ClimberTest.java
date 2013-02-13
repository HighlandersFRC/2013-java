package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class ClimberTest extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private Compressor compressor = new Compressor(1, 1);
    private DoubleSolenoid solenoid = new DoubleSolenoid(1, 2);
    private Joystick joystick = new Joystick(1);
    private Victor armMotor = new Victor(5);

    public void robotInit() {
        //compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
//        if (joystick.getRawButton(2)) {
//            solenoid.set(DoubleSolenoid.Value.kReverse);
//        } else if (joystick.getRawButton(3)) {
//            solenoid.set(DoubleSolenoid.Value.kForward);
//        }
//        if (joystick.getRawButton(4)) {
//            compressor.stop();
//        } else if (joystick.getRawButton(5)) {
//            compressor.start();
//        }
        System.out.println(joystick.getY());
        armMotor.set(joystick.getY() / 2);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
