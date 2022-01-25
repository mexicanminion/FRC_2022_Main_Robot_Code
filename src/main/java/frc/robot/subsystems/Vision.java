// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2.LinkType;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;

public class Vision extends SubsystemBase {
  /** Creates a new Vision. */

  private CameraServer cam1;
  
  private static Pixy2 pixy;
  public static double x;
  public static double y;

  public Vision() {
    //cam1.getInstance().startAutomaticCapture(0);
    pixy = Pixy2.createInstance(LinkType.SPI);
    pixy.init();
    SmartDashboard.putString("PixyCam", "initated");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public static void getBiggestBlockREDBALL() {
	  
	int blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 4);
	if (blockCount <= 0) {
		System.err.println("No block count");
	}
	ArrayList<Block> blocks = pixy.getCCC().getBlockCache();
	Block largestBlock = null;
	if (blocks == null) {
		System.err.println("No Blocks");
	}
	for (Block block : blocks) {
		if (block.getSignature() == Constants.BlueBallSignature) {
			if (largestBlock == null) {
				largestBlock = block;
			} else if (block.getWidth() > largestBlock.getWidth()) {
				largestBlock = block;
			}
		}
	}

	x = largestBlock.getX();
	y = largestBlock.getY();
	SmartDashboard.putNumber("Ball X", largestBlock.getX());
	SmartDashboard.putNumber("Ball Y", largestBlock.getY());
	}

  public static Block getBiggestBlockBLUEBALL() {
		// Gets the number of "blocks", identified targets, that match signature 1 on the Pixy2,
		// does not wait for new data if none is available,
		// and limits the number of returned blocks to 4, for a slight increase in efficiency
		int blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG2, 4);
		System.out.println("Found " + blockCount + " blocks!"); // Reports number of blocks found
		if (blockCount <= 0) {
			return null; // If blocks were not found, stop processing
		}
		ArrayList<Block> blocks = pixy.getCCC().getBlockCache(); // Gets a list of all blocks found by the Pixy2
		Block largestBlock = null;
		for (Block block : blocks) { // Loops through all blocks and finds the widest one
			if (largestBlock == null) {
				largestBlock = block;
			} else if (block.getWidth() > largestBlock.getWidth()) {
				largestBlock = block;
			}
		}
		return largestBlock;
	}

}
