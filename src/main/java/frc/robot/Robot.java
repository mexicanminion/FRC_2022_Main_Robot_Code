// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Accelerometer;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Winch;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  public static OI oi;
  public static Accelerometer imu;
  public static DriveBase driveBase;
  public static Vision vision;
  public static Intake intake;
  public static Shooter shooter;
  public static Winch winch;
  public static Conveyor conveyor;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    oi = new OI();
    imu = new Accelerometer();
    vision = new Vision();
    intake = new Intake();
    driveBase = new DriveBase();
    shooter = new Shooter();
    winch = new Winch();
    conveyor = new Conveyor();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    driveBase.parkingBrake(false);
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link OI} class. */
  @Override
  public void autonomousInit() {
    SmartDashboard.putBoolean("auto done?", false);
    driveBase.zeroEncoders();
    driveBase.parkingBrake(true);
    imu.resetPigeonYaw();
    shooter.goToPos(0);
      for(int i = 0; i <= 3000; i++){
        try{
          Thread.sleep(1);
        }catch(Exception ex){
        }
      }
    shooter.goToPos(1);
    driveBase.runToPos(90, .2);
    //driveBase.runToPos(-40, -.2);
    //driveBase.runToPos(10, .2);
    //driveBase.gyroRotate(-90);
    //driveBase.gyroRotate(90);
    //driveBase.gyroRotate(0);
    SmartDashboard.putBoolean("auto done?", true);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    driveBase.fullStop();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    driveBase.parkingBrake(true);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    conveyor.updateUltrasonics();
    driveBase.updateDriveBase();//DRIVER joysticks
    intake.teleIntake();//OP a to intake
    shooter.depositCargo();//OP b to shoot
    winch.winchUp();
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {

    vision.getBiggestBlockCARGO(Constants.RedBallSignature);

  }

  private void threeBallPath(){
    //servoDrop()
    driveBase.runToPos(10, .5);
    driveBase.gyroRotate(21);
    //turn on intake
    intake.ballIn();
    driveBase.runToPos(82, .5);
    intake.ballStop();
    //turn off intake and sort ball to first slot of conveyor
    driveBase.gyroRotate(-137);
    //turn on intake
    intake.ballIn();
    driveBase.runToPos(97, .5);
    //turn off intake and sort ball to first slot of conveyor
    intake.ballStop();
    driveBase.gyroRotate(79);
    driveBase.runToPos(-100, - .5);
    //servoDrop() both balls
    driveBase.runToPos(90, .5);

  }

  private void twoBallBottom(){
    intake.ballIn();
    driveBase.runToPos(48, .5);
    intake.ballStop();
    driveBase.runToPos(-78, -.5);
    driveBase.gyroRotate(-21);
    driveBase.runToPos(-10, -.5);
    //drop balls
    driveBase.gyroRotate(42);
    driveBase.runToPos(78, .5);
  }

  private void twoBallTop(){
    intake.ballIn();
    driveBase.runToPos(50, .5);
    intake.ballStop();
    driveBase.gyroRotate(25);
    driveBase.runToPos(-28, -.5);
    //drop balls
    driveBase.runToPos(88, .5);
  }

  private void autoTest(){
    intake.ballIn();
    driveBase.runToPos(12*5, .3);
    intake.ballStop();
    driveBase.runToPos(-12*5, -.3);
    intake.ballIn();
    driveBase.runToPos(12*10, .3);
    intake.ballStop();
    driveBase.runToPos(-12*10, -.3);
  }
}
