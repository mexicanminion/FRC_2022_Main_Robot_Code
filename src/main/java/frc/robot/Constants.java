// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    //motor CANS
    public static final int    CANLeftFrontMaster    = 7;
	public static final int    CANRightFrontMaster   = 9;
	public static final int    CANLeftFrontFollower  = 8;
	public static final int    CANRightFrontFollower = 10;
    public static final int    CANPigeon             = 24;


    public static final int MainController = 0;

    //DriveBase varibles
    public static final int     WheelCountsPerRev     = 2048;
	public static final double  WheelDiameter         = 6;//6 inch diameter
	public static final double  WheelCircumference    = Math.PI*WheelDiameter;
    public static final double  GearRatio             = 1/5.95;
    public static final double  WheelInchPerTicks     = WheelCircumference / WheelCountsPerRev * GearRatio; //double check gear ratio
    public static final double  DrivePIDkP            = 0.1; //porpotinal differnce
	public static final double  DrivePIDkI            = 0.001; //intergral differnece over time
    public static final int     kTimeoutMs            = 30;
    public static final double  MaxAccel              = 0.025; // 
	public static final double  MaxDecel              = 0.02; // 
    public static final int     MaxRPM                = 4000;
    public static final double  AutoRotateError       = 0.5;
    public static final double  AutoRotatekP		  = 0.75;
    public static final double  AutoSlowDown		  = 0.25;
    public static final double  AutoRotateConstant	  = 0.1;




    //Falcon Specific
    public static final double  FalconVelRatio        = 2048/600;

}
