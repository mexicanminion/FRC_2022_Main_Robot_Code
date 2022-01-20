// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
//import frc.robot.commands.*;
//import edu.wpi.first.wpilibj.GenericHID;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

//  private Joystick secondaryJoystick = new Joystick(1);
  public XboxController driveJoystick = new XboxController(Constants.MainController);
  public XboxController opJoystick = new XboxController(1);

  private double leftXOffSet = 0;
  private double leftYOffSet = 0;
  private double rightXOffSet = 0;
  private double rightYOffSet = 0;

  //ToDo : Need to check button assignments
  /*Button ButtonLiftOut =               new JoystickButton(driveJoystick, Constants.LiftExtendButton);//ToDo : Check which is in and which is out
  Button ButtonLiftIn =                new JoystickButton(driveJoystick, Constants.LiftRetractButton);//ToDo : pneumatics need constants defined correctly
  Button ButtonLiftUp =                new JoystickButton(driveJoystick,Constants.LiftUpButton);
  Button ButtonLiftDown =              new JoystickButton(driveJoystick,Constants.LiftDownButton);
//  Button ButtonTest =                  new JoystickButton(buttonBoard, Constants.TestButton);
  Button ButtonSetDriveHeightButton =  new JoystickButton(buttonBoard, Constants.SetDriveHeightButton);
  Button ButtonIntakeIn =              new JoystickButton(buttonBoard,Constants.CargoIntakeInButton);
  Button ButtonIntakeOut =             new JoystickButton(buttonBoard,Constants.CargoIntakeOutButton);
  Button ButtonCargoInGround =         new JoystickButton(buttonBoard,Constants.CargoRetrieveGroundButton);
  Button ButtonCargoInDepot =          new JoystickButton(buttonBoard,Constants.CargoRetrieveDepotButton);
  Button ButtonCargoOutLow =           new JoystickButton(buttonBoard,Constants.CargoDepositLowButton);
  Button ButtonCargoOutMid =           new JoystickButton(buttonBoard,Constants.CargoDepositMidButton);
  Button ButtonCargoOutHigh =          new JoystickButton(buttonBoard,Constants.CargoDepositHighButton);
  Button ButtonCargoOutRover =         new JoystickButton(buttonBoard,Constants.CargoDepositRoverButton);
  Button ButtonHatchOutLow =           new JoystickButton(buttonBoard,Constants.HatchDepositLowButton);
  Button ButtonHatchOutMid =           new JoystickButton(buttonBoard,Constants.HatchDepositMidButton);
  Button ButtonHatchOutHigh =          new JoystickButton(buttonBoard,Constants.HatchDepositHighButton);
  Button ButtonHatchInLow =            new JoystickButton(buttonBoard,Constants.HatchRetrieveDepotButton);
  Button ButtonHatchLift =             new JoystickButton(buttonBoard,Constants.HatchLiftButton);
  Button ButtonHatchDrop =             new JoystickButton(buttonBoard,Constants.HatchDropButton);
  Button ButtonHabClimb =              new JoystickButton(driveJoystick, Constants.HabClimbButton);*/


  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
//    private static XboxController controllerDr;
  //  private static Button LiftUp;

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create cusLift triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
  public OI(){
    //zeroJoySticks();

    //driveJoystick.getPOV();
 /*   if (Robot.useJoysticks() == true){
      ButtonLiftIn.whenPressed(       new TogglePneumatics(false));
      ButtonLiftOut.whenPressed(      new TogglePneumatics(true));
    }*/
  }

  public void zeroJoySticks(){
    leftXOffSet = driveJoystick.getLeftX();//getX(Hand.kLeft);
    leftYOffSet = driveJoystick.getLeftY();//getY(Hand.kLeft);
    rightXOffSet = driveJoystick.getRightX();//getX(Hand.kRight);
    rightYOffSet = driveJoystick.getRightY();//getY(Hand.kRight);

    SmartDashboard.putNumber("leftXOffset", leftXOffSet);
    SmartDashboard.putNumber("leftYOffset", leftYOffSet);
    SmartDashboard.putNumber("rightXOffset", rightXOffSet);
    SmartDashboard.putNumber("rightYOffset", rightYOffSet);
  }

  public double getControllerStickLeft(){
    double hand = -driveJoystick.getLeftY() + leftYOffSet;//getY(Hand.kLeft) + leftYOffSet;
    SmartDashboard.putNumber("Raw - offset left joy", hand);
    
    if(Math.abs(hand)< .04){
      hand = 0;
    }
    //return -driveJoystick.getY(Hand.kLeft);
    return hand;

  }

  public double getControllerStickRight(){
    double hand = -driveJoystick.getRightY() + rightYOffSet;//getY(Hand.kRight) + rightYOffSet;
    SmartDashboard.putNumber("Raw - offset right joy", hand);
    if(Math.abs(hand)< .04){
      hand = 0;
    }

    //return -driveJoystick.getY(Hand.kRight);
    return hand;

  }

  public boolean getControllerButtonState(int buttonID){

    return driveJoystick.getRawButton(buttonID);

  }

  public boolean getControllerButtonStateOp(int buttonID){

    return opJoystick.getRawButton(buttonID);

  }

  public double getControllerTriggerLeft(){

    return driveJoystick.getLeftTriggerAxis();//getTriggerAxis(Hand.kLeft);

  }

  public double getControllerTriggerLeftOp(){

    return opJoystick.getLeftTriggerAxis();//getTriggerAxis(Hand.kLeft);

  }

  public double getControllerTriggerRight(){

   return driveJoystick.getRightTriggerAxis();//getTriggerAxis(Hand.kRight);

  }

  public double getControllerTriggerRightOp(){

   return opJoystick.getRightTriggerAxis();//getTriggerAxis(Hand.kRight);

  }

  public int getPOV(){

    return driveJoystick.getPOV();

  }

  public int getPOVOp(){

    return opJoystick.getPOV();

  }

}  