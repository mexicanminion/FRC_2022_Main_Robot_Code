// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
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
  public static int width;
  public static int hight;
  private static Block mainBlock;
  private static double buffer;

  public Vision() {
    //cam1.startAutomaticCapture(0);
    pixy = Pixy2.createInstance(LinkType.SPI);
    pixy.init();
	hight = pixy.getFrameHeight();
	width = pixy.getFrameWidth();
	buffer = width/2;

    SmartDashboard.putString("PixyCam", "initated");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  private static boolean isAligned(Block block){
	  
	int blockHalf = (int)(x + block.getWidth() / 2);
	if(blockHalf <= width/2 + 50 && blockHalf >= width/2 - 50){
		return true;
	}
	return false;
  }

  public static void getBiggestBlockCARGO(int CargoSignature) {
	//int blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 4);
	/*if (blockCount <= 0) {
		System.err.println("No block count");
	}*/
	ArrayList<Block> blocks = pixy.getCCC().getBlockCache();
	Block largestBlock = null;
	/*if (blocks == null) {
		System.err.println("No Blocks");
	}*/
	for (Block block : blocks) {
		if (block.getSignature() == CargoSignature) {
			if (largestBlock == null) {
				largestBlock = block;
			} else if (block.getWidth() > largestBlock.getWidth()) {
				largestBlock = block;
			}
		}
	}

	if(largestBlock != null){
		x = largestBlock.getX();
		y = largestBlock.getY();
		mainBlock = largestBlock;
		SmartDashboard.putString("Cargo visible?", "Yes");
		SmartDashboard.putNumber("Ball X", largestBlock.getX());
		SmartDashboard.putNumber("Ball Y", largestBlock.getY());
		SmartDashboard.putBoolean("Aligned?", isAligned(largestBlock));
	}else{
		SmartDashboard.putString("Cargo visible?", "No");
	}
		
		
	}

  /*public static Block getBiggestBlockBLUEBALL() {
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
	}*/

	public void alignWithTarget(int CargoSignature){
		boolean isDone = false;
		Robot.driveBase.parkingBrake(true);
		double motorPower;
		double powerFudge = 0;
	
		while (isDone == false) {//instead of isDone, add XBoxB
		  SmartDashboard.putString("align?", "going");
		  
		  getBiggestBlockCARGO(CargoSignature);

		  SmartDashboard.putBoolean("Shooter loop", true);
		  SmartDashboard.putNumber("vision x", x);
		  motorPower = (x - buffer) * Constants.HomingModifier/27;
		  if(Math.abs(motorPower) < 0.1){
			if(motorPower < 0){
			  powerFudge = -0.08;
			}else{
			  powerFudge = 0.08;
			}
			
		  }
		  SmartDashboard.putNumber("motor power (vision)", motorPower);
		  //Robot.driveBase.drive(-motorPower-powerFudge, motorPower+powerFudge);
	
		  if(isAligned(mainBlock)){//(Robot.vision.x > Constants.VisionErrorAllowed){ 
			isDone = true;
		  }else{
			isDone = false;
			//Robot.hopper.wackerSpinOn();
		  }
	
		  if(Robot.oi.getControllerButtonState(Constants.XBoxButtonHome)){
			//Robot.driveBase.drive(0, 0);
			isDone = true;
		  }
	
		}
	}

	private int mathTo0(int x){
		return x - (int)buffer;
	}
}
