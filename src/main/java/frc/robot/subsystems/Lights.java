// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;


public class Lights extends SubsystemBase {
  /** Creates a new Lights. */

  Spark light;
  DriverStation driverStation;

  private void lightsInit(){
    light = new Spark(9);
  }

  public Lights() {
    lightsInit();
    stationaryLight();
  }

  public void stationaryLight(){
    light.set(-0.99);
  }

  public void setMatchLight(){
    if(driverStation.getAlliance() == driverStation.getAlliance().Red){
      light.set(0.03); //Color 1 Pattern Breath Fas 
    }
    else if(driverStation.getAlliance() == driverStation.getAlliance().Blue){
      light.set(0.31); //Color 2 Pattern Breath Fas
    }
    //light.set(0.05);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
