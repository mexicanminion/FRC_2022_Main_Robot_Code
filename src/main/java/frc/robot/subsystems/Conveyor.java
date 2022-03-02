// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class Conveyor extends SubsystemBase {
  /** Creates a new Conveyor. */

  TalonFX right;
  TalonFX rightFollower;

  AnalogInput bottomInput;
  AnalogInput topInput;

  AnalogPotentiometer bottom;
  AnalogPotentiometer top; 

  double bottomValue;
  double topValue;

  conveyorTypes cType = conveyorTypes.loadingCargo;
  

  public void conveyorInit(){

    right = new TalonFX(Constants.CANRightConveyorMASTER);
    rightFollower = new TalonFX(Constants.CANRightConveyorFOLLOWER);

    rightFollower.follow(right);
    rightFollower.setInverted(true);

    bottomInput = new AnalogInput(0);
    bottomInput.setAverageBits(2);

    topInput = new AnalogInput(1);
    topInput.setAverageBits(2);

    bottom = new AnalogPotentiometer(bottomInput, 180, 30);
    top = new AnalogPotentiometer(topInput, 180, 30);

  }

  public Conveyor() {
    conveyorInit();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void updateUltrasonics(){
    bottomValue = bottom.get();
    topValue = top.get();
    SmartDashboard.putNumber("Ultarsonic bottom", bottomValue);
    SmartDashboard.putNumber("Ultarsonic top", topValue);
  }

  public void ballIn(){
    right.set(ControlMode.PercentOutput, .2);
  }
  
  /**
   * out takes balls
   */
  public void ballOut(){
    right.set(ControlMode.PercentOutput, -.2);
  }
  
  /**
   * stops intake motor
   */
  public void ballStop(){
    right.set(ControlMode.PercentOutput, 0);
  }

  public void cycleCargo(){
    updateUltrasonics();
    switch(cType){
      case loadingCargo:
        if(topValue <= 130  && bottomValue <= 130){ //if shooter and conveyor have cargo 
          ballStop();
        }else{
          ballIn();
        }
        break;
      case shootingCargo:
        if(topValue <= 130  && bottomValue <= 130){
          SmartDashboard.putString("Conveyor Status: ", "shooting");
          Robot.shooter.goToPos(1);
          for(int i = 0; i <= 1000; i++){
            try{
              Thread.sleep(1);
            }catch(Exception ex){
            }
          }
          Robot.shooter.goToPos(0);
          while(topValue >= 130){
            ballIn();
          }
          Robot.shooter.goToPos(1);
          for(int i = 0; i <= 1000; i++){
            try{
              Thread.sleep(1);
            }catch(Exception ex){
            }
          }
          Robot.shooter.goToPos(0);
        }else{
          SmartDashboard.putString("Conveyor Status: ", "Cant shoot, balls are not in both places");
        }
        break;
    }
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

  enum conveyorTypes{
    loadingCargo,
    shootingCargo
  }
}
