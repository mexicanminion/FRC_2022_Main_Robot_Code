// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.sensors.*;
import com.ctre.phoenix.sensors.PigeonIMU.PigeonState;

import frc.robot.Constants;
import frc.robot.Robot;
/**
 * Add your docs here.
 */
public class Accelerometer extends SubsystemBase {

  public PigeonIMU imu;
  private static String CommandName = "Accelerometer";

  public Accelerometer() {
    imu = new PigeonIMU(Constants.CANPigeon);
  }

  public boolean isPigeonReady(){
    if(imu.getState() == PigeonState.Ready) return true;
    else return false;
  }

  public double getAngleX(){
    //Robot.logMessage(CommandName, "getting angle");
    double[] ypr = new double[3];
    imu.getYawPitchRoll(ypr);
    return ypr[2];
  } 
  public double getAngleY(){
    double[] ypr = new double[3];
    imu.getYawPitchRoll(ypr);
    return ypr[1];
  } 
  public double getAngleZ(){
    double[] ypr = new double[3];
    imu.getYawPitchRoll(ypr);
    SmartDashboard.putNumber("Gyro Yaw...", ypr[0]);
    return ypr[0]; //CCW is positive, CW is neg
  } 

  public short getAccelX(){
    short[] accel = new short[3];
    imu.getBiasedAccelerometer(accel);
    return accel[0];
  } 
  public short getAccelY(){
    short[] accel = new short[3];
    imu.getBiasedAccelerometer(accel);
    return accel[1];
  } 
  public short getAccelZ(){
    short[] accel = new short[3];
    imu.getBiasedAccelerometer(accel);
    return accel[2];
  } 

  public void resetPigeonYaw(){
    imu.setYaw(0);
  }

  public double getHeading(){
    return Math.IEEEremainder(getAngleZ(), 360);
  }

  public void setPigeonYaw(double yaw){
    imu.setYaw(yaw);
  }

}
