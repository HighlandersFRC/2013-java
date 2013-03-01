/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class FinalRobot extends IterativeRobot {

    Gyro gyro = new Gyro(1);
    boolean driver = true;
    boolean driveLock = false;
    double gunnerX;
    double gunnerY;
    double heading;
    LatchedBoolean absCtrlMode = new LatchedBoolean();
    RobotDrive drive = new RobotDrive(1, 2, 4, 3);
    Victor arm = new Victor(5);
    Victor arm2 = new Victor(6);
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Joystick joy3 = new Joystick(3);
    Victor launch = new Victor(7);
    Victor injector = new Victor(8);
    Compressor comp = new Compressor(1, 1);
    DoubleSolenoid feed = new DoubleSolenoid(1, 2);
    boolean wheel = false;
    LatchedBoolean hoppercontrol = new LatchedBoolean();
    boolean fireControl = false;
    boolean firing = false;
    double injPwr;
    double launchPwr;
    double injPulsePwr;
    double launchPulsePwr;
    double injPulseLen;
    double launchPulseLen;
    double injPulseDel;
    double launchPulseDel;
    double feedTime;
    double fireStartTime;
    boolean defaultPistonState;
    double autoStartTime;
    int autoState = 0;
    SendableChooser autoChooser = new SendableChooser();
    PIDController armpid = new PIDController(1, 1, 1, gyro, arm2);
    Preferences pref = Preferences.getInstance();
    Object autoMode;

    {
        armpid.setInputRange(-180, 180);
        armpid.setOutputRange(-1, 1);
        autoChooser.addDefault("None", null);
        autoChooser.addObject("Behind Pyramid", new Double(-4));
        autoChooser.addObject("Back Corner Of Pyramid", new Double(-3));
        autoChooser.addObject("Front Corner Of Pyramid", new Double(-5));
    }

    public void robotInit() {
        drive.setSafetyEnabled(false);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        System.out.println("init");
        SmartDashboard.putNumber("Launch Power", 60);
        SmartDashboard.putNumber("Injector Power", 45);
        SmartDashboard.putNumber("Injector Pulse Delay", 0);
        SmartDashboard.putNumber("Launcher Pulse Delay", 0);
        SmartDashboard.putNumber("Injector Pulse Power", 100);
        SmartDashboard.putNumber("Launcher Pulse Power", 100);
        SmartDashboard.putNumber("Injector Pulse Length", 0.25);
        SmartDashboard.putNumber("Launcher Pulse Length", 0.25);
        SmartDashboard.putNumber("Piston Extension Time", 0.75);
        SmartDashboard.putBoolean("Piston Default Extended", false);
        SmartDashboard.putNumber("Shoulder Power", 100);
        SmartDashboard.putNumber("Belt Power", 100);
        SmartDashboard.putNumber("kP", pref.getDouble("kp", 1));
        SmartDashboard.putNumber("kI", pref.getDouble("kI", 1));
        SmartDashboard.putNumber("kD", pref.getDouble("kD", 1));
        double voltage = DriverStation.getInstance().getBatteryVoltage();
        //magic code to calculate percent charge from voltage. formula given by quartic regression on empirical data. DO NOT TOUCH
        //DO NOT TOUCH NEXT LINE
        SmartDashboard.putNumber(" initial charge percent", (voltage > 11.7 ? (voltage < 12.6 ? 337.90418311119 * MathUtils.pow(voltage, 4) - 16321.796968975 * MathUtils.pow(voltage, 3) + 295645.26274574 * MathUtils.pow(voltage, 2) - 2379991.6548433 * voltage + 7184291.5749442 : 125) : -10));
        //DO NOT TOUCH PREVIOUS LINE
    }

    public void autonomousInit() {
        wheel = true;
        armpid.setPID(SmartDashboard.getNumber("kP"), SmartDashboard.getNumber("kI"), SmartDashboard.getNumber("kD"));
        autoMode = autoChooser.getSelected();
        if (autoMode != null) {
            armpid.setSetpoint(((Double) autoMode).doubleValue());
            armpid.enable();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        if (autoMode != null) {
            iterateFiring();
            if (Timer.getFPGATimestamp() - autoStartTime > 5 && autoState == 0) {
                autoState = 1;
                fireControl = true;
            }
            if (Timer.getFPGATimestamp() - autoStartTime > 8.5 && autoState == 1) {
                autoState = 2;
                fireControl = true;
            }
            if (Timer.getFPGATimestamp() - autoStartTime > 11 && autoState == 2) {
                autoState = 3;
                fireControl = true;
            }
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        /*
         * controls:
         * joystick 2, x-axis(left-right): drive translation x-axis(left-right)
         * joystick 2, y-axis(front-back): drive translation y-axis(front-back)
         * joystick 1, x-axis(left-right): drive rotation(counterclockwise-clockwise)
         * joystick 2, button 1(trigger): engage firing cycle
         * joystick 2, button 2(secondary fire): lock on target; unused until: camera mounted
         *      arm gyro connected
         *      targeting code tested
         * joystick 2, button 7(base buttons): manual override(retract feed piston)
         * joystick 2, button 8(base buttons): manual override(extend feed piston)
         * joystick 2, button 9(base buttons): spin up shooter
         * joystick 2, button 10(base buttons): spin down shooter
         * joystick 2, button 11(base buttons): disable compressor (may be irrelevant if compressor is offboard)
         * joystick 2, button 12(base buttons): enable compressor (may be irrelevant if compressor is offboard)
         * joystick 1, button 9(back buttons): toggle alignment mode
         *      depends on hardware not installed; DO NOT TOUCH
         * joystick 1, button 8(back buttons): toggle absolute control mode
         *      depends on hardware not installed; unknown effect, possibilities:
         *          nothing
         *          unpredictable direction mapping
         * joystick 3, button 6(bottom buttons): rotate shoulder forward
         *      this control may be added to gunner's joysticks to reduce the driver's need to multitask
         *      may be remapped if awkward to control.
         * joystick 3, button 7(bottom buttons): rotate shoulder backward
         *      this control may be added to gunner's joysticks to reduce the driver's need to multitask
         *      may be remapped if awkward to control.
         * joystick 3, button 11(bottom buttons): raise climbing claws
         *      may be remapped if awkward to control.
         * joystick 3, button 10(bottom buttons): lower climbing claws
         *      may be remapped if awkward to control.
         * additional notes: be cautious about initiating a firing cycle while moving or accelerating rapidly,
         *          this causes a large voltage drop with possible side effects including:
         *              loss of power to motors slowing motion and making shots miss
         *              loss of sensor calibration (could cause failure to maintain cylinder
         *                  or even cause attempt to put arm in impossible position.(bad))
         *              brown out of cRIO (possible regain of control after reboot, not guaranteed)
         *          however these are worst case scenarios and most likely will not occur, however caution is advised
         *      no fields on the smart dashboard should be edited during the match if possible.
         *      The CV widget can be used to aim if the camera is mounted.
         *      We (of the programming team) can add/change controls at request
         *          however, we will endeavor to minimize control changes otherwise.
         *          if we must change the controls we will notify people if feasible.
         */
//        System.out.println("Joystick: (" + joy1.getX()+", "+joy1.getY()+", "+joy2.getX()+")");
//        System.out.println(accel.getAccelerations());
//        System.out.println("test");
//        System.out.println("(" + accel.getAcceleration(Axes.kX) + ", " + accel.getAcceleration(Axes.kY) + ", " + accel.getAcceleration(Axes.kZ) + ", " + gyro.getAngle() + ")");

//        if (!driveLock && joy1.getRawButton(9)) {
////            System.out.println(driver);
////            driver = !driver;
//            driveLock = true;
////            System.out.println("driver: "+driver+" "+!driver);
//            if (driver == false) {
//                heading = gyro.getAngle();
//            }
////            System.out.println("toggle: " + driver);
//        } else if (driveLock && !joy1.getRawButton(9)) {
//            driveLock = false;
//        }
        if (driver) {
//            System.out.println("trigger");
//            absCtrlMode.set(joy1.getRawButton(8));
            drive.mecanumDrive_Cartesian(joy2.getX(), -joy2.getY(), joy1.getX(), absCtrlMode.getValue() ? gyro.getAngle() : 0);
        } else {
            if (joy2.getRawButton(4) && !joy2.getRawButton(5)) {
                gunnerX = -0.3;
            } else if (joy2.getRawButton(5) && !joy2.getRawButton(4)) {
                gunnerX = 0.3;
            } else {
                gunnerX = 0;
            }
            if (joy2.getRawButton(2) && !joy2.getRawButton(3)) {
                gunnerY = 0.3;
            } else if (joy2.getRawButton(3) && !joy2.getRawButton(2)) {
                gunnerY = -0.3;
            } else {
                gunnerY = 0;
            }
            heading += joy2.getAxis(Joystick.AxisType.kX);
            drive.mecanumDrive_Cartesian(gunnerX, gunnerY, (-gyro.getAngle() + heading) / 20, 0);
        }
        if (joy3.getRawButton(11)) {
            arm.set(-SmartDashboard.getNumber("Belt Power") / 100);
        } else if (joy3.getRawButton(10)) {
            arm.set(SmartDashboard.getNumber("Belt Power") / 100);
        } else {
            arm.set(0);
        }
        if (joy3.getRawButton(6) /*|| joy2.getRawButton(3)*/) {
            arm2.set(SmartDashboard.getNumber("Shoulder Power") / 100);
        } else if (joy3.getRawButton(7)/* || joy2.getRawButton(2)*/) {
            arm2.set(-SmartDashboard.getNumber("Shoulder Power") / 100);
        } else {
            arm2.set(0);
        }
        if (!fireControl) {
            launchPwr = -SmartDashboard.getNumber("Launch Power") / 100;
            injPwr = -SmartDashboard.getNumber("Injector Power") / 100;
            if (joy1.getRawButton(7)) {
                feed.set(DoubleSolenoid.Value.kReverse);
            } else if (joy1.getRawButton(6)) {
                feed.set(DoubleSolenoid.Value.kForward);
            } else {
                feed.set(DoubleSolenoid.Value.kOff);
            }
        }
        if (joy1.getRawButton(3)) {
            wheel = true;
        } else if (joy1.getRawButton(2)) {
            wheel = false;
        }
        iterateFiring();
        if (joy1.getRawButton(10)) {
            comp.stop();
        }
        if (joy2.getRawButton(11)) {
            comp.start();
        }
    }

    public void testInit() {
        pref.putDouble("kP", SmartDashboard.getNumber("kP", 1));
        pref.putDouble("kI", SmartDashboard.getNumber("kI", 1));
        pref.putDouble("kD", SmartDashboard.getNumber("kD", 1));
        pref.save();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        double voltage = DriverStation.getInstance().getBatteryVoltage();
        //magic code to calculate percent charge from voltage. formula given by quartic regression on empirical data. DO NOT TOUCH
        //DO NOT TOUCH NEXT LINE
        SmartDashboard.putNumber(" initial charge percent", (voltage > 11.7 ? (voltage < 12.6 ? 337.90418311119 * MathUtils.pow(voltage, 4) - 16321.796968975 * MathUtils.pow(voltage, 3) + 295645.26274574 * MathUtils.pow(voltage, 2) - 2379991.6548433 * voltage + 7184291.5749442 : 125) : -10));
        //DO NOT TOUCH PREVIOUS LINE
    }

    private void iterateFiring() throws TableKeyNotDefinedException {
        if (wheel && !fireControl) {
            launch.set(launchPwr);
            injector.set(injPwr);
        } else if (!fireControl) {
            launch.set(0);
            injector.set(0);
        }
        if (joy1.getRawButton(1) && !fireControl || fireControl && !firing) {
            System.out.println("fireStart");
            injPulsePwr = -SmartDashboard.getNumber("Injector Pulse Power") / 100;
            launchPulsePwr = -SmartDashboard.getNumber("Launcher Pulse Power") / 100;
            injPulseLen = SmartDashboard.getNumber("Injector Pulse Length");
            launchPulseLen = SmartDashboard.getNumber("Launcher Pulse Length");
            injPulseDel = SmartDashboard.getNumber("Injector Pulse Delay");
            launchPulseDel = SmartDashboard.getNumber("Launcher Pulse Delay");
            feedTime = SmartDashboard.getNumber("Piston Extension Time");
            defaultPistonState = SmartDashboard.getBoolean("Piston Default Extended");
            feed.set(DoubleSolenoid.Value.kReverse);
            fireControl = true;
            fireStartTime = Timer.getFPGATimestamp() + (defaultPistonState ? feedTime : 0);
            firing = true;
        }
        if (fireControl) {
            System.out.println("firing");
            double currTime = Timer.getFPGATimestamp();
            double fireTime = currTime - fireStartTime;
            if (fireTime >= 0) {
                feed.set(DoubleSolenoid.Value.kForward);
            }
            if (fireTime >= injPulseDel && fireTime < injPulseDel + injPulseLen) {
                injector.set(injPulsePwr);
            }
            if (fireTime >= launchPulseDel && fireTime < launchPulseDel + launchPulseLen) {
                launch.set(launchPulsePwr);
            }
            if (fireTime >= injPulseDel + injPulseLen) {
                injector.set(injPwr);
            }
            if (fireTime >= launchPulseDel + launchPulseLen) {
                launch.set(launchPwr);
            }
            if (fireTime >= feedTime) {
                feed.set(defaultPistonState ? DoubleSolenoid.Value.kOff : DoubleSolenoid.Value.kReverse);
            }
            if (fireTime >= Math.max(Math.max(injPulseDel + injPulseLen, launchPulseDel + launchPulseLen), feedTime + 0.1)) {
                if (!joy1.getRawButton(1)) {
                    fireControl = false;
                    firing = false;
                    feed.set(DoubleSolenoid.Value.kOff);
                }
            }
        }
    }
}
