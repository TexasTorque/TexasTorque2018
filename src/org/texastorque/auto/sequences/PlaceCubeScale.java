package org.texastorque.auto.sequences;

import org.texastorque.auto.AutoSequence;
import org.texastorque.auto.arm.SetClaw;
import org.texastorque.auto.arm.ShiftPivotArm;
import org.texastorque.auto.drive.Drive;
import org.texastorque.auto.drive.Turn;

import edu.wpi.first.wpilibj.DriverStation;

public class PlaceCubeScale extends AutoSequence {

	private final int startPos;
	private char scaleSide;

	public PlaceCubeScale(int start) {
		startPos = start;
		try {
			scaleSide = DriverStation.getInstance().getGameSpecificMessage().charAt(1);
		} catch (Exception e) {
			scaleSide = 'X';
		}
		init();
	}

	@Override
	public void init() {
		System.out.println("init PlaceCubeScale");
		if (startPos == 1) {
			handlePosition1();
		} else if (startPos == 3) {
			handlePosition3();
		} else if (startPos == -1 || scaleSide == 'X') {
			commandList.add(new Drive(220, .125, 3.25, true));
		}

		commandList.add(new SetClaw(true, true)); // Open claw after any sequence.
		commandList.add(new Drive(1, .125, 1.0, true));
		commandList.add(new Drive(-12, .125, 2.0, true));
	}

	private void handlePosition1() {
		if (scaleSide == 'L') {
			commandList.add(new ShiftPivotArm(4, 5.0, false, 0.0));
			commandList.add(new Drive(260, 0.125, 4.0, true));
			commandList.add(new Turn(45, 1.5, 1.5, true));
			commandList.add(new Drive(32, .125, 1.5, true));
		} else {
			commandList.add(new ShiftPivotArm(4, 5.0, false, 6.0));
			commandList.add(new Drive(220, 0.125, 3.25, true));
			commandList.add(new Turn(90, 1.5, 1.5, true));
			commandList.add(new Drive(178, 0.125, 3.25, true));
			commandList.add(new Turn(0, 1.5, 2.0, true));
			commandList.add(new Drive(54, 0.125, 2.0, true));
		}
	}

	private void handlePosition3() {
		if (scaleSide == 'R') {
			commandList.add(new ShiftPivotArm(4, 5.0, false, 0.0));
			commandList.add(new Drive(260, 0.125, 4.0, true));
			commandList.add(new Turn(-45, 1.5, 1.5, true));
			commandList.add(new Drive(32, .125, 1.5, true));
		} else {
			commandList.add(new ShiftPivotArm(4, 5.0, false, 6.0));
			commandList.add(new Drive(220, 0.125, 3.25, true));
			commandList.add(new Turn(-90, 1.5, 1.5, true));
			commandList.add(new Drive(178, 0.125, 3.25, true));
			commandList.add(new Turn(0, 1.5, 2.0, true));
			commandList.add(new Drive(54, 0.125, 2.0, true));
		}
	}

}
