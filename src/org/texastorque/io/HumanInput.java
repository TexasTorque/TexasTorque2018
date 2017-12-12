package org.texastorque.io;

import org.texastorque.torquelib.util.GenericController;

public class HumanInput extends Input{

	public static HumanInput instance;

	private GenericController driver;
	private GenericController operator;
	
	public HumanInput(){
		init();
	}
	
	public void update(){
		updateDrive();
	}
	
	public void updateDrive(){
		DB_leftSpeed = -driver.getLeftYAxis() + driver.getRightXAxis();
		DB_rightSpeed = -driver.getLeftYAxis() - driver.getRightXAxis();
	
		AR_armUp = driver.getAButton();
	}

	
	public void init(){
		driver = new GenericController(0 ,.1);
		operator = new GenericController(1, .1);
	}
	
	
	public static HumanInput getInstance() {
		return instance == null ? instance = new HumanInput() : instance;
	}
}