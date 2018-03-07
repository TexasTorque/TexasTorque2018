package org.texastorque.io;

import org.texastorque.auto.AutoManager;
import org.texastorque.auto.playback.HumanInputRecorder;
import org.texastorque.feedback.Feedback;
import org.texastorque.torquelib.util.GenericController;

public class HumanInput extends Input {

	public static HumanInput instance;

	private double lastLeftSpeed = 0;
	private double lastRightSpeed = 0;
	private double leftNegativeTest = 0;
	private double rightNegativeTest = 0;
	
	public GenericController driver;
	public GenericController operator;
	protected OperatorConsole board;
	
	private int PT_test;
	
	public HumanInput(){
		driver = new GenericController(0 , .1);
		operator = new GenericController(1, .1);
		board = new OperatorConsole(2);
		PT_test = 0;
	}
	
	public void update() {
		updateDrive();
//		updateFile();
		updateClaw();
		updateWheelIntake();
		updateBoardSubsystems();
		updateKill();
		if(pickingUp)
			pickingUp = false;
		if(operator.getRawButtonReleased(operator.controllerMap[15]));
		
	}
	
	public void updateDrive(){
		DB_leftSpeed = -driver.getLeftYAxis() + .75 * driver.getRightXAxis();
		DB_rightSpeed = -driver.getLeftYAxis() - .75 * driver.getRightXAxis();
		
	}

	public void updateFile() {
		if(driver.getYButton())
			AutoManager.getInstance();
	}
	
	
	public void updateClaw() {
		CL_closed.calc(operator.getBButton());
	}
	
	public void updateWheelIntake() {

		IN_down.calc(operator.getXButton());
		IN_out.calc(driver.getAButton());
		if(driver.getLeftBumper()) {
			IN_speed = -.5;
		} else if(driver.getRightBumper()) {
			IN_speed = .35;
		} else IN_speed = 0;
	}
	
	public void updateBoardSubsystems() {	
		if(getEncodersDead()) {
			updatePivotArmBackup();
		} else {
		MAXIMUM_OVERDRIVE.calc(board.getButton(10));
		
		if(MAXIMUM_OVERDRIVE.get()) {
			AM_setpoint = board.getSlider() * AM_CONVERSION;
			PT_setpoint = (int)(Math.round(board.getDial() / 0.00787401571)) * 18;			
		} else {
			updateNotManualOverride();
		  } //if not manual override
		} //if encoders not dead
	} //method close

	private void updateNotManualOverride() {
		if(driver.getXButton()) {
			climbing = true;
//			AM_setpoint = 0;
		} else {
			climbing = false;
		}
		if(operator.getLeftCenterButton()) {
			Feedback.getInstance().resetPivot();
		}
		if(operator.getRightCenterButton()) {
			Feedback.getInstance().resetPivot();
		} 
		if(operator.getYButton()) {
			pickingUp = true;
		} else pickingUp = false;
		for (int x = 1; x < 10; x++) {
			if(board.getButton(x)) {
				PT_index = x;
				AM_index = x;
				MAXIMUM_OVERDRIVE.set(false);
				PT_setpoint = PT_setpoints[PT_index];
				AM_setpoint = AM_setpoints[AM_index];
			} 
		}
		if(board.getButton(11)) {
			PT_index = 0;
			AM_index = 0;
			MAXIMUM_OVERDRIVE.set(false);
			PT_setpoint = PT_setpoints[PT_index];
			AM_setpoint = AM_setpoints[AM_index];
		}
	}
	
	private void updatePivotArmBackup() {
		if(operator.getDPADLeft())
			pivotCCW = true;
		else if(operator.getDPADRight()) {
			pivotCW = true;
			pivotCCW = false;
		} else {
			pivotCCW = false;
			pivotCW = false;
		}
		if(operator.getDPADUp()) {
			armFWD = true;
		} else if(operator.getDPADDown()) {
			armBACK = true;
			armFWD = false;
		} else {
			armFWD = false;
			armBACK = false;
		}
	}
	
	public void updateKill() {
		encodersDead.calc(operator.getRightTrigger() && operator.getLeftTrigger());
	}
	public static HumanInput getInstance() {
		return instance == null ? instance = new HumanInput() : instance;
	}
	
}

// Emily Lauren Roth was here!!!