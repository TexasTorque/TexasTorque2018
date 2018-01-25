package org.texastorque.io;

public class Input {

	private static Input instance;
	
	protected double DB_leftSpeed;
	protected double DB_rightSpeed;
	protected boolean DB_gearSole = true;
	
	protected volatile double DB_setpoint;
	protected volatile double DB_turnSetpoint;
	protected volatile double DB_precision;
	
	public Input(){
		DB_leftSpeed = 0.0;
		DB_rightSpeed = 0.0;
	}
	
	//Drivebase
	public double getDB_leftSpeed(){
		return DB_leftSpeed;
	}
	
	public double getDB_rightSpeed(){
		return DB_rightSpeed;
	}
	
	public double getDB_setpoint() {
		return DB_setpoint;
	}

	public double getDB_turnSetpoint() {
		return DB_turnSetpoint;
	}
	
	public double getDB_precision() {
		return DB_precision;
	}
	
	public boolean getDB_gearSole() {
		return DB_gearSole;
	}

	public static Input getInstance() {
		return instance == null ? instance = new Input() : instance;
	}

}
