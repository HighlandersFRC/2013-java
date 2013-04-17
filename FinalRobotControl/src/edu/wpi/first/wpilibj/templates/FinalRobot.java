/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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

    boolean driver = true;
    boolean driveLock = false;
    double gunnerX;
    double gunnerY;
    double heading;
    LatchedBoolean absCtrlMode = new LatchedBoolean();
    RobotDrive drive = new RobotDrive(1, 2, 4, 3);
    Victor arm = new Victor(9);
    Victor arm2 = new Victor(10);
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Joystick joy3 = new Joystick(3);
    Joystick joy4 = new Joystick(4);
    Victor launch = new Victor(5);
    Victor injector = new Victor(6);
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
    double indexerLen;
    double indexerDel;
    boolean defaultPistonState;
    double autoStartTime;
    int autoState = -1;
    double lastPidTurn = 0;
    double spinUpStartTime;
    Gyro armgyro = new Gyro(1);
    Gyro baseGyro = new Gyro(2);
    Servo indexer = new Servo(7);
    Jaguar articulator = new Jaguar(8);
    DigitalInput limitSwitch = new DigitalInput(2);
    DigitalInput articulatorLimit = new DigitalInput(3);
    SendableChooser autoChooser = new SendableChooser();
    PIDController yawTargetingPid = new PIDController(0.04, 0.01, 0.01, baseGyro, new ImageTrackingPidOutput());
    PIDController armElevationPid = new PIDController(1, 0.05, 0.8, new ElevationTargeterSource(), articulator);
    Solenoid armLock = new Solenoid(1);
    boolean armReleased = false;
    Preferences pref = Preferences.getInstance();
    Object autoMode;

    public class ImageTrackingPidOutput implements PIDOutput {

        public void pidWrite(double output) {
            lastPidTurn = output;
        }
    }

    public class ElevationTargeterSource implements PIDSource {

        public double pidGet() {
            return armgyro.getAngle() - Timer.getFPGATimestamp() * SmartDashboard.getNumber("Drift Coefficient");
        }
    }

    {
        autoChooser.addDefault("None", null);
        autoChooser.addObject("Behind Pyramid", new Double(-4));
        autoChooser.addObject("Back Corner Of Pyramid", new Double(-3));
        autoChooser.addObject("Front Corner Of Pyramid", new Double(-5));
    }

    public void robotInit() {
        drive.setSafetyEnabled(false);
        armgyro.reset();
        baseGyro.reset();
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        System.out.println("init");
        SmartDashboard.putNumber("Launch Power", 55);
        SmartDashboard.putNumber("Injector Power", -100);
        SmartDashboard.putNumber("Injector Pulse Delay", 0.2);
        SmartDashboard.putNumber("Launcher Pulse Delay", 0);
        SmartDashboard.putNumber("Injector Pulse Power", 25);
        SmartDashboard.putNumber("Launcher Pulse Power", 100);
        SmartDashboard.putNumber("Injector Pulse Length", 0.8);
        SmartDashboard.putNumber("Launcher Pulse Length", 0.25);
        SmartDashboard.putNumber("Articulator Power", 50);
        SmartDashboard.putNumber("Shoulder Power", 100);
        SmartDashboard.putNumber("Belt Power", 100);
        SmartDashboard.putNumber("Indexer Delay", 0);
        SmartDashboard.putNumber("Indexer Time", 1);
        SmartDashboard.putNumber("SpinUp Time", 0.6);
        SmartDashboard.putData("autoChooser", autoChooser);
        SmartDashboard.putNumber("azimuth", 0);
        SmartDashboard.putString("Test Table/Test Field", "Test Value");
        SmartDashboard.putBoolean("autonomous", true);
        double voltage = DriverStation.getInstance().getBatteryVoltage();
        //magic code to calculate percent charge from voltage. formula given by quartic regression on empirical data. DO NOT TOUCH
        //DO NOT TOUCH NEXT LINE
        SmartDashboard.putNumber(" initial charge percent", (voltage > 11.7 ? (voltage < 12.6 ? 337.90418311119 * MathUtils.pow(voltage, 4) - 16321.796968975 * MathUtils.pow(voltage, 3) + 295645.26274574 * MathUtils.pow(voltage, 2) - 2379991.6548433 * voltage + 7184291.5749442 : 125) : -10));
        //DO NOT TOUCH PREVIOUS LINE
        yawTargetingPid.setSetpoint(0);
        yawTargetingPid.setInputRange(0, 360);
        yawTargetingPid.setContinuous(true);
        yawTargetingPid.setOutputRange(-0.3, 0.3);
        armElevationPid.setInputRange(-30, 5);
        armElevationPid.setOutputRange(-0.6, 0.6);
        SmartDashboard.putData("YawTargeter PID", yawTargetingPid);
        SmartDashboard.putData("ElevationTargeting PID", armElevationPid);
        System.out.println(NetworkTable.getTable("SmartDashboard").containsSubTable("Test Table"));
    }

    public void autonomousInit() {
        launchPwr = -SmartDashboard.getNumber("Launch Power") / 100;
        autoState = -1;
        autoStartTime = Timer.getFPGATimestamp();
        autoMode = autoChooser.getSelected();
        if (SmartDashboard.getBoolean("autonomous")) {
            wheel = true;
            launch.set(-1);
            injector.set(0.25);
        } else {
            launch.set(0);
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        if (SmartDashboard.getBoolean("autonomous")) {
            iterateFiring();
            if (Timer.getFPGATimestamp() - autoStartTime > 0.6 && autoState == -1) {
                launch.set(-SmartDashboard.getNumber("Launch Power") / 100);
                autoState = 0;
            }
            if (autoState == 0) {
                injector.set(0.25);
            }
            if (limitSwitch.get() && autoState == 0) {
                autoState = 1;
                injector.set(0);
            }
            if (Timer.getFPGATimestamp() - autoStartTime > 5 && autoState == 1) {
                autoState = 2;
                fireControl = true;
            }
            if (Timer.getFPGATimestamp() - autoStartTime > 9 && autoState == 2) {
                autoState = 3;
                fireControl = true;
            }
            if (Timer.getFPGATimestamp() - autoStartTime > 13 && autoState == 3) {
                autoState = 4;
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
         *          if we must change the controls we will notify people.
         */

        SmartDashboard.putNumber("Heading", baseGyro.getAngle());
        yawTargetingPid.setSetpoint(SmartDashboard.getNumber("azimuth"));
        if (driver) {
            drive.mecanumDrive_Cartesian(-joy2.getX(), -joy2.getY(), joy1.getRawButton(1) ? lastPidTurn : joy1.getX(), absCtrlMode.getValue() ? baseGyro.getAngle() : 0);
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
            drive.mecanumDrive_Cartesian(gunnerX, gunnerY, (-baseGyro.getAngle() + heading) / 20, 0);
        }
        if (armReleased) {
            if (joy3.getRawButton(11)) {
                arm.set(-SmartDashboard.getNumber("Shoulder Power") / 100);
            } else if (joy3.getRawButton(10)) {
                arm.set(SmartDashboard.getNumber("Shoulder Power") / 100);
            } else {
                if (Math.abs(joy4.getY()) > 0.05) {
                    arm.set(joy4.getAxis(Joystick.AxisType.kY));
                } else {
                    arm.set(0);
                }
            }
        }
        if (joy3.getRawButton(6)) {
            arm2.set(SmartDashboard.getNumber("Belt Power") / 100);
        } else if (joy3.getRawButton(7)) {
            arm2.set(-SmartDashboard.getNumber("Belt Power") / 100);
        } else {
            arm2.set(0);
        }
        if (!fireControl) {
            launchPwr = -SmartDashboard.getNumber("Launch Power") / 100;
            injPwr = SmartDashboard.getNumber("Injector Power") / 100;
        }
        if ((joy2.getRawButton(2) || joy4.getRawButton(2)) && !wheel) {
            spinUpStartTime = Timer.getFPGATimestamp();
            launch.set(-1);
            wheel = true;
        } else if ((joy2.getRawButton(2) || joy4.getRawButton(2)) && Timer.getFPGATimestamp() < spinUpStartTime + SmartDashboard.getNumber("SpinUp Time")) {
            launch.set(-1);
        } else if ((joy2.getRawButton(2) || joy4.getRawButton(2)) && wheel) {
            launch.set(launchPwr);
            SmartDashboard.putBoolean("LaunchWheel Ready", true);
        } else {
            wheel = false;
            launch.set(0);
            SmartDashboard.putBoolean("LaunchWheel Ready", false);
        }
        iterateFiring();
        if (joy1.getRawButton(1) && !yawTargetingPid.isEnable()) {
            yawTargetingPid.enable();
        } else if (!joy1.getRawButton(1) && yawTargetingPid.isEnable()) {
            yawTargetingPid.disable();
        }
        if (joy3.getRawButton(8) && joy4.getRawButton(9)) {
            armReleased = true;
            armLock.set(true);
        } else {
            armLock.set(false);
        }
    }

    public void testInit() {
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
        if (!fireControl) {

            if (!armElevationPid.isEnable()) {
                if (joy2.getRawButton(11) || joy4.getRawButton(11)) {
                    articulator.set(SmartDashboard.getNumber("Articulator Power") / 100);
                } else if ((joy2.getRawButton(12) || joy4.getRawButton(12)) && !articulatorLimit.get()) {
                    articulator.set(-SmartDashboard.getNumber("Articulator Power") / 100);
                } else {
                    articulator.set(0);
                }
            }
            if (joy2.getRawButton(9) || joy4.getRawButton(9)) {
                indexer.set(0);
            } else if (joy2.getRawButton(10) || joy4.getRawButton(10)) {
                indexer.set(1);
            } else {
                indexer.set(0.5);
            }
            if (joy2.getRawButton(8) || joy4.getRawButton(8)) {
                injector.set(0.25);
            } else if (joy2.getRawButton(7) || joy4.getRawButton(7)) {
                injector.set(-0.25);
            } else {
                injector.set(0);
            }
        }
        if ((joy2.getRawButton(1) || joy4.getRawButton(1)) && !fireControl || (fireControl && !firing)) {
            launchPwr = -SmartDashboard.getNumber("Launch Power") / 100;
            injPwr = SmartDashboard.getNumber("Injector Power") / 100;
            injPulsePwr = SmartDashboard.getNumber("Injector Pulse Power") / 100;
            launchPulsePwr = -SmartDashboard.getNumber("Launcher Pulse Power") / 100;
            injPulseLen = SmartDashboard.getNumber("Injector Pulse Length");
            launchPulseLen = SmartDashboard.getNumber("Launcher Pulse Length");
            injPulseDel = SmartDashboard.getNumber("Injector Pulse Delay");
            launchPulseDel = SmartDashboard.getNumber("Launcher Pulse Delay");
            indexerDel = SmartDashboard.getNumber("Indexer Delay");
            indexerLen = SmartDashboard.getNumber("Indexer Time");
            injector.set(injPwr);
            fireControl = true;
            firing = true;
            fireStartTime = Timer.getFPGATimestamp() + (defaultPistonState ? feedTime : 0);
        }
        if (fireControl) {
            double currTime = Timer.getFPGATimestamp();
            double fireTime = currTime - fireStartTime;
            if (fireTime >= 0) {
            }
            if (fireTime >= injPulseDel && fireTime < injPulseDel + injPulseLen && !limitSwitch.get()) {
                injector.set(injPulsePwr);
            }
            if (fireTime >= launchPulseDel && fireTime < launchPulseDel + launchPulseLen) {
                launch.set(launchPulsePwr);
            }
            if (fireTime >= injPulseDel + injPulseLen || (fireTime >= injPulseDel && limitSwitch.get())) {
                injector.set(0);
            }
            if (fireTime >= launchPulseDel + launchPulseLen) {
                launch.set(launchPwr);
            }
            if (fireTime >= feedTime) {
            }
            if (fireTime >= indexerDel && fireTime < indexerDel + indexerLen) {
                indexer.set(0);
            } else {
                indexer.set(0.5);
            }
            if (fireTime >= Math.max(Math.max(injPulseDel + injPulseLen, launchPulseDel + launchPulseLen), indexerDel + indexerLen)) {
                if (!(joy2.getRawButton(1) || joy4.getRawButton(1))) {
                    fireControl = false;
                    injector.set(0);
                    firing = false;
                }
            }
        }
    }
}
