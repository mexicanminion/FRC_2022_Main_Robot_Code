// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class Conveyor extends SubsystemBase {
  /** Creates a new Conveyor. */

  TalonFX right;
  TalonFX rightFollower;

  public void conveyorInit(){

    right = new TalonFX(Constants.CANRightConveyorMASTER);
    rightFollower = new TalonFX(Constants.CANRightConveyorFOLLOWER);

    rightFollower.follow(right);
    
    rightFollower.setInverted(true);
  }

  public Conveyor() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void ballIn(){
    right.set(ControlMode.PercentOutput, .5);
  }
  
  /**
   * out takes balls
   */
  public void ballOut(){
    right.set(ControlMode.PercentOutput, -.5);
  }
  
  /**
   * stops intake motor
   */
  public void ballStop(){
    right.set(ControlMode.PercentOutput, 0);
  }

  /**
   * called periodically to update intake
   * used by OP
   * HOLD A - INTAKE
   * HOLD A + RightBumber - OUTTAKE
   */
  public void teleIntake(){
    
    if(Robot.oi.getControllerButtonState(Constants.XBoxButtonA) && Robot.oi.getControllerButtonState(Constants.XBoxButtonTriggerRight)){
      ballOut();
    }else if(Robot.oi.getControllerButtonState(Constants.XBoxButtonA)){
      ballIn();
    }else{
      ballStop();
    }

  }
}
