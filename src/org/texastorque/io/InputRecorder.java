package org.texastorque.io;

import java.util.ArrayList;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import org.texastorque.auto.AutoManager;
import org.texastorque.auto.AutoMode;
import org.texastorque.auto.AutoModeCollection;
import org.texastorque.feedback.Feedback;
import org.texastorque.subsystems.Drivebase;
import org.texastorque.subsystems.Pivot;
import org.texastorque.torquelib.util.TorqueToggle;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/* The purpose of this class is to read inputs from a controller for the same
 * length as the autonomous period, and then create an XML file from
 * which an autonomous mode can be read. This is the recording/writing class, 
 * AutoManager handles the reading.
 */
public class InputRecorder extends HumanInput{

	protected TorqueToggle recording;
	
	private XMLEncoder writer;
	private static InputRecorder instance;

	private static AutoMode currentMode;
	private static AutoModeCollection amc;
	private static String fileName;
	private static int index;

	
/* The file has to be inside of the roboRIO or else the program will not execute properly because it does not have
 * the file permissions needed to save it.
 */
	
	/*
	 * Write code to reflect an automode? Makes recording easier, not worth the effort unless I am really
	 * bored though
	 * 
	 */
	
	public InputRecorder(){
		init();
	}
	
	//for recording, input double that is 3.0X, and X is dependent on the fieldConfiguration, and during
	//competition, only input the number that corresponds to starting location (IE 3) and the rest should 
	//take care of itself
	public void init(){
		fileName = "2.0LLL";
		recording = new TorqueToggle();
		HumanInput.getInstance().init();			//SEE IF THIS IS NECESSARY
		currentMode = new AutoMode();
		currentMode.name = fileName;
		index = 0;
		amc = AutoModeCollection.getInstance();
	}
	
	
	public void update(){
		HumanInput.getInstance().update();
		updateRecordingStatus();
		if(recording.get()){
			if(index < 1500) {
				recordDrive();
				index++;
			}
		}
			
	}
	
	public void updateRecordingStatus() {
		//	recording.calc(driver.getDPADDown()); 
		//If possible, make this more than 1 button so it is harder to overwrite
		if(driver.getDPADUp()) {		     //something
			writeFile();
			System.out.println("Called Write File");
		}
	}
	
	public void recordDrive(){
		currentMode.setDBLeftSpeed(index, DB_leftSpeed);
		currentMode.setDBRightSpeed(index, DB_rightSpeed);
		currentMode.setDBLeftEncoderRate(index, Feedback.getInstance().getLeftEncoder().getRate());
		currentMode.setDBRightEncoderRate(index, Feedback.getInstance().getRightEncoder().getRate());
	}

	public void recordArm() {
		currentMode.setAMSpeed(index, AM_speed);
	}
	
	public void recordClaw() {
		currentMode.setCLState(index, CL_closed.get());
	}
	
	public void recordPivot() {
		currentMode.setPTSpeed(index, Pivot.getInstance().getSpeed());
	}
	
	public void recordIntake() {
		currentMode.setINPneumatics(index, IN_down.get(), IN_out.get());
	}

	public void writeFile(){
		amc.addMode(currentMode);
	/*
		try {
			writer = new XMLEncoder(new FileOutputStream(fileName));
			writer.writeObject(currentMode);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		try {
			writer = new XMLEncoder(new FileOutputStream(amc.getFileLocation()));
			writer.writeObject(amc);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static InputRecorder getInstance(){
		return instance == null ? instance = new InputRecorder() : instance;
	}
}