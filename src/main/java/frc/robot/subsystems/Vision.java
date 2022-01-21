// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2.LinkType;

public class Vision extends SubsystemBase {
  /** Creates a new Vision. */

  private CameraServer cam1;
  
  private Pixy2 pixy = Pixy2.createInstance(LinkType.SPI);

  public Vision() {
    cam1.getInstance().startAutomaticCapture(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
