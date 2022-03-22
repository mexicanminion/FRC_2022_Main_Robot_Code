// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class Winch extends SubsystemBase {
  /** Creates a new Winch. */
  Spark winch;

  /**
   * called once to set up winch
   */
  private void initWinch(){
    winch = new Spark(Constants.CANWinchMotor);//VictorSPX firmware updated
  }

  /**
   * constructor
   */
  public Winch() {
    initWinch();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * called periodcially to update winch
   * Used by OP
   * HOLD Dpad UP - ACTIVATE
   */
  public void winchUp(){
    if(Robot.oi.getPOVOp() == 0){
      winch.set(-.5);
    }else{
      winch.set(0);
    }
  }

}