// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */

  private Servo shooter;

  private void shooterInit(){
    shooter = new Servo(1);
  }

  public Shooter() {
    shooterInit();
    //shooter.set
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void getPosOnDash(){
    SmartDashboard.putNumber("Angle of Servo", shooter.getAngle());
    SmartDashboard.putNumber("Postition of Servo", shooter.getPosition());
  }
}
