// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;

import java.lang.Math.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.math.geometry.*;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;


public class DriveBase extends SubsystemBase {
  /** Creates a new DriveBase. */

  TalonFX rightFront;
  TalonFX rightFollower;
  TalonFX leftFront;
  TalonFX leftFollower;


  private double leftCurrentPercentJoystick = 0.0;
  private double rightCurrentPercentJoystick = 0.0;
  private double leftCurrentPercentAuto = 0.0;
  private double rightCurrentPercentAuto = 0.0;
  private double leftCurrentPercent = 0.0;
  private double rightCurrentPercent = 0.0;
  private int decelLoopCount = 0;
  private boolean autoFlag = false;
  private boolean velFlag = false;
  private boolean speedShift = false;

  private double leftSpeedFinal = 0;
  private double rightSpeedFinal = 0;
  private double targetHeading = 0;

  private void driveBaseInit(){

    leftFront = new TalonFX(Constants.CANLeftFrontMaster); 
    leftFollower = new TalonFX(Constants.CANLeftFrontFollower); 
    rightFront = new TalonFX(Constants.CANRightFrontMaster); 
    rightFollower = new TalonFX(Constants.CANRightFrontFollower);

    leftFront.setInverted(false);
    leftFollower.setInverted(false);
    rightFront.setInverted(true);
    rightFollower.setInverted(true);

    leftFront.setNeutralMode(NeutralMode.Coast);
    leftFollower.setNeutralMode(NeutralMode.Coast);
    rightFront.setNeutralMode(NeutralMode.Coast);
    rightFollower.setNeutralMode(NeutralMode.Coast);

    rightFollower.follow(rightFront);
    leftFollower.follow(leftFront);

    leftFront.configFactoryDefault();
    leftFollower.configFactoryDefault();
    rightFront.configFactoryDefault();
    rightFollower.configFactoryDefault();

    rightFront.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,0,30);
    leftFront.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,0,30);

    leftFront.clearStickyFaults();
    leftFollower.clearStickyFaults();
    rightFront.clearStickyFaults();
    rightFollower.clearStickyFaults();

    // rightFront.setSelectedSensorPosition(0);
    // leftFront.setSelectedSensorPosition(0);


    //leftFront.config_kF(0, Constants.DrivePIDkF, Constants.kTimeoutMs);
		leftFront.config_kP(0, Constants.DrivePIDkP, Constants.kTimeoutMs);
		leftFront.config_kI(0, Constants.DrivePIDkI, Constants.kTimeoutMs);
    //leftFront.config_kD(0, Constants.DrivePIDkD, Constants.kTimeoutMs);
    //^^ commmented and broke code  

    //rightFront.config_kF(0, Constants.DrivePIDkF, Constants.kTimeoutMs);
		rightFront.config_kP(0, Constants.DrivePIDkP, Constants.kTimeoutMs);
		rightFront.config_kI(0, Constants.DrivePIDkI, Constants.kTimeoutMs);
		//rightFront.config_kD(0, Constants.DrivePIDkD, Constants.kTimeoutMs);

    rightFront.set(TalonFXControlMode.Velocity, 0);
    leftFront.set(TalonFXControlMode.Velocity, 0);
  }

  public DriveBase() {
    driveBaseInit();
    zeroEncoders();
    fullStop();
  }

  public void parkingBrake(boolean isOn){

    if(isOn == true){
      leftFront.setNeutralMode(NeutralMode.Brake);
      leftFollower.setNeutralMode(NeutralMode.Brake);
      rightFront.setNeutralMode(NeutralMode.Brake);
      rightFollower.setNeutralMode(NeutralMode.Brake);  
    }else{
      leftFront.setNeutralMode(NeutralMode.Coast);
      leftFollower.setNeutralMode(NeutralMode.Coast);
      rightFront.setNeutralMode(NeutralMode.Coast);
      rightFollower.setNeutralMode(NeutralMode.Coast);
    }
  
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void runToPos(double pos, double speed){
    //-1 to 1 speed, converts to RPM
    double leftTick = 0;
    double rightTick = 0;
    boolean done = false;

    double currSpeed = speed;

    
    pos = (pos/Constants.WheelInchPerTicks);
    double wantedPos = pos + Constants.totalDistance;
    
    SmartDashboard.putNumber("wanted pos total", wantedPos);
    SmartDashboard.putNumber("wanted pos current", pos);

    double avg = 0;
    double theThird = (wantedPos/4) * 3;

    //System.out.println("Ran once");

    while(done == false){

      //System.out.println("Running");

      leftTick = leftFront.getSelectedSensorPosition();
      rightTick = rightFront.getSelectedSensorPosition();
      SmartDashboard.putNumber("right tick", rightTick);
      SmartDashboard.putNumber("left tick", leftTick);

      avg = (leftTick + rightTick) / 2;
      

      SmartDashboard.putNumber("Average of both encoders", avg);

      /*if(((avg >= theThird) && speed > 0) || ((avg <= theThird) && speed < 0)){
        currSpeed = currSpeed / 3;
      }*/

      if(Math.abs(wantedPos-avg) <= 30){
        //System.out.println("Case1");
        done = true;
        Constants.totalDistance += pos;
      }else if((avg > wantedPos) && speed > 0){
        done = true;
        Constants.totalDistance += pos;
      }else if((avg < wantedPos) && speed < 0){
        done = true;
        Constants.totalDistance += pos;
      }

      drive(currSpeed, currSpeed);

    } 

    fullStop();
    //zeroEncoders();

    /*double driveTicks = driveInches * Constants.WheelInchPerTicks;
    rightFront.set(TalonFXControlMode.Position, driveTicks);
    leftFront.set(TalonFXControlMode.Position, driveTicks);
    
    old concept code DONT USE
    */
  }

  public void drive(double leftSpeed, double rightSpeed) {

    SmartDashboard.putNumber("curr vel left", leftSpeedFinal);
    SmartDashboard.putNumber("curr vel right", rightSpeedFinal);

    SmartDashboard.putNumber("Target vel left", leftSpeed);
    SmartDashboard.putNumber("Target vel right", rightSpeed);

    //final is current
    //speed is joy
    leftSpeedFinal= setSpeed(leftSpeed, leftSpeedFinal);
    rightSpeedFinal = setSpeed(rightSpeed, rightSpeedFinal);

    if(velFlag == true){
      rightFront.set(TalonFXControlMode.Velocity, rightSpeedFinal* Constants.FalconVelRatio* Constants.MaxRPM);
      leftFront.set(TalonFXControlMode.Velocity, leftSpeedFinal* Constants.FalconVelRatio* Constants.MaxRPM);
    }else{
      rightFront.set(TalonFXControlMode.PercentOutput, rightSpeedFinal);
      leftFront.set(TalonFXControlMode.PercentOutput, leftSpeedFinal);
      
    }
    
  }

  public void driveWithJoySticks(double left, double right){

    if(Robot.oi.getControllerButtonState(Constants.XBoxButtonTriggerRight)){
      drive((right/2) * -1, (left/2)* -1);
    }
    else if(Robot.oi.getControllerButtonState(Constants.XBoxButtonA)){
      drive(.4,.4);
    }else{
      if(Robot.oi.getControllerButtonState(Constants.XBoxButtonTriggerLeft)){
        drive(left/1.33, right/1.33);
      }else{
        drive(left/2.5, right/2.5);
      } 
    }
  }

  public void gyroRotate(double angleYaw){
    boolean isDone = false;
    double angle = angleYaw;
    double currAngle = Robot.imu.getAngleZ();
    double angleDiff = angle - currAngle;
    double currAngleDiff;
    double speed = 0;
    double localMod = Constants.AutoRotatekP;
    while(!isDone){
        currAngle = Robot.imu.getAngleZ();
        currAngleDiff = angle - currAngle;
        if(Math.abs(currAngleDiff) >= 90)speed = localMod;
        else if(Math.abs(currAngleDiff) >= 30)speed = Math.abs(currAngleDiff/angleDiff) * localMod * Constants.AutoSlowDown + Constants.AutoRotateConstant;
        if(currAngleDiff < -Constants.AutoRotateError){
            Robot.driveBase.drive(speed, -speed);
        }else if(currAngleDiff > Constants.AutoRotateError){
            Robot.driveBase.drive(-speed, speed);
        }else{
            isDone = true;
            Robot.driveBase.drive(0, 0);
        }
        //Robot.driveBase.updateDriveTrain();
    }
    Robot.driveBase.fullStop();
  }

  public void fullStop(){
    leftSpeedFinal= 0;
    rightSpeedFinal = 0;

    leftFront.set(TalonFXControlMode.PercentOutput,0);
    rightFront.set(TalonFXControlMode.PercentOutput,0);
  }

  public void setVelFlag(boolean setFlag){
    velFlag = setFlag;
  }

  public void zeroEncoders() {
    leftFront.setSelectedSensorPosition(0);
    rightFront.setSelectedSensorPosition(0);
  }

  public double setSpeed(double speed, double fin){
    if((speed > 0) && (fin < 0) || (speed  < 0) && (fin > 0)){

      if(speed > fin){
        fin  = fin + Constants.MaxDecel;//accel before Decel change
      }else if(speed < fin){
        fin = fin - Constants.MaxDecel;
      }

    }else if(speed == 0){
      if(speed > fin){
        fin  = fin + Constants.MaxDecel;//accel before Decel change
      }else if(speed < fin){
        fin = fin - Constants.MaxDecel;
      }

    }else if(speed > 0){

      if(speed > fin){
        fin = fin + Constants.MaxAccel;
      }else if(speed < fin){
        fin = fin - Constants.MaxDecel;
      }

    }else{ 

      if(speed > fin){
        fin = fin + Constants.MaxDecel;
      }else if(speed < fin){
        fin = fin - Constants.MaxAccel;
      }

    }
    return fin;
  }

  private void driveHeading(double speed){
    int error;
    double currentHeading;
    double speedCorrection = 0;
    double leftSpeed;
    double rightSpeed;
    double gain = .06;


    if(Robot.oi.getControllerButtonState(Constants.XBoxButtonA)) {
      //Find where we are currently pointing
      currentHeading = Robot.imu.getAngleZ();

      //Calculate how far off we are
      error = (int)(currentHeading - targetHeading);

      //Using the error calculate some correction factor
      speedCorrection = error * gain;

      //Adjust the left and right power to try and compensate for the error
      leftSpeed = speed + speedCorrection;
      rightSpeed = speed - speedCorrection;

      //Apply the power settings to the motors
      drive(leftSpeed,rightSpeed);
    }else{
      targetHeading = Robot.imu.getAngleZ();
    }
  }

  public void driveForward(){
    if(Robot.oi.getControllerButtonState(Constants.XBoxButtonA)){
      drive(.4,.4);
    }
  }

  public void updateDriveBase(){
    //speedShift();
    //driveForward();
    driveWithJoySticks(Robot.oi.getControllerStickLeft(), Robot.oi.getControllerStickRight());
  }

}
