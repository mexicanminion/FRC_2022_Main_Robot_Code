// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */

  private Servo shooter;

  private void shooterInit(){
    shooter = new Servo(0);
    shooter.setSpeed(1);
  }

  public Shooter() {
    shooterInit();
    shooter.setAngle(102);
    //shooter.set
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void goToPos(int pos){
    if(pos == 0){
      shooter.setAngle(25);
    }else if(pos == 1){
      shooter.setAngle(102);
    }
  }

  

  public void depositCargo(){
    if(Robot.oi.getControllerButtonStateOp(Constants.XBoxButtonB)){
      //Robot.conveyor.setConyorType(2);
      //Robot.conveyor.cycleCargo();
      shooter.setAngle(25);
    }else{
      shooter.setAngle(102);
    }
    //SmartDashboard.putNumber("Angle of Servo", shooter.getAngle());
    //SmartDashboard.putNumber("Postition of Servo", shooter.getPosition());
  }
}
