/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package targeting;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Shooter extends IterativeRobot {

    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Victor launch = new Victor(5);
    Victor injector = new Victor(6);
    Compressor comp = new Compressor(1, 1);
    boolean wheel = false;
    RobotDrive drive = new RobotDrive(1, 2, 4, 3);
    LatchedBoolean hoppercontrol = new LatchedBoolean();
    DigitalInput limitSwitch = new DigitalInput(2);
    Servo indexer = new Servo(7);
    Jaguar articulator = new Jaguar(8);
    boolean fireControl = false;
    int fireState;
    double injPwr;
    double launchPwr;
    double injPulsePwr;
    double launchPulsePwr;
    double injPulseLen;
    double launchPulseLen;
    double injPulseDel;
    double launchPulseDel;
    double feedTime;
    double startTime;
    double indexerLen;
    double indexerDel;
    double lastPidTurn = 0;
    double spinUpStartTime;
    Gyro armgyro = new Gyro(1);
    Gyro baseGyro = new Gyro(2);
    int frameCount = 0;
    PIDController yawTargetingPid = new PIDController(0.04, 0.01, 0.01, baseGyro, new ImageTrackingPidOutput());
    PIDController armElevationPid = new PIDController(1, 0.05, 0.8, new ElevationTargeterSource(), articulator);

    public class ImageTrackingPidSource implements PIDSource {

        public double pidGet() {
            double azimuth = SmartDashboard.getNumber("azimuth");
            return (azimuth < 180) ? azimuth : azimuth - 360;
        }
    }

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

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("init");
        drive.setSafetyEnabled(false);
        armgyro.reset();
        baseGyro.reset();
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        SmartDashboard.putNumber("Arm Angle", armgyro.getAngle());
        SmartDashboard.putNumber("Heading", baseGyro.getAngle());
        SmartDashboard.putData("ArmGyro", armgyro);
        SmartDashboard.putNumber("Drift Coefficient", -0.028);
        SmartDashboard.putNumber("Launch Power", 55);
        SmartDashboard.putNumber("Injector Power", 100);
        SmartDashboard.putNumber("Injector Pulse Delay", 0.140);
        SmartDashboard.putNumber("Launcher Pulse Delay", 0);
        SmartDashboard.putNumber("Injector Pulse Power", -25);
        SmartDashboard.putNumber("Launcher Pulse Power", 100);
        SmartDashboard.putNumber("Injector Pulse Length", 0.5);
        SmartDashboard.putNumber("Launcher Pulse Length", 0.25);
        SmartDashboard.putNumber("Indexer Delay", 0.1);
        SmartDashboard.putNumber("Indexer Time", 1.3);
        SmartDashboard.putNumber("SpinUp Time", 0.15);
        SmartDashboard.putNumber("Articulator Power", 100);
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
        System.out.println(NetworkTable.getTable("SmartDashboard").containsSubTable("Shooter"));
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
//        System.out.println("teleop");
        drive.mecanumDrive_Cartesian(joy2.getX(), -joy2.getY(), joy1.getRawButton(1) ? lastPidTurn : joy1.getX(), 0);
        SmartDashboard.putNumber("Arm Angle", armgyro.getAngle() - Timer.getFPGATimestamp() * SmartDashboard.getNumber("Drift Coefficient"));
        SmartDashboard.putNumber("Heading", baseGyro.getAngle());
        yawTargetingPid.setSetpoint(SmartDashboard.getNumber("azimuth"));
        if (joy2.getRawButton(9)) {
            indexer.set(0);
        } else if (joy2.getRawButton(10)) {
            indexer.set(1);
        } else {
            indexer.set(0.5);
        }
        if (!armElevationPid.isEnable()) {
            if (joy2.getRawButton(11)) {
                articulator.set(SmartDashboard.getNumber("Articulator Power") / 100);
            } else if (joy2.getRawButton(12)) {
                articulator.set(-SmartDashboard.getNumber("Articulator Power") / 100);
            } else {
                articulator.set(0);
            }
        }
        if (!fireControl) {
            launchPwr = SmartDashboard.getNumber("Launch Power") / 100;
            injPwr = SmartDashboard.getNumber("Injector Power") / 100;
            if (joy2.getRawButton(2) && !wheel) {
                spinUpStartTime = Timer.getFPGATimestamp();
                launch.set(1);
            } else if (joy2.getRawButton(2) && Timer.getFPGATimestamp() < spinUpStartTime + SmartDashboard.getNumber("SpinUp Time")) {
                launch.set(1);
            } else if (joy2.getRawButton(2) && wheel) {
                launch.set(launchPwr);
            } else {
                wheel = false;
                launch.set(0);
            }
            if (joy2.getRawButton(8)) {
                injector.set(0.25);
//                System.out.println("fwd");
            } else if (joy2.getRawButton(7)) {
                injector.set(-0.25);
//                System.out.println("back");
            } else {
                injector.set(0);
//                System.out.println("off");
            }
        }
        if (joy2.getRawButton(1) && !fireControl) {
            System.out.println("fireStart");
            injPulsePwr = SmartDashboard.getNumber("Injector Pulse Power") / 100;
            launchPulsePwr = SmartDashboard.getNumber("Launcher Pulse Power") / 100;
            injPulseLen = SmartDashboard.getNumber("Injector Pulse Length");
            launchPulseLen = SmartDashboard.getNumber("Launcher Pulse Length");
            injPulseDel = SmartDashboard.getNumber("Injector Pulse Delay");
            launchPulseDel = SmartDashboard.getNumber("Launcher Pulse Delay");
            indexerDel = SmartDashboard.getNumber("Indexer Delay");
            indexerLen = SmartDashboard.getNumber("Indexer Time");
            injector.set(injPwr);
            fireControl = true;
            fireState = 0;
            startTime = Timer.getFPGATimestamp();
        }
        if (fireControl) {
//                System.out.println("firing");
            double currTime = Timer.getFPGATimestamp();
            double fireTime = currTime - startTime;
            if (fireTime >= 0) {
//                    feed.set(DoubleSolenoid.Value.kForward);
            }
            if (fireTime >= injPulseDel && fireTime < injPulseDel + injPulseLen) {
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
//                    feed.set(defaultPistonState ? DoubleSolenoid.Value.kOff : DoubleSolenoid.Value.kReverse);
            }
            if (fireTime >= indexerDel && fireTime < indexerDel + indexerLen) {
                indexer.set(0);
            } else {
                indexer.set(0.5);
            }
            if (fireTime >= Math.max(Math.max(injPulseDel + injPulseLen, launchPulseDel + launchPulseLen), indexerDel + indexerLen)) {
                if (!joy1.getRawButton(1)) {
                    fireControl = false;
//                        feed.set(DoubleSolenoid.Value.kOff);
                }
            }
        }
        boolean found = SmartDashboard.getBoolean("found");
        if (joy1.getRawButton(1) && !yawTargetingPid.isEnable()) {
            yawTargetingPid.enable();
        } else if (yawTargetingPid.isEnable()) {
            yawTargetingPid.disable();
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
