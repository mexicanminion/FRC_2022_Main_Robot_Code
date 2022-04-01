// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */

  TalonFX intakeMotor;

  public void intakeInit(){

    intakeMotor = new TalonFX(Constants.CANIntakeMotor);
    //intakeMotor.setInverted(true);
  
  }
  public Intake() {
    intakeInit();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void ballIn(){
    intakeMotor.set(ControlMode.PercentOutput, .6);
    SmartDashboard.putString("Intake is ", "Running in");
  }
  
  /**
   * out takes balls
   */
  public void ballOut(){
    intakeMotor.set(ControlMode.PercentOutput, -.6);
    SmartDashboard.putString("Intake is ", "Running out");
  }
  
  /**
   * stops intake motor
   */
  public void ballStop(){
    intakeMotor.set(ControlMode.PercentOutput, 0);
    SmartDashboard.putString("Intake is ", "stopped");
  }

  /**
   * called periodically to update intake
   * used by OP
   * HOLD A - INTAKE
   * HOLD A + RightBumber - OUTTAKE
   */
  public void teleIntake(){
    
    if(Robot.oi.getControllerButtonStateOp(Constants.XBoxButtonA) && Robot.oi.getControllerButtonStateOp(Constants.XBoxButtonTriggerRight)){
      ballOut();
      Robot.conveyor.ballOut();
    }else if(Robot.oi.getControllerButtonStateOp(Constants.XBoxButtonA)){
      Robot.conveyor.cycleCargo();
      ballIn();
    }else{
      ballStop();
      Robot.conveyor.ballStop();
    }

  }
}
