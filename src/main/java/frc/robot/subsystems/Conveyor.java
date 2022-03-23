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

  Ultrasonic ultrasonic;

  AnalogPotentiometer bottom;
  AnalogPotentiometer top; 

  double bottomValue;
  double topValue;
  double conveyorValue;

  conveyorTypes cType; 

  public void conveyorInit(){

    right = new TalonFX(Constants.CANRightConveyorMASTER);
    rightFollower = new TalonFX(Constants.CANRightConveyorFOLLOWER);

    rightFollower.follow(right);
    rightFollower.setInverted(true);

    ultrasonic = new Ultrasonic(0, 1);
    ultrasonic.setAutomaticMode(true);


    bottomInput = new AnalogInput(0);
    bottomInput.setAverageBits(1);

    topInput = new AnalogInput(1);
    topInput.setAverageBits(1);

    bottom = new AnalogPotentiometer(bottomInput, 180, 30);
    top = new AnalogPotentiometer(topInput, 180, 30);

    cType = conveyorTypes.loadingCargo;

  }

  public Conveyor() {
    conveyorInit();
    SmartDashboard.putString("Conveyor Status: ", "stopped");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void updateUltrasonics(){
    bottomValue = bottom.get();
    topValue = top.get();
    conveyorValue = ultrasonic.getRangeMM();

    if(topValue <= 130){
      SmartDashboard.putBoolean("Ball in Shooter? ", true);
    }else{
      SmartDashboard.putBoolean("Ball in Shooter? ", false);
    }

    /*if(bottomValue <= 130){
      SmartDashboard.putBoolean("Ball in Conveyor? ", true);
    }else{
      SmartDashboard.putBoolean("Ball in Conveyor? ", false);
    }*/

    if(conveyorValue <= 500){
      SmartDashboard.putBoolean("Ball in Conveyor? ", true);
    }else{
      SmartDashboard.putBoolean("Ball in Conveyor? ", false);
    }

    //SmartDashboard.putString("Conveyor Status: ", "stopped");
    SmartDashboard.putNumber("Ultarsonic Conveyor", conveyorValue);
    //SmartDashboard.putNumber("Ultarsonic bottom", bottomValue);
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
        SmartDashboard.putString("Conveyor Status: ", "intaking");
        if(topValue <= 130  && conveyorValue <= 500){ //if shooter and conveyor have cargo 
          ballStop();
          cType = conveyorTypes.lockCargo;
        }else{
          ballIn();
        }
        break;
      case lockCargo:
        SmartDashboard.putString("Conveyor Status: ", "locked");
        if(!(topValue <= 130)){
          cType = conveyorTypes.loadingCargo;
        }
        break;
      case shootingCargo:
        if(topValue <= 130  && bottomValue <= 170){
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
          SmartDashboard.putString("Conveyor Status: ", "Finished!");
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

  public void setConyorType(int type){
    if(type == 1){
      cType = conveyorTypes.loadingCargo;
    }
    if(type == 2){
      cType = conveyorTypes.shootingCargo;
    }
  }

  enum conveyorTypes{
    loadingCargo,
    lockCargo,
    shootingCargo
  }
}
